package sum25.se196853.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @Column(name = "manufacturer_name", nullable = false, unique = true, length = 20)
    private String manufacturerName;

    @Column(length = 50)
    private String country;

    public Manufacturer(String manufacturerName, String country) {
        this.manufacturerName = manufacturerName;
        this.country = country;
    }

    public Manufacturer() {
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
