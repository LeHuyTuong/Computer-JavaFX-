package sum25.se196853.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import sum25.se196853.demo.HelloApplication;

import java.io.IOException;

@Component
public class MainController {
    @Autowired
    private ConfigurableApplicationContext applicationContext; // Để load lại LoginView và các view con

    @FXML private TabPane mainTabPane;
    @FXML private Tab computersTab;
    @FXML private Tab manufacturersTab;

    @FXML private MenuBar menuBar;
    @FXML private MenuItem logoutMenuItem;


    @FXML
    public void initialize() {
        loadTabContent();
        setupPermissions();
    }

    private void loadTabContent() {
        try {
            // Load Computer View
            FXMLLoader computerLoader = new FXMLLoader(HelloApplication.class.getResource("/sum25/se196853/demo/computer-view.fxml"));
            computerLoader.setControllerFactory(applicationContext::getBean);
            computersTab.setContent(computerLoader.load());

            // Load Manufacturer View
            FXMLLoader manufacturerLoader = new FXMLLoader(HelloApplication.class.getResource("/sum25/se196853/demo/manufacturer-view.fxml"));
            manufacturerLoader.setControllerFactory(applicationContext::getBean);
            manufacturersTab.setContent(manufacturerLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
            // Hiển thị lỗi load tab nếu cần
        }
    }

    private void setupPermissions() {
         //Ví dụ: Ẩn tab Manufacturers nếu không phải Admin
         if (!AuthManager.getInstance().isAdmin()) {
             mainTabPane.getTabs().remove(manufacturersTab);
         }
    }


    @FXML
    private void handleLogoutAction() {
        AuthManager.getInstance().logout(); // Xóa thông tin user

        try {
            // Đóng màn hình chính
            Stage mainStage = (Stage) mainTabPane.getScene().getWindow();
            mainStage.close();

            // Mở lại màn hình login
            Stage loginStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/sum25/se196853/demo/login-view.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean); // Quan trọng
            Parent loginRoot = fxmlLoader.load();
            Scene loginScene = new Scene(loginRoot);

            // Áp dụng lại CSS cho màn hình login
            String css = HelloApplication.class.getResource("/sum25/se196853/demo/css/login.css").toExternalForm();
            loginScene.getStylesheets().add(css);

            loginStage.setScene(loginScene);
            loginStage.setTitle("Laptop Shop - Login");
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu không load lại được màn hình login
        }
    }
}
