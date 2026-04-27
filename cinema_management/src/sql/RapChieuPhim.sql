-- =============================================
-- DATABASE: RapChieuPhim (Full Schema)
-- SQL Server 2019+
-- =============================================
CREATE DATABASE RapChieuPhim;
GO
USE RapChieuPhim;
GO

-- 1. Bảng Phim
CREATE TABLE Phim (
    maPhim VARCHAR(20) PRIMARY KEY,
    tenPhim NVARCHAR(255) NOT NULL,
    theLoai NVARCHAR(100),
    daoDien NVARCHAR(100),
    thoiLuong INT, -- Phút
    ngayKhoiChieu DATE,
    moTa NVARCHAR(MAX),
	duongDanAnh NVARCHAR(255)
);

-- 2. Bảng PhongChieu
CREATE TABLE PhongChieu (
    maPhong VARCHAR(20) PRIMARY KEY,
    tenPhong NVARCHAR(100),
    soGhe INT,
    loaiPhong NVARCHAR(50) -- 2D, 3D, IMAX
);

-- 3. Bảng Ghe (Quan hệ 1..* - 1 với PhongChieu)
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    maPhong VARCHAR(20),
    hang VARCHAR(5),
    soGhe INT, -- Số thứ tự ghế trong hàng
    FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong)
);

-- 4. Bảng SuatChieu (Quan hệ 1 - 1..* với Vé, không chứa mã Ghế)
CREATE TABLE SuatChieu (
    maSC VARCHAR(20) PRIMARY KEY,
    maPhong VARCHAR(20),
    maPhim VARCHAR(20),
    ngayChieu DATE,
    gioChieu DATETIME,
    FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong),
    FOREIGN KEY (maPhim) REFERENCES Phim(maPhim)
);

-- 5. Bảng NhanVien (Hiển thị đầy đủ các thuộc tính)
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(255) NOT NULL,
    gioiTinh BIT, -- 1: Nam, 0: Nữ
    ngaySinh DATE,
    SDT VARCHAR(15),
    email VARCHAR(100),
    diaChi NVARCHAR(255),
    chucVu NVARCHAR(100)
);

-- 6. Bảng KhachHang (Quan hệ 1 - 1..* với HoaDon)
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKhachHang NVARCHAR(255) NOT NULL,
    SDT VARCHAR(15),
    Email VARCHAR(100),
    diemTichLuy INT DEFAULT 0
);

-- 7. Bảng KhuyenMai (Quan hệ 1 - 0..1 với HoaDon)
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(255),
    phanTramGiam DECIMAL(5, 2), -- Ví dụ: 10.00 (%)
    ngayBatDau DATE,
    ngayKetThuc DATE
);

-- 8. Bảng Thue (Quan hệ 1 - 0..* với HoaDon)
CREATE TABLE Thue (
    maThue VARCHAR(20) PRIMARY KEY,
    tenThue NVARCHAR(100),
    phanTramThue DECIMAL(5, 2) -- Ví dụ: 8.00 (%) cho VAT
);

-- 9. Bảng HoaDon
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20),
    maKH VARCHAR(20) NOT NULL, -- Bắt buộc có khách hàng
    maKM VARCHAR(20) NULL,     -- Có thể có hoặc không
    maThue VARCHAR(20) NULL,   -- Có thể có hoặc không
    ngayLap DATE DEFAULT GETDATE(),
    tongTien DECIMAL(18, 2),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue)
);

-- 10. Bảng Ve (Quan hệ 1-1 với Ghe trong 1 Suất chiếu)
CREATE TABLE Ve (
    maVe VARCHAR(20) PRIMARY KEY,
    maSC VARCHAR(20) NOT NULL,
    maGhe VARCHAR(20) NOT NULL,
    giaVe DECIMAL(18, 2),
    trangThai NVARCHAR(50), -- Đã bán, Chờ thanh toán
    FOREIGN KEY (maSC) REFERENCES SuatChieu(maSC),
    FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    -- Đảm bảo 1 ghế trong 1 suất chiếu chỉ được có 1 vé duy nhất
    CONSTRAINT UC_Ghe_SuatChieu UNIQUE (maSC, maGhe)
);

-- 11. Bảng ChiTietHoaDon (Kết nối Hóa đơn và Vé)
CREATE TABLE ChiTietHoaDon (
    maCTHD VARCHAR(20) PRIMARY KEY,
    maHoaDon VARCHAR(20),
    maVe VARCHAR(20) UNIQUE, -- 1 Vé chỉ xuất hiện trên 1 hóa đơn
    soLuong INT DEFAULT 1,
    thanhTien DECIMAL(18, 2),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);
GO
INSERT INTO Phim (maPhim, tenPhim, theLoai, daoDien, thoiLuong, ngayKhoiChieu, moTa, duongDanAnh)
VALUES 
('P001', N'Lật Mặt 7: Một Điều Ước', N'Gia đình, Tâm lý', N'Lý Hải', 138, '2024-04-26', N'Câu chuyện về người mẹ già và 5 người con.', 'latmat7.jpg'),

('P002', N'Đất Rừng Phương Nam', N'Hành động, Phiêu lưu', N'Nguyễn Quang Dũng', 110, '2023-10-20', N'Hành trình tìm cha của bé An.', 'datrung.jpg'),

('P003', N'Mai', N'Tâm lý, Tình cảm', N'Trấn Thành', 131, '2024-02-10', N'Câu chuyện về cuộc đời người phụ nữ tên Mai.', 'mai.jpg'),

('P004', N'Avengers: Endgame', N'Hành động, Viễn tưởng', N'Anthony Russo', 181, '2019-04-26', N'Trận chiến cuối cùng của các siêu anh hùng.', 'avengers.jpg'),

('P005', N'Mắt Biếc', N'Lãng mạn', N'Victor Vũ', 117, '2019-12-20', N'Tình yêu đơn phương của Ngạn dành cho Hà Lan.', 'matbiec.jpg');
GO

select *from Phim