package tp.software.traceability.ui.gui.controllers.graphics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tp.software.traceability.exceptions.UserServiceException;
import tp.software.traceability.ui.gui.controllers.UserController;
import tp.software.traceability.ui.models.requests.UserDetailsRequestModel;
import tp.software.traceability.ui.models.responses.UserResponse;

import java.net.URL;

@Component
public class CreateUserUiController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CreateUserUiController.class);
    private final UserController userController;

    private final String loginUiTitle;

    private final ApplicationContext applicationContext;

    private final Resource loginUiResource;

    @FXML
    public TextField txt_email;

    @FXML
    public TextField txt_password;

    @FXML
    public TextField txt_name;

    @FXML
    public TextField txt_age;


    public CreateUserUiController(UserController userController,
                                  @Value("${spring.application.ui.title}") String loginUiTitle,
                                  ApplicationContext applicationContext,
                                  @Value("classpath:/login_ui.fxml") Resource loginUiResource) {
        this.userController = userController;
        this.loginUiTitle = loginUiTitle;
        this.applicationContext = applicationContext;
        this.loginUiResource = loginUiResource;
    }
    @FXML
    public void handleButtonCreateUser() {
        String email = this.txt_email.getText();
        String password = this.txt_password.getText();
        String name = this.txt_name.getText();
        int age = Integer.parseInt(this.txt_age.getText());
        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || age == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all the fields", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        UserDetailsRequestModel userDetails = UserDetailsRequestModel.builder()
                .email(email)
                .password(password)
                .name(name)
                .age(age)
                .build();
        try {
            UserResponse userResponse = this.userController.createUser(userDetails);
            clearFields();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User "+ userResponse.getName()+ " created", ButtonType.CANCEL);
            alert.showAndWait();
        } catch (UserServiceException userServiceException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, userServiceException.getMessage(), ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    @FXML
    public void handleButtonLogin(ActionEvent event) {
        try {
            URL url = this.loginUiResource.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle(this.loginUiTitle);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            LOGGER.error("Error loading login user UI", e);
        }
    }

    private void clearFields() {
        this.txt_email.setText("");
        this.txt_password.setText("");
        this.txt_name.setText("");
        this.txt_age.setText("");
    }
}
