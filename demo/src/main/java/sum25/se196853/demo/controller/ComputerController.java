package sum25.se196853.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sum25.se196853.demo.entity.Computer;
import sum25.se196853.demo.entity.Manufacturer;
import sum25.se196853.demo.service.ComputerService;
import sum25.se196853.demo.service.ManufacturerService;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

@Component
public class ComputerController {
    @Autowired
    private ComputerService computerService;
    @Autowired
    private ManufacturerService manufacturerService;

    @FXML private TableView<Computer> computerTableView;
    @FXML private TableColumn<Computer, Integer> colComputerId;
    @FXML private TableColumn<Computer, String> colModel;
    @FXML private TableColumn<Computer, String> colType;
    @FXML private TableColumn<Computer, Integer> colYear;
    @FXML private TableColumn<Computer, BigDecimal> colPrice;
    @FXML private TableColumn<Computer, Manufacturer> colManufacturer; // Kiểu là Manufacturer

    @FXML private TextField txtModel;
    @FXML private TextField txtType;
    @FXML private TextField txtYear;
    @FXML private TextField txtPrice;
    @FXML private ComboBox<Manufacturer> cmbManufacturer;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private final ObservableList<Computer> computerList = FXCollections.observableArrayList();
    private final ObservableList<Manufacturer> manufacturerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // --- Cấu hình TableView Columns ---
        colComputerId.setCellValueFactory(new PropertyValueFactory<>("computerId"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("computerModel"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("productionYear"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colManufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer")); // Trỏ thẳng vào field manufacturer

        // Hiển thị tên Manufacturer trong cột thay vì object toString() mặc định
        colManufacturer.setCellFactory(column -> new TableCell<Computer, Manufacturer>() {
            @Override
            protected void updateItem(Manufacturer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getManufacturerName());
            }
        });

        computerTableView.setItems(computerList);

        // --- Cấu hình ComboBox ---
        loadManufacturers(); // Load danh sách hãng
        cmbManufacturer.setItems(manufacturerList);
        // Hiển thị tên hãng trong ComboBox thay vì object toString()
        cmbManufacturer.setConverter(new javafx.util.StringConverter<Manufacturer>() {
            @Override
            public String toString(Manufacturer manufacturer) {
                return manufacturer == null ? null : manufacturer.getManufacturerName();
            }
            @Override
            public Manufacturer fromString(String string) {
                // Tìm manufacturer theo tên nếu cần (ít dùng cho ComboBox chỉ hiển thị)
                return manufacturerList.stream()
                        .filter(m -> m.getManufacturerName().equalsIgnoreCase(string))
                        .findFirst().orElse(null);
            }
        });

        // --- Load dữ liệu và xử lý sự kiện ---
        loadComputers(); // Load danh sách máy tính ban đầu
        setupTableSelectionListener(); // Xử lý khi chọn dòng trong bảng
        setupPermissions(); // Thiết lập quyền hạn (hiển thị nút)
    }

    private void loadComputers() {
        try {
            computerList.setAll(computerService.getAllComputers()); // Gọi Service để lấy dữ liệu
            computerTableView.refresh();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load computers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadManufacturers() {
        try {
            manufacturerList.setAll(manufacturerService.getAllManufacturers()); // Gọi Service
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load manufacturers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableSelectionListener() {
        computerTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection); // Hiển thị thông tin lên form khi chọn
                    } else {
                        clearForm(); // Xóa form khi bỏ chọn
                    }
                });
    }

    private void setupPermissions() {
        // Chỉ Admin và Staff mới có quyền thêm/sửa/xóa Computer
        boolean canModify = AuthManager.getInstance().isStaffOrAdmin();
        btnAdd.setVisible(canModify);
        btnUpdate.setVisible(canModify);
        btnDelete.setVisible(canModify);
        // Nút Clear luôn hiển thị
    }

    @FXML
    private void handleAddAction() {
        if (validateInput()) {
            Computer newComputer = new Computer();
            try {
                updateComputerFromForm(newComputer); // Lấy dữ liệu từ form vào object mới
                Computer savedComputer = computerService.saveComputer(newComputer); // Gọi Service để lưu
                loadComputers(); // Tải lại danh sách để hiển thị máy mới
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Computer added successfully with ID: " + savedComputer.getComputerId());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to add computer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateAction() {
        Computer selectedComputer = computerTableView.getSelectionModel().getSelectedItem();
        if (selectedComputer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a computer to update.");
            return;
        }
        if (validateInput()) {
            try {
                Computer updatedDetails = new Computer();
                updateComputerFromForm(updatedDetails);

                computerService.updateComputer(selectedComputer.getComputerId(), updatedDetails);

                loadComputers();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Computer updated successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update computer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDeleteAction() {
        Computer selectedComputer = computerTableView.getSelectionModel().getSelectedItem();
        if (selectedComputer == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a computer to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete '" + selectedComputer.getComputerModel() + "'?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                computerService.deleteComputer(selectedComputer.getComputerId()); // Gọi Service xóa
                loadComputers(); // Tải lại danh sách
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Computer deleted successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete computer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClearAction() {
        clearForm();
        computerTableView.getSelectionModel().clearSelection(); // Bỏ chọn dòng trong bảng
    }

    // Helper để điền thông tin Computer lên form
    private void populateForm(Computer computer) {
        txtModel.setText(computer.getComputerModel());
        txtType.setText(computer.getType());
        txtYear.setText(computer.getProductionYear() != null ? String.valueOf(computer.getProductionYear()) : "");
        txtPrice.setText(computer.getPrice() != null ? computer.getPrice().toPlainString() : ""); // Dùng toPlainString cho BigDecimal

        // Chọn đúng Manufacturer trong ComboBox
        cmbManufacturer.setValue(computer.getManufacturer()); // Set giá trị bằng object Manufacturer
    }

    // Helper để xóa trắng form
    private void clearForm() {
        txtModel.clear();
        txtType.clear();
        txtYear.clear();
        txtPrice.clear();
        cmbManufacturer.getSelectionModel().clearSelection(); // Xóa lựa chọn trong ComboBox
        txtModel.requestFocus(); // Focus vào trường đầu tiên
    }

    // Helper để lấy dữ liệu từ form cập nhật vào entity Computer
    private void updateComputerFromForm(Computer computer) {
        computer.setComputerModel(txtModel.getText().trim());
        computer.setType(txtType.getText().trim());
        try {
            computer.setProductionYear(Integer.parseInt(txtYear.getText().trim()));
        } catch (NumberFormatException e) {
            computer.setProductionYear(null); // Hoặc xử lý lỗi khác
        }
        try {
            computer.setPrice(new BigDecimal(txtPrice.getText().trim()));
        } catch (NumberFormatException e) {
            computer.setPrice(null); // Hoặc xử lý lỗi khác
        }
        computer.setManufacturer(cmbManufacturer.getSelectionModel().getSelectedItem()); // Gán đối tượng Manufacturer đã chọn
    }


    // Helper để kiểm tra dữ liệu nhập trên form
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        if (txtModel.getText() == null || txtModel.getText().trim().isEmpty()) {
            errors.append("- Model cannot be empty.\n");
        }
        if (txtType.getText() == null || txtType.getText().trim().isEmpty()) {
            errors.append("- Type cannot be empty.\n");
        }
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            int currentYear = Year.now().getValue();
            if (year < 1990 || year > currentYear) {
                errors.append("- Production year must be between 1990 and ").append(currentYear).append(".\n");
            }
        } catch (NumberFormatException e) {
            errors.append("- Production year must be a valid number.\n");
        }
        try {
            // Cho phép giá trị null hoặc trống, nhưng nếu có thì phải hợp lệ
            String priceStr = txtPrice.getText().trim();
            if (!priceStr.isEmpty()) {
                BigDecimal price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    errors.append("- Price must be non-negative.\n");
                }
            } else {
                errors.append("- Price cannot be empty.\n"); // Bắt buộc nhập giá
            }
        } catch (NumberFormatException | ArithmeticException e) {
            errors.append("- Price must be a valid decimal number.\n");
        }
        if (cmbManufacturer.getSelectionModel().getSelectedItem() == null) {
            errors.append("- Manufacturer must be selected.\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errors.toString());
            return false;
        }
        return true;
    }

    // Helper để hiển thị Alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không hiển thị header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
