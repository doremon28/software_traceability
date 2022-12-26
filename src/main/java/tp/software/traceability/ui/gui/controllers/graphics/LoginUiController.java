package tp.software.traceability.ui.gui.controllers.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tp.software.traceability.exceptions.SwitchScreenException;
import tp.software.traceability.exceptions.UserServiceException;
import tp.software.traceability.ui.gui.controllers.UserController;
import tp.software.traceability.ui.gui.controllers.graphics.utils.SwitchScreen;

@Component
public class LoginUiController {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoginUiController.class);
    private final UserController userController;

    private final Resource createUserUiResource;

    private final Resource productUiResource;

    private final String productUiTitle;
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
                             @Value("classpath:/product_ui.fxml") Resource productUiResource,
                             @Value("${spring.application.ui.product.title}") String productUiTitle,
                             @Value("${spring.application.create_user_ui.title}") String createUserUiTitle,
                             ApplicationContext applicationContext) {
        this.userController = userController;
        this.createUserUiResource = createUserUiResource;
        this.productUiResource = productUiResource;
        this.productUiTitle = productUiTitle;
        this.createUserUiTitle = createUserUiTitle;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void handleButtonLogin(ActionEvent event) {
        String success = "Login successful";
        String fail = "Login failed";
        String email = this.txt_email.getText();
        String password = this.txt_password.getText();
        try {
            if (this.userController.authenticateUser(email, password)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, success, ButtonType.CANCEL);
                alert.showAndWait();
                switchScreen(event, this.applicationContext,this.productUiResource, this.productUiTitle);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, fail, ButtonType.CANCEL);
                alert.showAndWait();
            }
        } catch (UserServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CANCEL);
            alert.showAndWait();
        }

    }

    private void switchScreen(ActionEvent event, ApplicationContext applicationContext, Resource resource, String title) {
        try {
            SwitchScreen switchScreen = SwitchScreen.builder()
                    .applicationContext(applicationContext)
                    .resource(resource)
                    .title(title)
                    .build();
            switchScreen.switchScreen(event);
        } catch (SwitchScreenException e) {
            LOGGER.error("Error while switching to product ui", e);
        }
    }

    @FXML
    public void handleButtonCreateUser(ActionEvent event) {
        switchScreen(event, this.applicationContext,this.createUserUiResource, this.createUserUiTitle);
    }
}
