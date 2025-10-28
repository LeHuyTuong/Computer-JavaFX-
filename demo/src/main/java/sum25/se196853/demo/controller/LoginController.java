package sum25.se196853.demo.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.HelloApplication;
import sum25.se196853.demo.entity.User;
import sum25.se196853.demo.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Component
public class LoginController {
    @FXML
    private TextField txtUsername; // Đổi tên fx:id thành txtUsername cho khớp FXML mới
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;
    @FXML private Button btnHello; // Đổi tên fx:id cho nút login
    @FXML private Button btnBye;   // Đổi tên fx:id cho nút exit

    @Autowired
    private UserService userService; // Spring sẽ inject UserService

    @Autowired
    private ConfigurableApplicationContext applicationContext; // Để load màn hình Main

    @FXML
    public void initialize() {
        // Đặt lại label trạng thái khi khởi tạo
        if (lblStatus != null) lblStatus.setText("Please enter your credentials.");
        // Đặt style class để CSS có thể áp dụng
        if(lblStatus != null) lblStatus.getStyleClass().add("status");
    }

    @FXML
    private void onHelloButtonClick() { // Đổi tên phương thức khớp onAction
        String email = txtUsername.getText() == null ? "" : txtUsername.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            setStatus("Email and password cannot be empty.", true);
            return;
        }

        // Sử dụng UserService để kiểm tra login (trả về Optional<User>)
        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            AuthManager.getInstance().login(userOpt.get()); // Lưu user đã đăng nhập
            setStatus("Login Successfully! Welcome " + userOpt.get().getEmail(), false);
            // Chuyển sang màn hình chính
            loadMainScreen();
        } else {
            setStatus("Login Failed. Invalid email or password.", true);
            txtPassword.clear(); // Xóa password field khi sai
        }
    }

    private void loadMainScreen() {
        try {
            // Lấy Stage (cửa sổ) hiện tại của màn hình login
            Stage loginStage = (Stage) btnHello.getScene().getWindow();

            // Tạo FXML Loader cho màn hình chính
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/sum25/se196853/demo/main-view.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean); // Dùng Spring context factory

            Parent mainRoot = fxmlLoader.load();
            Scene mainScene = new Scene(mainRoot);

            loginStage.setScene(mainScene); // Đặt scene mới cho cửa sổ hiện tại
            loginStage.setTitle("Laptop Shop Management"); // Đổi tiêu đề cửa sổ
            loginStage.centerOnScreen(); // Canh giữa màn hình

        } catch (IOException e) {
            e.printStackTrace();
            setStatus("Error loading main screen.", true);
        }
    }

    @FXML
    public void onByeByeClick() { // Đổi tên phương thức khớp onAction
        Platform.exit(); // Tắt ứng dụng JavaFX (sẽ trigger stop() trong JavaFxMainApplication)
    }

    // Helper để cập nhật label trạng thái và style
    private void setStatus(String message, boolean isError) {
        if (lblStatus != null) {
            lblStatus.setText(message);
            // Xóa style cũ, thêm style mới
            lblStatus.getStyleClass().removeAll("status-success", "status-error");
            if (isError) {
                lblStatus.getStyleClass().add("status-error");
            } else {
                lblStatus.getStyleClass().add("status-success");
            }
        }
    }
}
