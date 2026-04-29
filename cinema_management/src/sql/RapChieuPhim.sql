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
    thoiLuong INT,
    ngayKhoiChieu DATE,
    moTa NVARCHAR(MAX),
	duongDanAnh NVARCHAR(255)
);

-- 2. Bảng PhongChieu
CREATE TABLE PhongChieu (
    maPhong VARCHAR(20) PRIMARY KEY,
    tenPhong NVARCHAR(100),
    soGhe INT,
    loaiPhong NVARCHAR(50) 
);

-- 3. Bảng Ghe (Quan hệ 1..* - 1 với PhongChieu)
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    maPhong VARCHAR(20),
    hang VARCHAR(5),
    soGhe INT,
    loaiGhe NVARCHAR(50) DEFAULT N'Thường',
    trangThai NVARCHAR(50) DEFAULT N'Còn trống', -- Thuộc tính mới ở đây
    FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong)
);


-- 4. Bảng SuatChieu
CREATE TABLE SuatChieu (
    maSC VARCHAR(20) PRIMARY KEY,
    maPhong VARCHAR(20),
    maPhim VARCHAR(20),
    ngayChieu DATE,
    gioChieu DATETIME,
    FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong),
    FOREIGN KEY (maPhim) REFERENCES Phim(maPhim)
);

-- 5. Bảng NhanVien 
CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(255) NOT NULL,
    gioiTinh BIT, 
    ngaySinh DATE,
    SDT VARCHAR(15),
    email VARCHAR(100),
    diaChi NVARCHAR(255),
    chucVu NVARCHAR(100)
);

-- 6. Bảng KhachHang 
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKhachHang NVARCHAR(255) NOT NULL,
    SDT VARCHAR(15),
    Email VARCHAR(100),
    diemTichLuy INT DEFAULT 0
);

-- 7. Bảng KhuyenMai 
CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(255),
    phanTramGiam DECIMAL(5, 2),
    ngayBatDau DATE,
    ngayKetThuc DATE
);

-- 8. Bảng Thue 
CREATE TABLE Thue (
    maThue VARCHAR(20) PRIMARY KEY,
    tenThue NVARCHAR(100),
    phanTramThue DECIMAL(5, 2)
);

-- 9. Bảng HoaDon
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20),
    maKH VARCHAR(20) NOT NULL, 
    maKM VARCHAR(20) NULL,    
    maThue VARCHAR(20) NULL,  
    ngayLap DATE DEFAULT GETDATE(),
    tongTien DECIMAL(18, 2),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue)
);

-- 10. Bảng Ve 
CREATE TABLE Ve (
    maVe VARCHAR(20) PRIMARY KEY,
    maSC VARCHAR(20) NOT NULL,
    maGhe VARCHAR(20) NOT NULL,
    giaVe DECIMAL(18, 2),
    trangThai NVARCHAR(50),
    FOREIGN KEY (maSC) REFERENCES SuatChieu(maSC),
    FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT UC_Ghe_SuatChieu UNIQUE (maSC, maGhe)
);

