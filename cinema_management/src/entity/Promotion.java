package entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Lớp đại diện cho Khuyến Mãi (Promotion)
 */
public class Promotion implements Serializable {
    private int promotionId;
    private String promotionCode;
    private String description;
    private double discountPercentage;
    private double discountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxUsage;
    private int currentUsage;
    private String status; // ACTIVE, EXPIRED, INACTIVE

    public Promotion() {}

    public Promotion(int promotionId, String promotionCode, String description,
                     double discountPercentage, double discountAmount,
                     LocalDate startDate, LocalDate endDate, int maxUsage, 
                     int currentUsage, String status) {
        this.promotionId = promotionId;
        this.promotionCode = promotionCode;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxUsage = maxUsage;
        this.currentUsage = currentUsage;
        this.status = status;
    }

    // Getters and Setters
    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    public String getPromotionCode() { return promotionCode; }
    public void setPromotionCode(String promotionCode) { this.promotionCode = promotionCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getMaxUsage() { return maxUsage; }
    public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }

    public int getCurrentUsage() { return currentUsage; }
    public void setCurrentUsage(int currentUsage) { this.currentUsage = currentUsage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return promotionCode + " - " + description;
    }
}