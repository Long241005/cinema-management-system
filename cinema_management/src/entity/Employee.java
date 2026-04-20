package entity;
import java.io.Serializable;
import java.time.LocalDate;
public class Employee implements Serializable{
	 private int employeeId;
	    private String fullName;
	    private String email;
	    private String phone;
	    private String position; // MANAGER, CASHIER, STAFF, CLEANER
	    private double salary;
	    private LocalDate hireDate;
	    private String status; // ACTIVE, INACTIVE, ON_LEAVE

	    public Employee() {}

	    public Employee(int employeeId, String fullName, String email, String phone,
	                    String position, double salary, LocalDate hireDate, String status) {
	        this.employeeId = employeeId;
	        this.fullName = fullName;
	        this.email = email;
	        this.phone = phone;
	        this.position = position;
	        this.salary = salary;
	        this.hireDate = hireDate;
	        this.status = status;
	    }

	    // Getters and Setters
	    public int getEmployeeId() { return employeeId; }
	    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

	    public String getFullName() { return fullName; }
	    public void setFullName(String fullName) { this.fullName = fullName; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getPhone() { return phone; }
	    public void setPhone(String phone) { this.phone = phone; }

	    public String getPosition() { return position; }
	    public void setPosition(String position) { this.position = position; }

	    public double getSalary() { return salary; }
	    public void setSalary(double salary) { this.salary = salary; }

	    public LocalDate getHireDate() { return hireDate; }
	    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }

	    @Override
	    public String toString() {
	        return fullName + " (" + position + ")";
	    }
}
