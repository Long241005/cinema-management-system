-- =============================================
-- DATABASE: RapChieuPhim (Full Schema)
-- SQL Server 2019+
-- =============================================
CREATE DATABASE RapChieuPhim;
GO
USE RapChieuPhim;
GO

-- 1. Bảng Phim
CREATE TABLE TheLoai (
    maTheLoai VARCHAR(20) PRIMARY KEY,
    tenTheLoai NVARCHAR(100) NOT NULL
);

-- 1. Bảng Phim (Sửa theLoai thành maTheLoai để làm khóa ngoại)
CREATE TABLE Phim (
    maPhim VARCHAR(20) PRIMARY KEY,
    tenPhim NVARCHAR(255) NOT NULL,
    maTheLoai VARCHAR(20), -- Khóa ngoại trỏ sang bảng TheLoai
    daoDien NVARCHAR(100),
    thoiLuong INT,
    ngayKhoiChieu DATE,
    moTa NVARCHAR(MAX),
    duongDanAnh NVARCHAR(255),
    FOREIGN KEY (maTheLoai) REFERENCES TheLoai(maTheLoai)
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

INSERT INTO TheLoai (maTheLoai, tenTheLoai) VALUES 
('TL0001', N'Hành động'),
('TL0002', N'Hoạt hình'),
('TL0003', N'Kinh dị'),
('TL0004', N'Tâm lý'),
('TL0005', N'Lãng mạn'),
('TL0006', N'Gia đình');

INSERT INTO Phim (maPhim, tenPhim, maTheLoai, daoDien, thoiLuong, ngayKhoiChieu, moTa, duongDanAnh)
VALUES 
('P001', N'Lật Mặt 7: Một Điều Ước', 'TL0006', N'Lý Hải', 138, '2024-04-26', N'Câu chuyện về người mẹ già và 5 người con.', 'latmat7.jpg'),
('P002', N'Đất Rừng Phương Nam', 'TL0001', N'Nguyễn Quang Dũng', 110, '2023-10-20', N'Hành trình tìm cha của bé An.', 'datrung.jpg'),
('P003', N'Mai', 'TL0004', N'Trấn Thành', 131, '2024-02-10', N'Câu chuyện về cuộc đời người phụ nữ tên Mai.', 'mai.jpg'),
('P004', N'Avengers: Endgame', 'TL0001', N'Anthony Russo', 181, '2019-04-26', N'Trận chiến cuối cùng của các siêu anh hùng.', 'avengers.jpg'),
('P005', N'Mắt Biếc', 'TL0005', N'Victor Vũ', 117, '2019-12-20', N'Tình yêu đơn phương của Ngạn dành cho Hà Lan.', 'matbiec.jpg'),
('P006', N'Quỷ Cẩu', 'TL0003', N'Võ Thanh Hòa', 91, '2023-12-29', N'Chuyện tâm linh xoay quanh một gia đình làm nghề mổ chó.', 'quycau.jpg'),
('P007', N'Doraemon: Bản Tình Ca Nobita', 'TL0002', N'Imai Kazuaki', 115, '2024-05-24', N'Nobita và những người bạn dùng âm nhạc để cứu thế giới.', 'doraemon2024.jpg'),
('P008', N'Bố Già', 'TL0006', N'Trấn Thành', 128, '2021-03-05', N'Mâu thuẫn giữa cha và con trai trong một xóm nghèo.', 'bogia.jpg'),
('P009', N'Spider-Man: Across the Spider-Verse', 'TL0002', N'Joaquim Dos Santos', 140, '2023-06-02', N'Miles Morales du hành qua đa vũ trụ nhện.', 'spiderman_verse.jpg'),
('P010', N'Nhà Bà Nữ', 'TL0006', N'Trấn Thành', 102, '2023-01-22', N'Những rắc rối và định kiến trong gia đình bà bán bánh canh cua.', 'nhabanu.jpg'),
('P011', N'Dune: Hành Tinh Cát - Phần 2', 'TL0001', N'Denis Villeneuve', 166, '2024-03-01', N'Paul Atreides tiếp tục hành trình trả thù.', 'dune2.jpg'),
('P012', N'Minions: Sự Trỗi Dậy Của Gru', 'TL0002', N'Kyle Balda', 87, '2022-07-01', N'Hành trình của Gru thời niên thiếu.', 'minions.jpg'),
('P013', N'Kẻ Ăn Hồn', 'TL0003', N'Trần Hữu Tấn', 109, '2023-12-15', N'Những cái chết bí ẩn tại làng Địa Ngục.', 'keanhon.jpg'),
('P014', N'John Wick: Phần 4', 'TL0001', N'Chad Stahelski', 169, '2023-03-24', N'John Wick tìm cách đánh bại Hội Đồng Tối Cao.', 'johnwick4.jpg'),
('P015', N'Your Name', 'TL0002', N'Makoto Shinkai', 107, '2017-01-13', N'Câu chuyện hoán đổi thân xác kỳ lạ.', 'yourname.jpg');

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

INSERT INTO KhachHang (maKH, tenKhachHang, SDT, Email, diemTichLuy) VALUES
('KH001', N'Nguyễn Văn An', '0901234567', 'an.nguyen@gmail.com', 120),
('KH002', N'Trần Thị Bình', '0912345678', 'binh.tran@gmail.com', 50),
('KH003', N'Lê Văn Cường', '0923456789', 'cuong.le@gmail.com', 200),
('KH004', N'Phạm Thị Dung', '0934567890', 'dung.pham@gmail.com', 80),
('KH005', N'Hoàng Văn Em', '0945678901', 'em.hoang@gmail.com', 30),
('KH006', N'Đỗ Thị Giang', '0956789012', 'giang.do@gmail.com', 150),
('KH007', N'Vũ Văn Hải', '0967890123', 'hai.vu@gmail.com', 60),
('KH008', N'Bùi Thị Hạnh', '0978901234', 'hanh.bui@gmail.com', 90),
('KH009', N'Ngô Văn Khang', '0989012345', 'khang.ngo@gmail.com', 40),
('KH010', N'Dương Thị Lan', '0990123456', 'lan.duong@gmail.com', 170),

('KH011', N'Nguyễn Văn Minh', '0901122334', 'minh.nguyen@gmail.com', 300),
('KH012', N'Trần Thị Nga', '0912233445', 'nga.tran@gmail.com', 20),
('KH013', N'Lê Văn Phúc', '0923344556', 'phuc.le@gmail.com', 110),
('KH014', N'Phạm Thị Quỳnh', '0934455667', 'quynh.pham@gmail.com', 75),
('KH015', N'Hoàng Văn Sơn', '0945566778', 'son.hoang@gmail.com', 55),
('KH016', N'Đỗ Thị Trang', '0956677889', 'trang.do@gmail.com', 95),
('KH017', N'Vũ Văn Tuấn', '0967788990', 'tuan.vu@gmail.com', 10),
('KH018', N'Bùi Thị Uyên', '0978899001', 'uyen.bui@gmail.com', 130),
('KH019', N'Ngô Văn Việt', '0989900112', 'viet.ngo@gmail.com', 160),
('KH020', N'Dương Thị Yến', '0990011223', 'yen.duong@gmail.com', 70);

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



INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai)
VALUES 
('G0006', 'P0002', 'B', 3, N'VIP', N'Còn trống'),
('G0007', 'P0002', 'B', 4, N'VIP', N'Còn trống'),
('G0008', 'P0002', 'B', 5, N'VIP', N'Còn trống'),
('G0009', 'P0002', 'B', 6, N'VIP', N'Còn trống'),
('G0010', 'P0002', 'B', 7, N'Thường', N'Còn trống'),
('G0011', 'P0002', 'B', 8, N'Thường', N'Còn trống'),
('G0012', 'P0002', 'B', 9, N'Thường', N'Còn trống'),
('G0013', 'P0002', 'B', 10, N'Thường', N'Còn trống');

