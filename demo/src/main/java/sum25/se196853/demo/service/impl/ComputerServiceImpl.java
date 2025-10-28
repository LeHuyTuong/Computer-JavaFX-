package sum25.se196853.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sum25.se196853.demo.entity.Computer;
import sum25.se196853.demo.repository.ComputerRepository;
import sum25.se196853.demo.repository.ManufacturerRepository;
import sum25.se196853.demo.service.ComputerService;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class ComputerServiceImpl implements ComputerService {
    @Autowired
    private ComputerRepository computerRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public List<Computer> getAllComputers() {
        return computerRepository.findAllWithManufacturer();
    }

    @Override
    public Optional<Computer> findByComputerId(Integer computerId) {
        return Optional.empty();
    }

    public Computer saveComputer(Computer computer) {
        validateComputer(computer);
        if (computer.getManufacturer() == null || computer.getManufacturer().getManufacturerId() == null ||
                !manufacturerRepository.existsById(computer.getManufacturer().getManufacturerId())) {
            throw new IllegalArgumentException("Invalid or non-existent Manufacturer selected.");
        }
        // Trim dữ liệu text
        computer.setComputerModel(computer.getComputerModel().trim());
        computer.setType(computer.getType().trim());

        return computerRepository.save(computer);
    }

    public Computer updateComputer(Integer id, Computer computerDetails) {
        Computer existingComputer = computerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Computer not found with id: " + id));

        validateComputer(computerDetails); // Validate dữ liệu mới
        if (computerDetails.getManufacturer() == null || computerDetails.getManufacturer().getManufacturerId() == null ||
                !manufacturerRepository.existsById(computerDetails.getManufacturer().getManufacturerId())) {
            throw new IllegalArgumentException("Invalid or non-existent selected Manufacturer for update.");
        }

        existingComputer.setComputerModel(computerDetails.getComputerModel().trim());
        existingComputer.setType(computerDetails.getType().trim());
        existingComputer.setProductionYear(computerDetails.getProductionYear());
        existingComputer.setPrice(computerDetails.getPrice());

        existingComputer.setManufacturer(computerDetails.getManufacturer());

        return computerRepository.save(existingComputer);
    }


    public void deleteComputer(Integer id) {
        if (!computerRepository.existsById(id)) {
            throw new RuntimeException("Computer not found with id: " + id);
        }
        computerRepository.deleteById(id);
    }

    // --- Helper Validation Method ---
    private void validateComputer(Computer computer) {
        if (computer.getComputerModel() == null || computer.getComputerModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Computer model cannot be empty.");
        }
        if (computer.getType() == null || computer.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Computer type cannot be empty.");
        }
        if (computer.getProductionYear() != null) {
            int currentYear = Year.now().getValue();
            if (computer.getProductionYear() < 1990 || computer.getProductionYear() > currentYear) {
                throw new IllegalArgumentException("Production year must be between 1990 and " + currentYear + ".");
            }
        } else {
            throw new IllegalArgumentException("Production year cannot be null.");
        }
        if (computer.getPrice() == null || computer.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }
        if (computer.getManufacturer() == null) { // Kiểm tra manufacturer object
            throw new IllegalArgumentException("Manufacturer must be selected.");
        }
    }
}

