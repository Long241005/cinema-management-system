-- Tạo Database Cinema Management System
-- PostgreSQL SQL Script

-- Tạo database
CREATE DATABASE cinema_db;

-- Kết nối vào database
\c cinema_db;

-- Bảng Phim
CREATE TABLE phim (
    id_phim SERIAL PRIMARY KEY,
    ten_phim VARCHAR(255) NOT NULL,
    dao_dien VARCHAR(100),
    the_loai VARCHAR(50),
    thoi_luong INT,
    ngon_ngu VARCHAR(50),
    mo_ta TEXT,
    url_poster VARCHAR(255),
    danh_gia DECIMAL(3,1) DEFAULT 0.0,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Phòng Chiếu
CREATE TABLE phong (
    id_phong SERIAL PRIMARY KEY,
    ten_phong VARCHAR(100) NOT NULL,
    tong_so_ghe INT NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'AVAILABLE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Ghế
CREATE TABLE ghe (
    id_ghe SERIAL PRIMARY KEY,
    id_phong INT NOT NULL,
    hang_ghe CHAR(1) NOT NULL,
    so_ghe INT NOT NULL,
    loai_ghe VARCHAR(50) DEFAULT 'STANDARD',
    gia DECIMAL(10,2) NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'AVAILABLE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_phong) REFERENCES phong(id_phong) ON DELETE CASCADE,
    UNIQUE(id_phong, hang_ghe, so_ghe)
);

-- Bảng Suất Chiếu
CREATE TABLE suat_chieu (
    id_suat SERIAL PRIMARY KEY,
    id_phim INT NOT NULL,
    id_phong INT NOT NULL,
    thoi_gian_bat_dau TIMESTAMP NOT NULL,
    thoi_gian_ket_thuc TIMESTAMP NOT NULL,
    gia_ve DECIMAL(10,2) NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'ACTIVE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_phim) REFERENCES phim(id_phim) ON DELETE CASCADE,
    FOREIGN KEY (id_phong) REFERENCES phong(id_phong) ON DELETE CASCADE
);

-- Bảng Khách Hàng
CREATE TABLE khach_hang (
    id_khach_hang SERIAL PRIMARY KEY,
    ten_khach_hang VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    so_dien_thoai VARCHAR(20),
    cap_thanh_vien VARCHAR(50) DEFAULT 'BRONZE',
    diem_trung_thanh INT DEFAULT 0,
    ngay_dang_ky DATE NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'ACTIVE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Vé
CREATE TABLE ve (
    id_ve SERIAL PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    id_suat INT NOT NULL,
    id_ghe INT NOT NULL,
    gia_ve DECIMAL(10,2) NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'PENDING',
    ngay_dat DATE NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE,
    FOREIGN KEY (id_suat) REFERENCES suat_chieu(id_suat) ON DELETE CASCADE,
    FOREIGN KEY (id_ghe) REFERENCES ghe(id_ghe) ON DELETE CASCADE
);

-- Bảng Hóa Đơn
CREATE TABLE hoa_don (
    id_hoa_don SERIAL PRIMARY KEY,
    id_khach_hang INT NOT NULL,
    tong_tien DECIMAL(10,2) NOT NULL,
    tien_giam DECIMAL(10,2) DEFAULT 0,
    tong_thanh_toan DECIMAL(10,2) NOT NULL,
    phuong_thuc_thanh_toan VARCHAR(50) DEFAULT 'CASH',
    ngay_lap DATE NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'PENDING',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_khach_hang) REFERENCES khach_hang(id_khach_hang) ON DELETE CASCADE
);

-- Bảng Chi Tiết Hóa Đơn
CREATE TABLE chi_tiet_hoa_don (
    id_chi_tiet SERIAL PRIMARY KEY,
    id_hoa_don INT NOT NULL,
    id_ve INT NOT NULL,
    so_luong INT DEFAULT 1,
    don_gia DECIMAL(10,2) NOT NULL,
    thanh_tien DECIMAL(10,2) NOT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_hoa_don) REFERENCES hoa_don(id_hoa_don) ON DELETE CASCADE,
    FOREIGN KEY (id_ve) REFERENCES ve(id_ve) ON DELETE CASCADE
);

-- Bảng Nhân Viên
CREATE TABLE nhan_vien (
    id_nhan_vien SERIAL PRIMARY KEY,
    ten_nhan_vien VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    so_dien_thoai VARCHAR(20),
    chuc_vu VARCHAR(50) DEFAULT 'STAFF',
    luong DECIMAL(12,2),
    ngay_vao_lam DATE NOT NULL,
    trang_thai VARCHAR(20) DEFAULT 'ACTIVE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Khuyến Mãi
CREATE TABLE khuyen_mai (
    id_khuyen_mai SERIAL PRIMARY KEY,
    ten_khuyen_mai VARCHAR(255) NOT NULL,
    mo_ta TEXT,
    phan_tram_giam DECIMAL(5,2) DEFAULT 0,
    so_tien_giam DECIMAL(10,2) DEFAULT 0,
    ngay_bat_dau DATE NOT NULL,
    ngay_ket_thuc DATE NOT NULL,
    so_luong_toi_da INT,
    da_su_dung INT DEFAULT 0,
    trang_thai VARCHAR(20) DEFAULT 'ACTIVE',
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes cho tìm kiếm nhanh
CREATE INDEX idx_phim_ten ON phim(ten_phim);
CREATE INDEX idx_phim_the_loai ON phim(the_loai);
CREATE INDEX idx_suat_chieu_id_phim ON suat_chieu(id_phim);
CREATE INDEX idx_suat_chieu_id_phong ON suat_chieu(id_phong);
CREATE INDEX idx_suat_chieu_thoi_gian ON suat_chieu(thoi_gian_bat_dau);
CREATE INDEX idx_ve_id_khach_hang ON ve(id_khach_hang);
CREATE INDEX idx_ve_id_suat ON ve(id_suat);
CREATE INDEX idx_khach_hang_email ON khach_hang(email);
CREATE INDEX idx_hoa_don_id_khach_hang ON hoa_don(id_khach_hang);
CREATE INDEX idx_hoa_don_ngay_lap ON hoa_don(ngay_lap);
CREATE INDEX idx_nhan_vien_email ON nhan_vien(email);
CREATE INDEX idx_khuyen_mai_ngay ON khuyen_mai(ngay_bat_dau, ngay_ket_thuc);

-- Thêm một số dữ liệu mẫu
INSERT INTO phim (ten_phim, dao_dien, the_loai, thoi_luong, ngon_ngu, mo_ta, danh_gia) VALUES
('Avengers: Endgame', 'Anthony Russo, Joe Russo', 'Action', 181, 'English', 'Cuộc chiến cuối cùng', 8.4),
('The Avatar', 'James Cameron', 'Sci-Fi', 162, 'English', 'Hành trình đến Pandora', 7.8),
('Inception', 'Christopher Nolan', 'Thriller', 148, 'English', 'Giấc mơ trong giấc mơ', 8.8),
('Titanic', 'James Cameron', 'Romance', 194, 'English', 'Tình yêu trên tàu lớn', 7.8);

INSERT INTO phong (ten_phong, tong_so_ghe, trang_thai) VALUES
('Phòng 1', 100, 'AVAILABLE'),
('Phòng 2', 120, 'AVAILABLE'),
('Phòng 3', 80, 'AVAILABLE');

-- Tạo dữ liệu ghế cho Phòng 1
INSERT INTO ghe (id_phong, hang_ghe, so_ghe, loai_ghe, gia, trang_thai)
SELECT 1, hang_char, so_ghe, 
  CASE WHEN so_ghe > 8 THEN 'VIP' ELSE 'STANDARD' END,
  CASE WHEN so_ghe > 8 THEN 100000 ELSE 80000 END,
  'AVAILABLE'
FROM (
  SELECT ARRAY['A', 'B', 'C', 'D', 'E']::char[] AS hang_array
) AS t,
LATERAL (
  SELECT UNNEST(hang_array) AS hang_char
) AS hang,
LATERAL (
  SELECT generate_series(1, 10) AS so_ghe
) AS ghe;

INSERT INTO khach_hang (ten_khach_hang, email, so_dien_thoai, cap_thanh_vien, diem_trung_thanh, ngay_dang_ky, trang_thai) VALUES
('Nguyễn Văn A', 'nguyenvana@gmail.com', '0123456789', 'BRONZE', 0, CURRENT_DATE, 'ACTIVE'),
('Trần Thị B', 'tranthib@gmail.com', '0987654321', 'SILVER', 50, CURRENT_DATE, 'ACTIVE');

INSERT INTO nhan_vien (ten_nhan_vien, email, so_dien_thoai, chuc_vu, luong, ngay_vao_lam, trang_thai) VALUES
('Admin T3L', 'admin@cinema.com', '0123456789', 'ADMIN', 10000000, CURRENT_DATE, 'ACTIVE'),
('Nhân viên 1', 'staff1@cinema.com', '0987654321', 'STAFF', 5000000, CURRENT_DATE, 'ACTIVE');

-- Commit
COMMIT;
