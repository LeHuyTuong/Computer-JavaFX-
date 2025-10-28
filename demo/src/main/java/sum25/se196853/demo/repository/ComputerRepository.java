package sum25.se196853.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sum25.se196853.demo.entity.Computer;

import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Integer> {
    // Query để lấy Computer cùng với Manufacturer (tránh lỗi N+1)
    @Query("SELECT c FROM Computer c JOIN FETCH c.manufacturer m ORDER BY m.manufacturerName, c.computerModel")
    List<Computer> findAllWithManufacturer();

    // Đếm số lượng computer theo manufacturer ID (để kiểm tra trước khi xóa manufacturer)
    long countByManufacturer_ManufacturerId(Integer manufacturerId);
}
