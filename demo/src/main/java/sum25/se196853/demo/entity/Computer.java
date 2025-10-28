package sum25.se196853.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "computers")
public class Computer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "computer_id")
    private Integer computerId; // Sử dụng Integer thay vì int để có thể null

    @Column(name = "computer_model", nullable = false, length = 100)
    private String computerModel;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "production_year")
    private Integer productionYear; // Kiểm tra validation ở tầng Service/Controller

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Kiểm tra >= 0 ở Service/Controller

    // Quan hệ ManyToOne với Manufacturer
    @ManyToOne(fetch = FetchType.LAZY) // LAZY để tránh tải không cần thiết
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    public Computer() {
    }

    public Computer(Integer computerId, String computerModel, String type, Integer productionYear, BigDecimal price, Manufacturer manufacturer) {
        this.computerId = computerId;
        this.computerModel = computerModel;
        this.type = type;
        this.productionYear = productionYear;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public Integer getComputerId() {
        return computerId;
    }

    public void setComputerId(Integer computerId) {
        this.computerId = computerId;
    }

    public String getComputerModel() {
        return computerModel;
    }

    public void setComputerModel(String computerModel) {
        this.computerModel = computerModel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
