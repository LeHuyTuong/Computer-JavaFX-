package sum25.se196853.demo.service;

import sum25.se196853.demo.entity.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerService {
    List<Manufacturer> getAllManufacturers();
    Optional<Manufacturer> getManufacturerById(Integer id);
    Manufacturer saveManufacturer(Manufacturer manufacturer);
    void deleteManufacturer(Integer id);
}
