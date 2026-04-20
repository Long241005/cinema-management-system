# Hướng Dẫn Sử Dụng Hệ Thống Quản Lý Rạp Chiếu Phim T3L

## Mục Lục
1. [Đăng Nhập & Đăng Ký](#đăng-nhập--đăng-ký)
2. [Cho Khách Hàng](#cho-khách-hàng)
3. [Cho Quản Trị Viên](#cho-quản-trị-viên)
4. [Hỏi Đáp](#hỏi-đáp)

---

## Đăng Nhập & Đăng Ký

### Đăng Nhập
1. Khởi động ứng dụng
2. Nhập **Email** của bạn
3. Nhập **Mật khẩu**
4. Click nút **"Đăng Nhập"**
5. Nếu email chưa đăng ký, bạn sẽ nhận được thông báo lỗi

**Tài Khoản Mẫu Để Test:**
- Email: `nguyenvana@gmail.com` (Khách hàng)
- Email: `tranthib@gmail.com` (Khách hàng)
- Email: `admin@cinema.com` (Admin/Nhân viên)

**Lưu ý**: Hiện tại, bất kỳ email nào đã được đăng ký trong hệ thống đều có thể đăng nhập mà không cần kiểm tra mật khẩu. Tính năng này sẽ được cải thiện trong các phiên bản tiếp theo.

### Đăng Ký Tài Khoản Mới
1. Từ màn hình Đăng Nhập, click nút **"Đăng Ký"**
2. Điền các thông tin bắt buộc:
   - **Họ và tên**: Tên đầy đủ của bạn
   - **Email**: Email hợp lệ (chưa được đăng ký)
   - **Số điện thoại**: Số điện thoại 10-11 chữ số
   - **Mật khẩu**: Ít nhất 6 ký tự
   - **Xác nhận mật khẩu**: Phải giống mật khẩu trên
3. Click **"Đăng Ký"**
4. Nếu thành công, bạn sẽ được chuyển về trang Đăng Nhập
5. Sử dụng email và mật khẩu vừa tạo để đăng nhập

---

## Cho Khách Hàng

### Trang Chủ Khách Hàng
Sau khi đăng nhập thành công, bạn sẽ thấy:
- **Header**: Hiển thị tên người dùng và nút "Đăng Xuất"
- **Menu bên trái**: Danh sách các chức năng có sẵn
- **Vùng nội dung chính**: Hiển thị thông tin theo menu được chọn

### 1. Duyệt Phim
**Menu**: Duyệt Phim → Danh sách phim hiện có

**Các thông tin hiển thị:**
- ID phim
- Tên phim
- Đạo diễn
- Thể loại
- Thời lượng (phút)
- Ngôn ngữ
- Đánh giá (1-10)

**Tính năng:**
- **Làm Mới**: Tải lại danh sách phim
- **Chi Tiết**: Xem đầy đủ thông tin của phim (mô tả, đánh giá chi tiết)

### 2. Chọn Lịch Chiếu
**Menu**: Lịch Chiếu → Chọn suất chiếu

**Các bước:**
1. Chọn phim từ dropdown "Chọn Phim"
2. Xem danh sách suất chiếu:
   - Phòng chiếu
   - Giờ bắt đầu
   - Giờ kết thúc
   - Giá vé
   - Trạng thái (ACTIVE/COMPLETED)
3. Chọn một suất chiếu từ bảng
4. Click **"Chọn Suất Chiếu"** để tiếp tục

### 3. Chọn Ghế Ngồi
**Menu**: Đặt Vé → Chọn ghế

**Cách sử dụng:**
1. Thấy sơ đồ phòng chiếu với:
   - **Ghế xanh**: Ghế còn trống (có thể chọn)
   - **Ghế cam**: Ghế bạn đã chọn
   - **Ghế đỏ**: Ghế đã bán (không thể chọn)

2. **Chọn ghế**: Click vào ghế xanh để chọn/bỏ chọn
3. **Xem giá tiền**: 
   - Giá tiền hiển thị ở dưới cùng
   - Tổng tiền = Giá tiền ghế × Số ghế chọn

4. **Tiếp tục**: Click nút để đến bước thanh toán

**Lưu ý về loại ghế:**
- **STANDARD**: Ghế bình thường (80,000 VND)
- **VIP**: Ghế cao cấp (100,000 VND)
- **COUPLE**: Ghế đôi (200,000 VND)

### 4. Thanh Toán
**Menu**: Đặt Vé → Thanh toán

**Thông tin trên màn hình:**
- **Tóm tắt đơn hàng**: 
  - Suất chiếu ID
  - Số ghế được chọn
  - Danh sách ID ghế
  - Chi tiết giá tiền

- **Thông tin thanh toán**:
  - Tổng tiền
  - Khuyến mãi (nếu có)
  - **Hình thức thanh toán**:
    - Tiền Mặt
    - Thẻ Tín Dụng
    - Chuyển Khoản
    - E-Wallet

**Cách thanh toán:**
1. Xem lại thông tin đơn hàng
2. Chọn hình thức thanh toán
3. Click **"Thanh Toán"**
4. Nếu thành công, bạn sẽ nhận:
   - Thông báo xác nhận
   - Lời nhắc check email
   - Hóa đơn và vé được tạo

### 5. Lịch Sử Vé
**Menu**: Lịch Sử Vé

**Hiển thị:**
- Bảng tất cả vé bạn đã mua
- Thông tin: ID vé, Phim, Phòng, Ghế, Giờ chiếu, Giá, Trạng thái, Ngày mua

**Tính năng:**
- **Làm Mới**: Tải lại danh sách
- **Xem Chi Tiết**: Xem đầy đủ thông tin vé (bao gồm mô tả phim)
- **In Vé**: In vé (hiển thị chi tiết ở console)

### 6. Tài Khoản Của Tôi
**Menu**: Tài Khoản Của Tôi

**Thông tin hiển thị:**
- Tên
- Email
- Số điện thoại
- Cấp thành viên (BRONZE, SILVER, GOLD, PLATINUM)
- Điểm trung thành
- Ngày đăng ký
- Trạng thái

**Tính năng:**
- Xem thông tin cá nhân
- (Sẽ bổ sung tính năng chỉnh sửa trong phiên bản sau)

### 7. Khuyến Mãi
**Menu**: Khuyến Mãi

**Xem:**
- Danh sách các khuyến mãi đang hoạt động
- Giảm giá theo % hoặc số tiền cố định
- Ngày bắt đầu - kết thúc
- Số lượng còn lại

---

## Cho Quản Trị Viên

### Trang Chủ Admin
Sau khi Admin đăng nhập, sẽ thấy menu bên trái với các chức năng quản lý:
- Tổng Quan
- Quản Lý Phim
- Quản Lý Phòng
- Quản Lý Ghế
- Quản Lý Suất Chiếu
- Quản Lý Khách Hàng
- Quản Lý Nhân Viên
- Quản Lý Khuyến Mãi
- Thống Kê & Báo Cáo

### 1. Quản Lý Phim (CRUD)
**Menu**: Quản Lý Phim

**Giao diện:**
- **Bên trái**: Form nhập liệu
- **Bên phải**: Bảng danh sách phim

**Các trường dữ liệu:**
- Tên Phim (bắt buộc)
- Đạo Diễn
- Thể Loại (bắt buộc)
- Thời Lượng - phút (bắt buộc)
- Ngôn Ngữ
- Mô Tả
- URL Poster
- Đánh Giá (1-10)

**Tính năng:**
- **Thêm Phim**: 
  1. Điền thông tin vào form
  2. Click "Thêm"
  3. Phim được thêm vào database

- **Sửa Phim**:
  1. Click vào phim trong bảng (để load dữ liệu)
  2. Chỉnh sửa các trường
  3. Click "Sửa"

- **Xóa Phim**:
  1. Chọn phim trong bảng
  2. Click "Xóa"
  3. Xác nhận yêu cầu xóa

### 2. Các Module Quản Lý Khác (Chuẩn Bị)
- **Quản Lý Phòng**: CRUD thông tin phòng chiếu
- **Quản Lý Ghế**: CRUD thông tin ghế (giá, loại)
- **Quản Lý Suất Chiếu**: CRUD lịch chiếu phim
- **Quản Lý Khách Hàng**: CRUD thông tin khách hàng
- **Quản Lý Nhân Viên**: CRUD thông tin nhân viên
- **Quản Lý Khuyến Mãi**: CRUD khuyến mãi, thiết lập điều kiện
- **Thống Kê & Báo Cáo**: Xem doanh thu, số vé bán

---

## Hỏi Đáp

### Q: Làm sao để thay đổi mật khẩu?
**A**: Tính năng này sẽ được bổ sung trong phiên bản tiếp theo. Hiện tại, bạn có thể liên hệ quản trị viên để được hỗ trợ.

### Q: Vé được lưu ở đâu?
**A**: Vé được lưu trong database PostgreSQL. Bạn có thể xem lại lịch sử vé trong menu "Lịch Sử Vé".

### Q: Có thể hủy vé không?
**A**: Tính năng hủy vé sẽ được bổ sung trong phiên bản tiếp theo.

### Q: Làm sao để in vé?
**A**: Từ menu "Lịch Sử Vé", chọn vé và click "In Vé". Chi tiết vé sẽ được in (hiển thị ở console).

### Q: Tích điểm trung thành như nào?
**A**: Mỗi lần bạn mua vé, sẽ tích điểm tương ứng. Hệ thống tích điểm sẽ được nâng cấp để cho phép đổi điểm lấy quà.

### Q: Khuyến mãi áp dụng như thế nào?
**A**: Khuyến mãi sẽ được áp dụng tự động khi bạn mua vé (nếu điều kiện khớp). Bạn sẽ thấy số tiền giảm ở bước thanh toán.

### Q: Hỗ trợ tiếng nào?
**A**: Hiện tại ứng dụng hỗ trợ 100% **Tiếng Việt**. Các phiên bản tiếp theo sẽ thêm hỗ trợ tiếng khác.

### Q: Database là gì?
**A**: Database là **PostgreSQL** - một hệ quản trị cơ sở dữ liệu mã nguồn mở, chạy trên cổng 5432 của máy tính bạn.

### Q: Cần cài đặt gì thêm không?
**A**: Cần cài đặt:
- JDK 11+
- PostgreSQL 12+
- Có thể sử dụng Maven/Gradle (optional)

### Q: Ứng dụng chạy bao lâu?
**A**: Ứng dụng là desktop app, chạy trên máy tính cá nhân. Tốc độ phụ thuộc vào cấu hình máy.

---

## Liên Hệ Hỗ Trợ

Nếu gặp bất kỳ vấn đề nào:
1. Kiểm tra lại hướng dẫn trên
2. Xem các tài liệu kỹ thuật (README.md, IMPLEMENTATION_SUMMARY.md)
3. Liên hệ với nhóm phát triển

---

**Version**: 1.0.0  
**Last Updated**: April 2026  
**Language**: Tiếng Việt 100%
