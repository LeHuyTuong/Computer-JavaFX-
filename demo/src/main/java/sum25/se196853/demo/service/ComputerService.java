package sum25.se196853.demo.service;

import sum25.se196853.demo.entity.Computer;

import java.util.List;
import java.util.Optional;

public interface ComputerService {
    List<Computer> getAllComputers();
    Optional<Computer> findByComputerId(Integer computerId);

    Computer saveComputer(Computer computer);

    Computer updateComputer(Integer id, Computer computerDetails);

    void deleteComputer(Integer id);


}
