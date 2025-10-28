package sum25.se196853.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se196853.demo.entity.Manufacturer;
import sum25.se196853.demo.repository.ComputerRepository;
import sum25.se196853.demo.repository.ManufacturerRepository;
import sum25.se196853.demo.service.ManufacturerService;

import java.util.List;
import java.util.Optional;


@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ComputerRepository computerRepository; // Để kiểm tra trước khi xóa

    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAllByOrderByManufacturerNameAsc();
    }

    public Optional<Manufacturer> getManufacturerById(Integer id) {
        return manufacturerRepository.findById(id);
    }

    public Manufacturer saveManufacturer(Manufacturer manufacturer) {
        if (manufacturer.getManufacturerName() == null || manufacturer.getManufacturerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Manufacturer name cannot be empty.");
        }
        Optional<Manufacturer> existing = manufacturerRepository.findByManufacturerNameIgnoreCase(manufacturer.getManufacturerName().trim());
        if (existing.isPresent() && !existing.get().getManufacturerId().equals(manufacturer.getManufacturerId())) {
            throw new IllegalArgumentException("Manufacturer name '" + manufacturer.getManufacturerName() + "' already exists.");
        }
        // Trim dữ liệu trước khi lưu
        manufacturer.setManufacturerName(manufacturer.getManufacturerName().trim());
        if (manufacturer.getCountry() != null) {
            manufacturer.setCountry(manufacturer.getCountry().trim());
        }
        return manufacturerRepository.save(manufacturer);
    }

    public void deleteManufacturer(Integer id) {
        if (!manufacturerRepository.existsById(id)) {
            throw new RuntimeException("Manufacturer not found with id: " + id);
        }
        // Kiểm tra xem có computer nào liên kết không
        if (computerRepository.countByManufacturer_ManufacturerId(id) > 0) {
            throw new RuntimeException("Cannot delete manufacturer with ID: " + id + " because it is associated with existing computers.");
        }
        manufacturerRepository.deleteById(id);
    }
}
