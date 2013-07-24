package tarleton;

import java.util.StringTokenizer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextInputControl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


// Version 1.0  7/10/2013
// Richard Reese

public class FontDialogBox {

    
    private ObservableList<Label> familyList;
    private ObservableList<Label> fontStyleList;
    private ObservableList<String> fontSizeList =
            FXCollections.observableArrayList("8", "9", "10", "11",
            "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72");
    ListView fontStyleListView = new ListView();
    private String sampleText = "\nAaBbYyZz\n";
    private TextArea sampleTextArea;
    private String currentFontFamily = "Courier";
    private String currentFontStyle = "normal";
    private int currentFontSize = 12;
    private String currentFontWeight = "normal";
    private String textFillColor = "black";
    private String textBackgroundColor = "white";
    private final Stage primaryStage = new Stage();

    // Enhancements needed
    //      Restore previous font settings
    //      Fix sample display - it changes size as the font size changes
    //      Allow the user to enter a font name/style/size and then find a match
    
    // Add the following for support of Apply button
    private StringProperty fontFamily = new SimpleStringProperty();
    public final StringProperty fontFamilyProperty() {
        return fontFamily;
    }

    public final StringProperty getFontFamily() {
        return fontFamily;
    }

    public final void setFontFamily(StringProperty fontFamily) {
        this.fontFamily = fontFamily;
    }

    public FontDialogBox() {
        // initialize default font information
        familyList = FXCollections.observableArrayList();
        for (String family : Font.getFamilies()) {
            Label labelField = new Label(family);
            labelField.setStyle("-fx-font-family: " + family);
            familyList.add(labelField);
        }
        currentFontFamily = familyList.get(0).getText();
        setFontStyleList(fontStyleListView);

        currentFontSize = Integer.parseInt(fontSizeList.get(0));
    }

    private void setFontStyleList(ListView fontStyleComboBox) {
        fontStyleList = FXCollections.observableArrayList();
        // For the currently selected font get all of the style and weight information
        System.out.println("---------  fontFamily: [" + currentFontFamily + "]");
        for (String fontName : Font.getFontNames(currentFontFamily)) {
            // Use a string tokenizer to parse the font style
            System.out.println("---------  Original fontName: [" + fontName + "]");
            String displayString = fontName.substring(currentFontFamily.length(), fontName.length()).trim();

            StringTokenizer st = new StringTokenizer(
                    fontName.substring(currentFontFamily.length(), fontName.length()).trim());

            currentFontStyle = " normal";
            currentFontWeight = "normal";
            while (st.hasMoreTokens()) {
                String shortStyleName;
                if (st.hasMoreTokens()) {
                    shortStyleName = st.nextToken();
                    if (shortStyleName.equalsIgnoreCase("normal")) {
//                        currentFontStyle = " normal";
//                        currentFontWeight = "normal";
                    } else if (shortStyleName.equalsIgnoreCase("bold")) {
//                        currentFontStyle = "normal";
                        currentFontWeight = "bold";
                    } else if (shortStyleName.equalsIgnoreCase("italic")) {
                        currentFontStyle = "italic";
                    } else if (shortStyleName.equalsIgnoreCase("oblique")) {
                        currentFontStyle = "oblique";
                    }
                }
            }
            Label tmpLabel = new Label();
            if (!"".equals(displayString)) {
                tmpLabel.setText(displayString);
            } else {
                tmpLabel.setText("Normal");
            }
            // Store the font size temporarily
            // This insures that the font in dosplay in the cmbobox all with
            // the same size
            int tmpSize = currentFontSize;
            currentFontSize = 12;
                        System.out.println("------------tmpLabel-------" + getFontStyle());
            tmpLabel.setStyle(getFontStyle());
            currentFontSize = tmpSize;
            fontStyleList.add(tmpLabel);
        }
        fontStyleComboBox.setItems(fontStyleList);
    }