-- Hàng C (Ghế từ G0014 đến G0025)
INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai)
VALUES 
('G0014', 'P0002', 'C', 1, N'Thường', N'Còn trống'),
('G0015', 'P0002', 'C', 2, N'Thường', N'Còn trống'),
('G0016', 'P0002', 'C', 3, N'VIP', N'Còn trống'),
('G0017', 'P0002', 'C', 4, N'VIP', N'Còn trống'),
('G0018', 'P0002', 'C', 5, N'VIP', N'Còn trống'),
('G0019', 'P0002', 'C', 6, N'VIP', N'Còn trống'),
('G0020', 'P0002', 'C', 7, N'VIP', N'Còn trống'),
('G0021', 'P0002', 'C', 8, N'VIP', N'Còn trống'),
('G0022', 'P0002', 'C', 9, N'Thường', N'Còn trống'),
('G0023', 'P0002', 'C', 10, N'Thường', N'Còn trống'),
('G0024', 'P0002', 'C', 11, N'Thường', N'Còn trống'),
('G0025', 'P0002', 'C', 12, N'Thường', N'Còn trống');

-- Hàng D (Ghế từ G0026 đến G0038)
INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai)
VALUES 
('G0026', 'P0002', 'D', 1, N'Thường', N'Còn trống'),
('G0027', 'P0002', 'D', 2, N'Thường', N'Còn trống'),
('G0028', 'P0002', 'D', 3, N'VIP', N'Còn trống'),
('G0029', 'P0002', 'D', 4, N'VIP', N'Còn trống'),
('G0030', 'P0002', 'D', 5, N'VIP', N'Còn trống'),
('G0031', 'P0002', 'D', 6, N'VIP', N'Còn trống'),
('G0032', 'P0002', 'D', 7, N'VIP', N'Còn trống'),
('G0033', 'P0002', 'D', 8, N'VIP', N'Còn trống'),
('G0034', 'P0002', 'D', 9, N'Thường', N'Còn trống'),
('G0035', 'P0002', 'D', 10, N'Thường', N'Còn trống'),
('G0036', 'P0002', 'D', 11, N'Thường', N'Còn trống'),
('G0037', 'P0002', 'D', 12, N'Thường', N'Còn trống'),
('G0038', 'P0002', 'D', 13, N'Thường', N'Còn trống');