-- 11. Bảng ChiTietHoaDon 
CREATE TABLE ChiTietHoaDon (
    maCTHD VARCHAR(20) PRIMARY KEY,
    maHoaDon VARCHAR(20),
    maVe VARCHAR(20) UNIQUE,
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

('P005', N'Mắt Biếc', N'Lãng mạn', N'Victor Vũ', 117, '2019-12-20', N'Tình yêu đơn phương của Ngạn dành cho Hà Lan.', 'matbiec.jpg'),

('P006', N'Quỷ Cẩu', N'Kinh dị', N'Võ Thanh Hòa', 91, '2023-12-29', N'Chuyện tâm linh xoay quanh một gia đình làm nghề mổ chó.', 'quycau.jpg'),

('P007', N'Doraemon: Bản Tình Ca Nobita', N'Hoạt hình, Phiêu lưu', N'Imai Kazuaki', 115, '2024-05-24', N'Nobita và những người bạn dùng âm nhạc để cứu thế giới.', 'doraemon2024.jpg'),

('P008', N'Bố Già', N'Gia đình, Hài', N'Trấn Thành', 128, '2021-03-05', N'Mâu thuẫn giữa cha và con trai trong một xóm nghèo.', 'bogia.jpg'),

('P009', N'Spider-Man: Across the Spider-Verse', N'Hoạt hình, Hành động', N'Joaquim Dos Santos', 140, '2023-06-02', N'Miles Morales du hành qua đa vũ trụ nhện.', 'spiderman_verse.jpg'),

('P010', N'Nhà Bà Nữ', N'Tâm lý, Gia đình', N'Trấn Thành', 102, '2023-01-22', N'Những rắc rối và định kiến trong gia đình bà bán bánh canh cua.', 'nhabanu.jpg'),
('P011', N'Dune: Hành Tinh Cát - Phần 2', N'Hành động, Viễn tưởng', N'Denis Villeneuve', 166, '2024-03-01', N'Paul Atreides tiếp tục hành trình trả thù những kẻ đã hủy diệt gia đình mình.', 'dune2.jpg'),

('P012', N'Minions: Sự Trỗi Dậy Của Gru', N'Hoạt hình, Hài', N'Kyle Balda', 87, '2022-07-01', N'Hành trình của Gru thời niên thiếu để trở thành siêu ác nhân cùng các Minions.', 'minions.jpg'),

('P013', N'Kẻ Ăn Hồn', N'Kinh dị, Cổ trang', N'Trần Hữu Tấn', 109, '2023-12-15', N'Những cái chết bí ẩn tại làng Địa Ngục liên quan đến tà thuật.', 'keanhon.jpg'),

('P014', N'John Wick: Phần 4', N'Hành động, Giật gân', N'Chad Stahelski', 169, '2023-03-24', N'John Wick tìm cách đánh bại Hội Đồng Tối Cao để giành lấy tự do.', 'johnwick4.jpg'),

('P015', N'Your Name', N'Hoạt hình, Lãng mạn', N'Makoto Shinkai', 107, '2017-01-13', N'Câu chuyện hoán đổi thân xác kỳ lạ giữa hai thiếu niên Mitsuha và Taki.', 'yourname.jpg');

INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, SDT, email, diaChi, chucVu)
VALUES
('NV001', N'Nguyễn Văn An', 1, '1998-03-15', '0901234567', 'an.nguyen@gmail.com', N'Hà Nội', N'Quản lý'),

('NV002', N'Trần Thị Bình', 0, '1999-07-21', '0902345678', 'binh.tran@gmail.com', N'Hồ Chí Minh', N'Nhân viên bán vé'),

('NV003', N'Lê Văn Cường', 1, '1997-11-10', '0903456789', 'cuong.le@gmail.com', N'Đà Nẵng', N'Nhân viên bán vé'),

('NV004', N'Phạm Thị Dung', 0, '2000-01-05', '0904567890', 'dung.pham@gmail.com', N'Hải Phòng', N'Nhân viên bán vé'),

('NV005', N'Hoàng Văn Em', 1, '1996-09-18', '0905678901', 'em.hoang@gmail.com', N'Cần Thơ', N'Quản lý'),

('NV006', N'Võ Thị Giang', 0, '1998-12-30', '0906789012', 'giang.vo@gmail.com', N'Nha Trang', N'Nhân viên bán vé'),

('NV007', N'Đặng Văn Hùng', 1, '1995-06-25', '0907890123', 'hung.dang@gmail.com', N'Bình Dương', N'Nhân viên bán vé'),

('NV008', N'Bùi Thị Lan', 0, '2001-04-14', '0908901234', 'lan.bui@gmail.com', N'Đồng Nai', N'Nhân viên bán vé'),

('NV009', N'Ngô Văn Minh', 1, '1997-08-09', '0909012345', 'minh.ngo@gmail.com', N'Vũng Tàu', N'Nhân viên bán vé'),

('NV010', N'Đỗ Thị Ngọc', 0, '1999-10-27', '0910123456', 'ngoc.do@gmail.com', N'Huế', N'Nhân viên bán vé');
--DELETE FROM Phim;



INSERT INTO PhongChieu (maPhong, tenPhong, soGhe, loaiPhong)
VALUES 
('P0001', N'Phòng Chiếu 01', 50, N'2D'),
('P0002', N'Phòng Chiếu 02', 50, N'2D'),
('P0003', N'Phòng Chiếu 03', 30, N'3D'),
('P0004', N'Phòng IMAX', 100, N'IMAX');
-- Thêm ghế cho Phòng 1 (P0001)


INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai)
VALUES 
('G0001', 'P0001', 'A', 1, N'Thường', N'Còn trống'),
('G0002', 'P0001', 'A', 2, N'Thường', N'Còn trống'),
('G0003', 'P0001', 'A', 3, N'VIP', N'Còn trống'),
('G0004', 'P0002', 'B', 1, N'Thường', N'Còn trống'),
('G0005', 'P0002', 'B', 2, N'Thường', N'Còn trống');


select *from Phim
select *from NhanVien
select *from Ghe




--DELETE FROM Ghe;
--DELETE FROM PhongChieu;