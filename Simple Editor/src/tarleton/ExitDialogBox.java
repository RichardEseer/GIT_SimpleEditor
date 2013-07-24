/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tarleton;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Richard
 */
public class ExitDialogBox extends Stage {

    private final Stage primaryStage = this; //new Stage();
    private String applicationName = "Exit Dialog Box";
    // Dialog font settings
    private String currentFontFamily = "Courier";
    private int currentFontSize = 16;
    private String currentFontWeight = "normal";
    private String textFillColor = "black";
    private String textBackgroundColor = "white";

    public enum ReturnValue {

        Save, DontSave, Cancel
    };
    private ReturnValue returnValue = ReturnValue.Cancel;

    public ReturnValue showDialog() {
        this.sizeToScene();
        this.centerOnScreen();
//        System.out.println("Before showandWait");
        this.showAndWait();
//        System.out.println("After showandWait");
        return returnValue;
    }

    public ExitDialogBox(Stage owner, String title, String fileName, boolean displayCancelButton) {
        BorderPane borderPane = new BorderPane();
        Label fontLabel = new Label("\n Do you want to save changes to " + fileName + "?\n   ");
        fontLabel.setStyle(getFontStyle());
        fontLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Setup dialog box buttons
        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);
        buttonHBox.setSpacing(5.0);
        buttonHBox.setStyle("-fx-background-color: #336699;");

        // Setup OK button
        Button saveButton = new Button("_Save");
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(new OkHandler());

        // Setup Dont't Save button
        Button dontSaveButton = new Button("Do_n't Save");
        dontSaveButton.setOnAction(new DontSaveHandler());

        // Setup Cancel button
        Button cancelButton = new Button("_Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(new CancelHandler());

        // Try a ButtonBar !!!!!!!!
        if(displayCancelButton) {
            buttonHBox.getChildren().addAll(saveButton, dontSaveButton, cancelButton);
        } else {
            buttonHBox.getChildren().addAll(saveButton, dontSaveButton);
        }

        borderPane.setCenter(fontLabel);
        borderPane.setBottom(buttonHBox);

        Color sceneColor = Color.BURLYWOOD;
        
        // Compute the height and width needed for the dialog box
        Font font = Font.font(currentFontFamily, FontWeight.NORMAL, (double)currentFontSize);
        FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
        float height = fontMetrics.getLineHeight()*4;   // 4 is used - 3 for each line
            // of the Label and 1 for the buttons.
        float width = fontMetrics.computeStringWidth(fontLabel.getText());
        height += saveButton.getHeight();

        Scene scene = new Scene(borderPane, width, height, sceneColor);

        if(title == null) {
            this.setTitle(applicationName);
        } else {
            this.setTitle(title);
        }
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);  //.WINDOW_MODAL);
        this.initOwner(owner);
        this.setResizable(false);
        this.setScene(scene);
    }

    public ReturnValue getResults() {
        System.out.println("*******************************");
        primaryStage.show();
        System.out.println("*******************************");
        return returnValue;
    }

    private class OkHandler implements EventHandler {

        @Override
        public void handle(Event t) {
            returnValue = ReturnValue.Save;
            primaryStage.close();
        }
    }

    private class DontSaveHandler implements EventHandler {

        @Override
        public void handle(Event t) {
            returnValue = ReturnValue.DontSave;
            primaryStage.close();
        }
    }

    private class CancelHandler implements EventHandler {

        @Override
        public void handle(Event t) {
            returnValue = ReturnValue.Cancel;
            primaryStage.close();
        }
    }

    public String getFontStyle() {
        // Used to get the default font style
        return "-fx-text-fill: " + textFillColor + ";"
                + "-fx-background-color: " + textBackgroundColor + ";"
                //                + "-fx-font: " + currentFontName + ";"
                + "-fx-font-family: " + currentFontFamily + ";"
                + "-fx-font-size: " + currentFontSize + ";"
                + "-fx-font-weight: " + currentFontWeight;
    }
}