-- Hàng E (Ghế từ G0039 đến G0050)
INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai)
VALUES 
('G0039', 'P0002', 'E', 1, N'Thường', N'Còn trống'),
('G0040', 'P0002', 'E', 2, N'Thường', N'Còn trống'),
('G0041', 'P0002', 'E', 3, N'Thường', N'Còn trống'),
('G0042', 'P0002', 'E', 4, N'Thường', N'Còn trống'),
('G0043', 'P0002', 'E', 5, N'Thường', N'Còn trống'),
('G0044', 'P0002', 'E', 6, N'Thường', N'Còn trống'),
('G0045', 'P0002', 'E', 7, N'Thường', N'Còn trống'),
('G0046', 'P0002', 'E', 8, N'Thường', N'Còn trống'),
('G0047', 'P0002', 'E', 9, N'Thường', N'Còn trống'),
('G0048', 'P0002', 'E', 10, N'Thường', N'Còn trống'),
('G0049', 'P0002', 'E', 11, N'Thường', N'Còn trống'),
('G0050', 'P0002', 'E', 12, N'Thường', N'Còn trống');

INSERT INTO KhuyenMai (maKM, tenKhuyenMai, phanTramGiam, ngayBatDau, ngayKetThuc) VALUES
('KM001', N'Khuyến mãi đầu năm', 10.00, '2026-01-01', '2026-01-15'),
('KM002', N'Ưu đãi Tết Nguyên Đán', 20.00, '2026-02-01', '2026-02-10'),
('KM003', N'Valentine ngọt ngào', 15.00, '2026-02-13', '2026-02-15'),
('KM004', N'Chào hè rực rỡ', 25.00, '2026-05-01', '2026-05-31'),
('KM005', N'Ngày Quốc tế Thiếu nhi', 30.00, '2026-06-01', '2026-06-05'),
('KM006', N'Khuyến mãi mùa hè', 20.00, '2026-06-10', '2026-07-10'),
('KM007', N'Back to School', 15.00, '2026-08-15', '2026-09-05'),
('KM008', N'Ưu đãi Halloween', 35.00, '2026-10-25', '2026-10-31'),
('KM009', N'Black Friday Sale', 40.00, '2026-11-25', '2026-11-30'),
('KM010', N'Giáng Sinh an lành', 50.00, '2026-12-20', '2026-12-31');
