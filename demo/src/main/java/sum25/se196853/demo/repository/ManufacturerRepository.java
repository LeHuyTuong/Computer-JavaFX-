package sum25.se196853.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sum25.se196853.demo.entity.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    Optional<Manufacturer> findByManufacturerNameIgnoreCase(String name);
    List<Manufacturer> findAllByOrderByManufacturerNameAsc();
}
