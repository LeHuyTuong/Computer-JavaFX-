package sum25.se196853.demo.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sum25.se196853.demo.entity.Manufacturer;
import sum25.se196853.demo.service.ManufacturerService;

import java.util.Optional;

@Component
public class ManufacturerController {
    @Autowired
    private ManufacturerService manufacturerService;

    @FXML private TableView<Manufacturer> manufacturerTableView;
    @FXML private TableColumn<Manufacturer, Integer> colId;
    @FXML private TableColumn<Manufacturer, String> colName;
    @FXML private TableColumn<Manufacturer, String> colCountry;

    @FXML private TextField txtName;
    @FXML private TextField txtCountry;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private final ObservableList<Manufacturer> manufacturerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("manufacturerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));

        manufacturerTableView.setItems(manufacturerList);

        loadManufacturers();

        // Xử lý chọn dòng trong TableView
        manufacturerTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection);
                    } else {
                        clearForm();
                    }
                });
        setupPermissions();
    }

    private void loadManufacturers() {
        try {
            manufacturerList.setAll(manufacturerService.getAllManufacturers());
            manufacturerTableView.refresh();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load manufacturers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupPermissions() {
        boolean canModify = AuthManager.getInstance().isAdmin();
        btnAdd.setVisible(canModify);
        btnUpdate.setVisible(canModify);
        btnDelete.setVisible(canModify);
        // Nút Clear luôn hiển thị
    }

    @FXML
    private void handleAddAction() {
        if (validateInput()) {
            Manufacturer newManufacturer = new Manufacturer();
            updateManufacturerFromForm(newManufacturer);
            try {
                Manufacturer saved = manufacturerService.saveManufacturer(newManufacturer);
                loadManufacturers();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Manufacturer added successfully with ID: " + saved.getManufacturerId());
            } catch (IllegalArgumentException e) { // Bắt lỗi tên trùng
                showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
            }
            catch (Exception e) { // Bắt lỗi khác
                showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to add manufacturer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdateAction() {
        Manufacturer selected = manufacturerTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a manufacturer to update.");
            return;
        }
        if (validateInput()) {
            try {
                // Lấy dữ liệu từ form cập nhật vào đối tượng đang chọn
                updateManufacturerFromForm(selected);
                manufacturerService.saveManufacturer(selected); // Lưu lại thay đổi
                // Lưu vị trí đang chọn
                int selectedIndex = manufacturerTableView.getSelectionModel().getSelectedIndex();
                loadManufacturers(); // Tải lại để thấy sự thay đổi
                // Chọn lại dòng vừa sửa
                if (selectedIndex >= 0 && selectedIndex < manufacturerList.size()) {
                    manufacturerTableView.getSelectionModel().select(selectedIndex);
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Manufacturer updated successfully.");
            } catch (IllegalArgumentException e) { // Bắt lỗi tên trùng
                showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
            } catch (Exception e) { // Bắt lỗi khác
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update manufacturer: " + e.getMessage());
                e.printStackTrace();
                loadManufacturers(); // Tải lại dữ liệu gốc nếu có lỗi
            }
        }
    }

    @FXML
    private void handleDeleteAction() {
        Manufacturer selected = manufacturerTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a manufacturer to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '" + selected.getManufacturerName() + "'?\n(This will fail if computers are associated with it)",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                manufacturerService.deleteManufacturer(selected.getManufacturerId());
                loadManufacturers();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Manufacturer deleted successfully.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete manufacturer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClearAction() {
        clearForm();
        manufacturerTableView.getSelectionModel().clearSelection();
    }

    // Helper để cập nhật entity từ form
    private void updateManufacturerFromForm(Manufacturer manufacturer) {
        manufacturer.setManufacturerName(txtName.getText().trim());
        // Cho phép Country null hoặc rỗng
        String country = txtCountry.getText();
        manufacturer.setCountry(country == null ? null : country.trim());
    }

    // Helper để điền thông tin lên form
    private void populateForm(Manufacturer manufacturer) {
        txtName.setText(manufacturer.getManufacturerName());
        txtCountry.setText(manufacturer.getCountry() != null ? manufacturer.getCountry() : "");
    }

    // Helper để xóa trắng form
    private void clearForm() {
        txtName.clear();
        txtCountry.clear();
        txtName.requestFocus();
    }

    // Helper kiểm tra input
    private boolean validateInput() {
        if (txtName.getText() == null || txtName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Manufacturer name cannot be empty.");
            return false;
        }
        // Country có thể trống
        return true;
    }

    // Helper hiển thị Alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
