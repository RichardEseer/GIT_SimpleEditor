package tarleton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard
 */
public class GoToDialogBoxController implements Initializable {
    
    public int position;

    @FXML
    private Button cancelButton;
    @FXML
    private Button gotoButton;
    @FXML
    private TextField numberField;
    @FXML
    public SimpleIntegerProperty lineNumber;

    @FXML
    protected void cancelAction(ActionEvent event) {
        Stage stage = (Stage) numberField.getScene().getWindow();
//        stage.close();
        stage.hide();
    }

    @FXML
    protected void gotoAction(ActionEvent event) {
//        System.out.println("numberField.getText()1: " + numberField.getText());
        lineNumber.set(Integer.parseInt(numberField.getText()));
//        System.out.println("numberField.getText()2: " + numberField.getText());
        position = Integer.parseInt(numberField.getText());  //lineNumber.get();
//        System.out.println("numberField.getText()3: " + numberField.getText());
//        System.out.println("position: " + position);
//        System.out.println("lineNumber: " + lineNumber.get());
        Stage stage = (Stage) numberField.getScene().getWindow();
//        stage.close();
        stage.hide();
    }

    public int displayGoToDialogBox() {
        try {
            URL location = getClass().getResource("GoToDialogBox.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            final Parent root = (Parent) fxmlLoader.load(location.openStream());
//            final MessageDialogBoxController controller = (MessageDialogBoxController)fxmlLoader.getController();
//            controller.toString();

            Stage stage = new Stage();
            stage.setTitle("Go To Line ...");
            stage.setScene(new Scene(root));
            stage.showAndWait();
//            stage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
//        lineNumber.set(Integer.parseInt(numberField.getText()));
//        System.out.println("returning lineNumber: " + lineNumber.get());
//        return lineNumber.get();
//        System.out.println("returning position: " + position);
        return position;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineNumber = new SimpleIntegerProperty();
        lineNumber.set(0);
        System.out.println("GoToDialogBoxController initialize method executed");
    }
}
