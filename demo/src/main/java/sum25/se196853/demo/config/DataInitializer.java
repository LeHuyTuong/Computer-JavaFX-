package sum25.se196853.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sum25.se196853.demo.entity.Computer;
import sum25.se196853.demo.entity.Manufacturer;
import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.repository.ComputerRepository;
import sum25.se196853.demo.repository.ManufacturerRepository;
import sum25.se196853.demo.repository.UserRepository;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ComputerRepository computerRepository;

    @Override
    public void run(String... args) throws Exception {
        // --- Create Users ---
        if (userRepository.findByEmail("admin@laptopshop.com").isEmpty()) {
            User admin = new User("admin@laptopshop.com", "@1", "Admin"); // email, password, role
            userRepository.save(admin);
            System.out.println("Created User: admin@laptopshop.com");
        }
        if (userRepository.findByEmail("staff@laptopshop.com").isEmpty()) {
            User staff = new User("staff@laptopshop.com", "@2", "Staff");
            userRepository.save(staff);
            System.out.println("Created User: staff@laptopshop.com");
        }
        if (userRepository.findByEmail("member@laptopshop.com").isEmpty()) {
            User member = new User("member@laptopshop.com", "@3", "Member");
            userRepository.save(member);
            System.out.println("Created User: member@laptopshop.com");
        }


        // --- Create Manufacturers ---
        Manufacturer dell = manufacturerRepository.findByManufacturerNameIgnoreCase("Dell")
                .orElseGet(() -> manufacturerRepository.save(new Manufacturer("Dell", "USA")));
        Manufacturer lenovo = manufacturerRepository.findByManufacturerNameIgnoreCase("Lenovo")
                .orElseGet(() -> manufacturerRepository.save(new Manufacturer("Lenovo", "China")));
        Manufacturer hp = manufacturerRepository.findByManufacturerNameIgnoreCase("HP")
                .orElseGet(() -> manufacturerRepository.save(new Manufacturer("HP", "USA")));

        // --- Create Computers ---
        if (computerRepository.count() == 0) {
            Computer c1 = new Computer();
            c1.setComputerModel("XPS 13");
            c1.setType("Ultrabook");
            c1.setProductionYear(2023);
            c1.setPrice(new BigDecimal("1299.99"));
            c1.setManufacturer(dell); // Gán đối tượng Manufacturer
            computerRepository.save(c1);

            Computer c2 = new Computer();
            c2.setComputerModel("ThinkPad X1 Carbon");
            c2.setType("Business Laptop");
            c2.setProductionYear(2023);
            c2.setPrice(new BigDecimal("1499.99"));
            c2.setManufacturer(lenovo);
            computerRepository.save(c2);

            Computer c3 = new Computer();
            c3.setComputerModel("Pavilion 15");
            c3.setType("Consumer Laptop");
            c3.setProductionYear(2022);
            c3.setPrice(new BigDecimal("699.99"));
            c3.setManufacturer(hp);
            computerRepository.save(c3);

            Computer c4 = new Computer();
            c4.setComputerModel("Inspiron 14");
            c4.setType("Budget Laptop");
            c4.setProductionYear(2023);
            c4.setPrice(new BigDecimal("549.99"));
            c4.setManufacturer(dell);
            computerRepository.save(c4);

            System.out.println("Initialized sample computers.");
        }

        System.out.println("Data initialization check complete.");
    }

}