    public Stage getFontDialogBox(Stage owner, final TextInputControl text) {

        VBox groupVBox = new VBox();
//        groupVBox.setSpacing(50);

        HBox fontHBox = new HBox();
        fontHBox.setSpacing(5);

        VBox fontVBox = new VBox();
        Label fontLabel = new Label("Font");
        ListView fontFamilyListView = new ListView();
        fontFamilyListView.setItems(familyList);

        fontVBox.getChildren().addAll(fontLabel, fontFamilyListView);

        fontFamilyListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> ov,
                    Label old_val, Label new_val) {
                currentFontFamily = new_val.getText();
                setFontStyleList(fontStyleListView);
                setTextStyle();
            }
        });

        VBox fontNameVBox = new VBox();
        Label fontNameLabel = new Label("Font Style");

        fontStyleListView.setItems(fontStyleList);
        fontNameVBox.getChildren().addAll(fontNameLabel, fontStyleListView);

        fontStyleListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> ov,
                    Label old_val, Label new_val) {
//                System.out.println("Changed!!");
//                System.out.println(fontStyleListView.getSelectionModel().getSelectedItem());
                setTextStyle();
            }
        });

        VBox fontSizeVBox = new VBox();
        Label fontSizeLabel = new Label("Size");
        ListView fontSizeListView = new ListView();
        fontSizeListView.setItems(fontSizeList);

        fontSizeListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                currentFontSize = Integer.parseInt(new_val);
                setTextStyle();
            }
        });

        fontSizeVBox.getChildren().addAll(fontSizeLabel, fontSizeListView);

        // Setup sample text
        VBox titleVBox = new VBox();
        TitledPane titledPane = new TitledPane();
//        titledPane.setCollapsible(false);
        titledPane.setText("Sample");
        sampleTextArea = new TextArea(sampleText);
        setTextStyle();
        titledPane.setContent(sampleTextArea);
        titleVBox.getChildren().add(titledPane);

        // Setup dialog box buttons
        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER_RIGHT);
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        buttonHBox.getChildren().addAll(okButton, cancelButton);

        okButton.setOnAction(new OkHandler(text));
        cancelButton.setOnAction(new CancelHandler());
        
        fontHBox.getChildren().addAll(fontVBox, fontNameVBox, fontSizeVBox);
        groupVBox.getChildren().addAll(fontHBox, titleVBox, buttonHBox);

        Color sceneColor = Color.BROWN;
        Scene scene = new Scene(groupVBox, 400, 500, sceneColor);

        primaryStage.setTitle("Font Dialog Box");
        primaryStage.setResizable(true);
        primaryStage.initOwner(owner);
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.setScene(scene);

        return primaryStage;
    }

    private class OkHandler implements EventHandler {

        private TextInputControl text;

        public OkHandler(TextInputControl text) {
            this.text = text;
        }

        @Override
        public void handle(Event t) {
            text.setStyle(getFontStyle());
            primaryStage.close();
        }
    }

    private class ApplyHandler implements EventHandler {

        private TextInputControl text;

        public ApplyHandler(TextInputControl text) {
            this.text = text;
        }

        @Override
        public void handle(Event t) {
            text.setStyle(getFontStyle());
        }
    }

    private class CancelHandler implements EventHandler {

        @Override
        public void handle(Event t) {
            primaryStage.close();
        }
    }

    private void setTextStyle() {
        sampleTextArea.setStyle(getFontStyle());
    }

    public String getFontStyle() {
        return "-fx-text-fill: " + textFillColor + ";"
                + "-fx-background-color: " + textBackgroundColor + ";"
                + "-fx-font-family: " + currentFontFamily + ";"
                + "-fx-font-size: " + currentFontSize + ";"
                + "-fx-font-style: " + currentFontStyle + ";"
                + "-fx-font-weight: " + currentFontWeight;
        // -fx-font: is not needed and produces a warning
    }
}
