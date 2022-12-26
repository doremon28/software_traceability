package tp.software.traceability.ui.gui.controllers.graphics.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Builder;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import tp.software.traceability.exceptions.SwitchScreenException;

import java.net.URL;

@Builder
public class SwitchScreen {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SwitchScreen.class);
    private final Resource resource;
    private final String title;
    private final String uiSource;
    private final ApplicationContext applicationContext;


    public void switchScreen(ActionEvent event) {
        try {
            URL url = resource.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            LOGGER.info("Switched to {} screen", this.title);
        } catch (Exception e) {
            throw new SwitchScreenException("Error while switching to " + uiSource, e);
        }
    }


}
