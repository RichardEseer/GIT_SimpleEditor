/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tarleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard
 */
public class FXMLBasedFindDialogBoxController implements Initializable {

    @FXML
    private TextField findTextField;
    @FXML
    private GridPane window;
    @FXML
    private RadioButton upDirectionRadioButton;
    @FXML
    private RadioButton downDirectionRadioButton;
    @FXML
    private CheckBox caseCheckBox;
    @FXML
    private ToggleGroup toggleGroup;
    //
    private TextInputControl textControl;
    
    private String targetText;
    private boolean searchWrap = false;       // Controls whether search continues
    private boolean ignoreCase = false;
    private int currentCursorPosition;

    public IntegerProperty searchStringLength;

    public void setTargetString(TextInputControl textControl) {
        this.textControl = textControl;
        targetText = textControl.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize executed");
        searchStringLength = new SimpleIntegerProperty();
        searchStringLength.set(0);
        upDirectionRadioButton.setToggleGroup(toggleGroup);
        downDirectionRadioButton.setToggleGroup(toggleGroup);
        findTextField.requestFocus();
    }

    @FXML
    protected void findNextAction(ActionEvent event) {
        StringIndexer si;
        int ssLength = findTextField.getText().length();
        targetText = textControl.getText();
        currentCursorPosition = textControl.getCaretPosition();
        ignoreCase = !caseCheckBox.isSelected();

        if (upDirectionRadioButton.isSelected()) {
            // Search toward the beginning of the string
            // A new StirngIndexer is required because any number of parameters may have changed
            si = new StringIndexer(targetText,
                    findTextField.getText(), currentCursorPosition - ssLength,
                    searchWrap, ignoreCase);

            int sposition = si.previousStringIndex();
            if (sposition > -1) {
                textControl.selectRange(sposition + ssLength, sposition);
            } else {
                displayMessageDialogBox();
            }
        } else {
            // Search toward the end of the string
            // A new StirngIndexer is required because any number of parameters may have changed
            si = new StringIndexer(targetText,
                    findTextField.getText(), currentCursorPosition,
                    searchWrap, ignoreCase);
            int sposition = si.nextStringIndex();
            if (sposition > -1) {
                textControl.selectRange(sposition, sposition + ssLength);
            } else {
                displayMessageDialogBox();
            }
        }
    }

    private void displayMessageDialogBox() {
        try {
            URL location = getClass().getResource("MessageDialogBox.fxml");

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            final Parent root = (Parent) fxmlLoader.load(location.openStream());
            final MessageDialogBoxController controller = fxmlLoader.getController();

            //                    System.out.println("classpath is: " + System.getProperty("java.class.path"));
//                    InputStream url = getClass().getResourceAsStream("Simple Editor/resources/Information.PNG");
//                    System.out.println("url: " + url);
//                    url = getClass().getResourceAsStream("/resources/Information.PNG");
//                    System.out.println("url: " + url);
//                     url = getClass().getResourceAsStream("resources/Information.PNG");
//                    System.out.println("url: " + url);
//                     url = getClass().getResourceAsStream("/Information.PNG");
//                    System.out.println("url: " + url);
//                     url = getClass().getResourceAsStream("Information.PNG");
//                    System.out.println("url: " + url);
//                    Image img = new Image(url);
//                    Image img = new Image("http://docs.oracle.com/javafx/"
//        + "javafx/images/javafx-documentation.png");
            Image img = new Image("file:\\C:\\Tarleton\\CS330\\Simple Editor\\src\\resources\\Information.PNG");
//                    controller.setImage(img);
            controller.setMessage("Cannot find \"" + findTextField.getText() + "\"");

            Stage stage = new Stage();
            stage.setTitle("Simple Editor");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void cancelAction(ActionEvent event) {
        Stage stage = (Stage) findTextField.getScene().getWindow();
        stage.close();
    }

    
}
