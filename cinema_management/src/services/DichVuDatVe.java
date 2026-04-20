package services;

import dao.*;
import entity.*;
import java.time.LocalDate;
import java.util.List;

public class BookingService {
    
    private SeatDAO seatDAO;
    private TicketDAO ticketDAO;
    private InvoiceDAO invoiceDAO;
    private ShowtimeDAO showtimeDAO;
    
    public BookingService() {
        this.seatDAO = new SeatDAO();
        this.ticketDAO = new TicketDAO();
        this.invoiceDAO = new InvoiceDAO();
        this.showtimeDAO = new ShowtimeDAO();
    }
    
    /**
     * Đặt vé cho khách hàng
     */
    public boolean bookTickets(int customerId, int showtimeId, List<Integer> seatIds, double totalPrice, String paymentMethod) {
        try {
            // Kiểm tra ghế còn trống
            for (Integer seatId : seatIds) {
                Seat seat = seatDAO.getSeatById(seatId);
                if (seat == null || !seat.getStatus().equals("AVAILABLE")) {
                    System.err.println("Ghế ID " + seatId + " không còn trống");
                    return false;
                }
            }
            
            // Tạo hóa đơn
            Invoice invoice = new Invoice();
            invoice.setCustomerId(customerId);
            invoice.setTotalAmount(totalPrice);
            invoice.setDiscount(0);
            invoice.setFinalAmount(totalPrice);
            invoice.setPaymentMethod(paymentMethod);
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setStatus("PAID");
            
            int invoiceId = invoiceDAO.addInvoice(invoice);
            if (invoiceId == -1) {
                System.err.println("Lỗi tạo hóa đơn");
                return false;
            }
            
            // Tạo vé và cập nhật ghế
            for (Integer seatId : seatIds) {
                Seat seat = seatDAO.getSeatById(seatId);
                Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
                
                // Tạo vé
                Ticket ticket = new Ticket();
                ticket.setCustomerId(customerId);
                ticket.setShowtimeId(showtimeId);
                ticket.setSeatId(seatId);
                ticket.setPrice(seat.getPrice());
                ticket.setStatus("PAID");
                ticket.setBookingDate(LocalDate.now());
                
                if (!ticketDAO.addTicket(ticket)) {
                    System.err.println("Lỗi tạo vé");
                    return false;
                }
                
                // Cập nhật trạng thái ghế
                if (!seatDAO.updateSeatStatus(seatId, "BOOKED")) {
                    System.err.println("Lỗi cập nhật ghế");
                    return false;
                }
            }
            
            System.out.println("✓ Đặt vé thành công. Hóa đơn ID: " + invoiceId);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Hủy đặt vé
     */
    public boolean cancelBooking(int ticketId) {
        try {
            Ticket ticket = ticketDAO.getTicketById(ticketId);
            if (ticket == null) {
                return false;
            }
            
            // Cập nhật ghế thành sẵn sàng
            if (!seatDAO.updateSeatStatus(ticket.getSeatId(), "AVAILABLE")) {
                return false;
            }
            
            // Xóa vé
            if (!ticketDAO.deleteTicket(ticketId)) {
                return false;
            }
            
            System.out.println("✓ Hủy vé thành công");
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra ghế còn trống
     */
    public boolean isSeatAvailable(int seatId) {
        Seat seat = seatDAO.getSeatById(seatId);
        return seat != null && seat.getStatus().equals("AVAILABLE");
    }
    
    /**
     * Lấy số ghế còn trống cho suất chiếu
     */
    public int getAvailableSeatsCount(int theaterId) {
        List<Seat> availableSeats = seatDAO.getSeatsByStatus(theaterId, "AVAILABLE");
        return availableSeats.size();
    }
    
    /**
     * Lấy số vé đã bán
     */
    public int getTicketsSoldCount(int showtimeId) {
        return ticketDAO.getTicketCountByShowtime(showtimeId);
    }
    
    /**
     * Áp dụng khuyến mãi
     */
    public double applyPromotion(double originalPrice, int promotionId, PromotionDAO promotionDAO) {
        Promotion promotion = promotionDAO.getPromotionById(promotionId);
        if (promotion != null && promotion.getStatus().equals("ACTIVE")) {
            double discount = originalPrice * (promotion.getDiscountPercent() / 100.0);
            promotionDAO.incrementUsedQuantity(promotionId);
            return originalPrice - discount;
        }
        return originalPrice;
    }
    
    /**
     * Lấy giá vé của suất chiếu
     */
    public double getShowtimeTicketPrice(int showtimeId) {
        Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
        if (showtime != null) {
            return showtime.getTicketPrice();
        }
        return 0;
    }
}
