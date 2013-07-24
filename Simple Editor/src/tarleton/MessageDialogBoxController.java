package tarleton;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Richard
 */
public class MessageDialogBoxController implements Initializable {

    @FXML Button OKButton;
    @FXML Label messageLabel;
    @FXML ImageView imageView;
    
    @FXML
    private void okButtonHandler() {
        Stage stage = (Stage) OKButton.getScene().getWindow();
        stage.close();
    }
    
    public void setImage(Image image) {
        imageView.setImage(image);
    }
    
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
