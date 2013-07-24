package tarleton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard
 */
public class ReplaceDialogBoxController implements Initializable {

    @FXML
    Button findNextButton;
    @FXML
    Button replaceButton;
    @FXML
    Button replaceAllButton;
    @FXML
    Button cancelButton;
    @FXML
    TextField findWhatTextField;
    @FXML
    TextField replaceWithTextField;
    @FXML
    CheckBox matchCaseCheckBox;
    
    private TextInputControl textControl;
    
    public IntegerProperty searchStringLength;
    
    private boolean searchWrap = false;       // Controls whether search continues
    private boolean ignoreCase = false;
    private boolean noMoreMatches = false;
    private int currentCursorPosition;
    private String targetText;

    public void setTargetString(TextInputControl textControl) {
        this.textControl = textControl;
        targetText = textControl.getText();
    }

    @FXML
    protected void findNextAction(ActionEvent event) {
        findNext();
    }

    private void findNext() {
        StringIndexer si;
        int ssLength = findWhatTextField.getText().length();
        targetText = textControl.getText();

        currentCursorPosition = textControl.getCaretPosition();
        ignoreCase = !matchCaseCheckBox.isSelected();

        // Search toward the end of the string
        // A new StirngIndexer is required because any number of parameters may have changed
        si = new StringIndexer(targetText,
                findWhatTextField.getText(), currentCursorPosition,
                searchWrap, ignoreCase);
        int sposition = si.nextStringIndex();
        if (sposition > -1) {
            textControl.selectRange(sposition, sposition + ssLength);
        } else {
            displayMessageDialogBox();
            noMoreMatches = true;
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
            controller.setMessage("Cannot find \"" + findWhatTextField.getText() + "\"");

            Stage stage = new Stage();
            stage.setTitle("Simple Editor");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    protected void replaceAction(ActionEvent event) {
        if (!textControl.getSelectedText().equals(findWhatTextField.getText())) {
            findNextAction(event);
        }
        textControl.replaceSelection(replaceWithTextField.getText());
        findNextAction(event);
    }

    @FXML
    protected void replaceAllAction(ActionEvent event) {
        while (!noMoreMatches) {
            replaceAction(event);
        }
        noMoreMatches = false;
    }

    @FXML
    protected void cancelAction(ActionEvent event) {
        Stage stage = (Stage) findWhatTextField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize executed");
        searchStringLength = new SimpleIntegerProperty();
        searchStringLength.set(0);
    }
}
