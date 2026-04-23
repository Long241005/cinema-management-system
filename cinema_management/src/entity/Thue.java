package entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Lớp đại diện cho Thuế
 */
public class Thue implements Serializable {
    private String maThue;
    private String tenThue;
    private BigDecimal phanTramThue;

    public Thue() {}

    public Thue(String maThue, String tenThue, BigDecimal phanTramThue) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.phanTramThue = phanTramThue;
    }

   
    public String getMaThue() { return maThue; }
    public void setMaThue(String maThue) { this.maThue = maThue; }

    public String getTenThue() { return tenThue; }
    public void setTenThue(String tenThue) { this.tenThue = tenThue; }

    public BigDecimal getPhanTramThue() { return phanTramThue; }
    public void setPhanTramThue(BigDecimal phanTramThue) { this.phanTramThue = phanTramThue; }

    @Override
    public String toString() {
        return tenThue + " (" + phanTramThue + "%)";
    }
}
