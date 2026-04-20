# Hệ Thống Quản Lý Rạp Chiếu Phim T3L

## Giới Thiệu
Ứng dụng Desktop Java Swing để quản lý rạp chiếu phim, hỗ trợ đặt vé, chọn ghế, thanh toán và quản lý hoàn toàn.

## Công Nghệ Sử Dụng
- **Ngôn ngữ**: Java 11+
- **UI Framework**: Java Swing
- **Database**: PostgreSQL
- **JDBC Driver**: postgresql-42.x.x
- **Build Tool**: Maven hoặc Gradle

## Yêu Cầu Hệ Thống
- JDK 11 hoặc cao hơn
- PostgreSQL 12+
- Maven 3.6+ hoặc Gradle 6.0+

## Hướng Dẫn Cài Đặt

### 1. Cài Đặt PostgreSQL
- Download và cài đặt PostgreSQL từ https://www.postgresql.org/download/
- Ghi nhớ mật khẩu của user `postgres`
- Mặc định, PostgreSQL chạy trên port 5432

### 2. Tạo Database
```bash
# Mở psql (PostgreSQL Command Line)
psql -U postgres

# Tạo database
CREATE DATABASE cinema_db;

# Kết nối vào database mới
\c cinema_db

# Import SQL script (nếu cần)
\i /path/to/scripts/create_database.sql
```

Hoặc chạy script từ file:
```bash
psql -U postgres -d cinema_db -f scripts/create_database.sql
```

### 3. Cấu Hình DatabaseConnection
Mở file `src/connect/DatabaseConnection.java` và cập nhật thông tin kết nối:
```java
private static final String SERVER = "localhost";
private static final String PORT = "5432";
private static final String DATABASE = "cinema_db";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";
```

### 4. Build Project
```bash
# Với Maven
mvn clean install

# Với Gradle
gradle build
```

### 5. Chạy Ứng Dụng
```bash
# Với Maven
mvn exec:java -Dexec.mainClass="App"

# Với Gradle
gradle run

# Hoặc chạy file JAR
java -jar target/cinema-management-system.jar
```

## Cấu Trúc Project
```
cinema_management/
├── src/
│   ├── App.java                 # Entry point
│   ├── connect/
│   │   └── DatabaseConnection.java
│   ├── dao/
│   │   ├── CustomerDAO.java
│   │   ├── FilmDAO.java
│   │   ├── SeatDAO.java
│   │   ├── TheaterDAO.java
│   │   ├── ShowtimeDAO.java
│   │   ├── TicketDAO.java
│   │   ├── EmployeeDAO.java
│   │   ├── InvoiceDAO.java
│   │   └── PromotionDAO.java
│   ├── entity/
│   │   ├── Customer.java
│   │   ├── Film.java
│   │   ├── Seat.java
│   │   ├── Theater.java
│   │   ├── Showtime.java
│   │   ├── Ticket.java
│   │   ├── Employee.java
│   │   ├── Invoice.java
│   │   └── Promotion.java
│   ├── services/
│   │   └── AuthService.java
│   └── ui/
│       ├── MainFrameNew.java
│       ├── LoginPanel.java
│       ├── RegisterPanel.java
│       ├── CustomerDashboardPanel.java
│       └── AdminDashboardPanel.java
└── scripts/
    └── create_database.sql
```

## Tài Khoản Mặc Định
Sau khi chạy script SQL, có 2 tài khoản mẫu:
- **Email**: nguyenvana@gmail.com (Khách hàng)
- **Email**: tranthib@gmail.com (Khách hàng)
- **Email**: admin@cinema.com (Nhân viên/Admin)

**Lưu ý**: Hiện tại, bất kỳ email nào đã đăng ký cũng có thể đăng nhập mà không cần mật khẩu. Điều này sẽ được cải thiện.

## Chức Năng Chính

### Cho Khách Hàng
- Duyệt phim và suất chiếu
- Chọn ghế ngồi
- Đặt vé và thanh toán
- Xem lịch sử vé đã mua
- Quản lý tài khoản cá nhân
- Xem khuyến mãi

### Cho Quản Trị Viên
- Quản lý phim (CRUD)
- Quản lý phòng chiếu
- Quản lý ghế
- Quản lý suất chiếu
- Quản lý khách hàng
- Quản lý nhân viên
- Quản lý khuyến mãi
- Thống kê và báo cáo doanh thu

## Mô Tả Database

### Bảng Chính
1. **phim**: Lưu thông tin phim
2. **phong**: Phòng chiếu
3. **ghe**: Ghế ngồi
4. **suat_chieu**: Suất chiếu
5. **khach_hang**: Thông tin khách hàng
6. **ve**: Vé mua
7. **hoa_don**: Hóa đơn
8. **chi_tiet_hoa_don**: Chi tiết hóa đơn
9. **nhan_vien**: Thông tin nhân viên
10. **khuyen_mai**: Khuyến mãi

## API Routes (Backend)
Tất cả operations được thực hiện thông qua DAO classes:
- CustomerDAO: CRUD khách hàng
- FilmDAO: CRUD phim
- SeatDAO: CRUD ghế
- TheaterDAO: CRUD phòng
- ShowtimeDAO: CRUD suất chiếu
- TicketDAO: CRUD vé
- EmployeeDAO: CRUD nhân viên
- InvoiceDAO: CRUD hóa đơn
- PromotionDAO: CRUD khuyến mãi

## Tính Năng Sắp Phát Triển
- Hashing mật khẩu BCrypt
- Tích hợp thanh toán online (Stripe, Momo, ZaloPay)
- In vé PDF
- QR code trên vé
- Email confirmation
- Hệ thống review/rating
- Mobile app

## Troubleshooting

### Lỗi: "Không tìm thấy PostgreSQL JDBC Driver"
- Thêm JDBC driver vào classpath
- Nếu dùng Maven: thêm dependency trong pom.xml
  ```xml
  <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <version>42.5.1</version>
  </dependency>
  ```

### Lỗi: "Kết nối PostgreSQL thất bại"
- Kiểm tra PostgreSQL server đang chạy
- Kiểm tra cấu hình DATABASE, USER, PASSWORD trong DatabaseConnection
- Kiểm tra database `cinema_db` đã được tạo

### Lỗi: "Database không tìm thấy"
- Chạy script SQL: `psql -U postgres -d cinema_db -f scripts/create_database.sql`
- Hoặc tạo database thủ công và import schema

## Hỗ Trợ
Nếu gặp vấn đề, vui lòng kiểm tra:
1. PostgreSQL đang chạy
2. Cấu hình kết nối đúng
3. JDBC driver được cài đặt
4. Database và tables đã được tạo

## License
MIT License - Tự do sử dụng và sửa đổi

---
**Tác giả**: T3L Cinema Team
**Năm**: 2026
**Phiên bản**: 1.0.0
