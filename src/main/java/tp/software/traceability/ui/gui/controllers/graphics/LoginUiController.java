package tp.software.traceability.ui.gui.controllers.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tp.software.traceability.exceptions.UserServiceException;
import tp.software.traceability.ui.gui.controllers.UserController;

import java.net.URL;

@Component
public class LoginUiController {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoginUiController.class);
    private final UserController userController;

    private final Resource createUserUiResource;


    private final String createUserUiTitle;

    private final ApplicationContext applicationContext;


    @FXML
    public TextField txt_email;

    @FXML
    public TextField txt_password;

    @FXML
    public Button btn_login;

    @FXML
    public Button btn_createUser;

    public LoginUiController(UserController userController,
                             @Value("classpath:/create_user.fxml") Resource createUserUiResource,
                             @Value("${spring.application.create_user_ui.title}") String createUserUiTitle, ApplicationContext applicationContext) {
        this.userController = userController;
        this.createUserUiResource = createUserUiResource;
        this.createUserUiTitle = createUserUiTitle;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void handleButtonLogin() {
        String success = "Login successful";
        String fail = "Login failed";
        this.btn_login.setOnAction(event -> {
            String email = this.txt_email.getText();
            String password = this.txt_password.getText();
            try {
                if (this.userController.authenticateUser(email, password)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, success, ButtonType.CANCEL);
                    alert.showAndWait();
                    LOGGER.info("User authenticated");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, fail, ButtonType.CANCEL);
                    alert.showAndWait();
                    LOGGER.error("User not authenticated");
                }
            }catch(UserServiceException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CANCEL);
                alert.showAndWait();
                LOGGER.error("User not authenticated");
            }
        });
    }

    @FXML
    public void handleButtonCreateUser(ActionEvent event) {
            try {
                URL url = this.createUserUiResource.getURL();
                FXMLLoader fxmlLoader = new FXMLLoader(url);
                fxmlLoader.setControllerFactory(this.applicationContext::getBean);
                Parent root = fxmlLoader.load();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle(this.createUserUiTitle);
                stage.setScene(new Scene(root, 600, 400));
                stage.show();
            } catch (Exception e) {
                LOGGER.error("Error loading create user UI", e);
            }
    }
}
