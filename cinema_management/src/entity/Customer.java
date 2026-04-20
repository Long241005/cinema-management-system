package entity;
import java.io.Serializable;
import java.time.LocalDate;
public class Customer implements Serializable{
	 private int customerId;
	    private String fullName;
	    private String email;
	    private String phone;
	    private String membershipLevel; // BRONZE, SILVER, GOLD, PLATINUM
	    private int loyaltyPoints;
	    private LocalDate registrationDate;
	    private String status; // ACTIVE, INACTIVE

	    public Customer() {}

	    public Customer(int customerId, String fullName, String email, String phone,
	                    String membershipLevel, int loyaltyPoints, LocalDate registrationDate, String status) {
	        this.customerId = customerId;
	        this.fullName = fullName;
	        this.email = email;
	        this.phone = phone;
	        this.membershipLevel = membershipLevel;
	        this.loyaltyPoints = loyaltyPoints;
	        this.registrationDate = registrationDate;
	        this.status = status;
	    }

	    // Getters and Setters
	    public int getCustomerId() { return customerId; }
	    public void setCustomerId(int customerId) { this.customerId = customerId; }

	    public String getFullName() { return fullName; }
	    public void setFullName(String fullName) { this.fullName = fullName; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getPhone() { return phone; }
	    public void setPhone(String phone) { this.phone = phone; }

	    public String getMembershipLevel() { return membershipLevel; }
	    public void setMembershipLevel(String membershipLevel) { this.membershipLevel = membershipLevel; }

	    public int getLoyaltyPoints() { return loyaltyPoints; }
	    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

	    public LocalDate getRegistrationDate() { return registrationDate; }
	    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }

	    @Override
	    public String toString() {
	        return fullName + " (" + membershipLevel + ")";
	    }
}
