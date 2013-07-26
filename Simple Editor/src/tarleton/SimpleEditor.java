package tarleton;

import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tarleton.ExitDialogBox.ReturnValue;
import static tarleton.ExitDialogBox.ReturnValue.Cancel;
import static tarleton.ExitDialogBox.ReturnValue.DontSave;
import static tarleton.ExitDialogBox.ReturnValue.Save;

// Version 1.0  7/24/2013
// Richard Reese
public class SimpleEditor extends Application
        implements Printable {

    String applicationTitle = "SimpleEditor";
    private File initialDirectory = 
            new File("C:\\Tarleton\\CS330\\Simple Editor");
    private File currentFile = null;
    private boolean textModified = false;
    private final TextArea text = new TextArea();
    private String currentFontFamily = "Courier";
    private int currentFontSize = 12;
    private String currentFontWeight = "normal";
    private String textFillColor = "black";
    private String textBackgroundColor = "white";
    private int cursorCount;
    private int currentLineCount;
    private int currentColumnCount;
    private Label fileNameLabel;
    private Label lineNumberLabel;
    private Label columnNumberLabel;
    private String lineNumberText = "Line: ";
    private String columnNumberText = "Column: ";
    Stage primaryStage;
    private FXMLBasedFindDialogBoxController findDialogBoxController;

    public TextArea getText() {
        return text;
    }

    // Todo
    // Document changes (as Asciidoc)
    // Generic set of file extensions
    // Logging
    // Font
   // http://thierrywasyl.wordpress.com/2013/01/27/set-custom-font-in-javafx-2/
    // Icons
    // Properties
    @Override
    public void start(final Stage primaryStage) {
        this.primaryStage = primaryStage;
        File defaultDirectory = new File(".");
        try {
           // This is a workaround. Apparently using "." directly does not work
            initialDirectory = new File(initialDirectory.getCanonicalPath());
        } catch (IOException ex) {
    Logger.getLogger(SimpleEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Setup TextArea
        setUpTextArea();
        // Setup Menus
        MenuBar menuBar = getMenuBar(primaryStage);
        // Setup Status bar
        Pane statusPane = setUpStatusBar();

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(text);
        root.setBottom(statusPane);

        Scene scene = new Scene(root, 600, 250);
        primaryStage.setTitle("Simple Editor");
        primaryStage.setScene(scene);
        primaryStage.show();

System.out.println("classpath is: " + System.getProperty("java.class.path"));
InputStream url = getClass().getResourceAsStream(
        "Simple Editor/resources/Information.PNG");
System.out.println("url: " + url);
url = getClass().getResourceAsStream("/resources/Information.PNG");
System.out.println("url: " + url);
url = getClass().getResourceAsStream("resources/Information.PNG");
System.out.println("url: " + url);
url = getClass().getResourceAsStream("/Information.PNG");
System.out.println("url: " + url);
url = getClass().getResourceAsStream(
        "C:\\Tarleton\\CS330\\Simple Editor\\src\\resources\\Information.PNG");
        System.out.println("url: " + url);

        Image img = new Image(
 "file:\\C:\\Tarleton\\CS330\\Simple Editor\\src\\resources\\Information.PNG");
    }

    @Override
    public void stop() {
        // Prompt to save fiel before exit
        handleExit(true);
    }

    private void handleExit(boolean programTerminating) {
        if (textModified) {
            String fileName;
            if (currentFile == null) {
                fileName = "this text";
            } else {
                fileName = currentFile.getName();
            }
            ExitDialogBox exitDialogBox = new ExitDialogBox(
                primaryStage, applicationTitle, fileName, !programTerminating);

            ReturnValue returnValue = exitDialogBox.showDialog();

            switch (returnValue) {
                case Save:
                    handleSave();
                    break;
                case DontSave:
                    System.exit(0);
                    break;
                case Cancel:
                    break;
            }
        }
    }

    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
//        displayFileInformation(initialDirectory);
        fileChooser.setInitialDirectory(initialDirectory);

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            writeFile(file, text.getText());
            textModified = false;
            currentFile = file;
        }
    }

    private Pane setUpStatusBar() {
        BorderPane gridPane = new BorderPane();

        FlowPane fileNamePane = new FlowPane();
        fileNamePane.setAlignment(Pos.CENTER_LEFT);
        fileNameLabel = new Label();
        fileNamePane.getChildren().addAll(fileNameLabel);

        FlowPane statusPane = new FlowPane();
        statusPane.setAlignment(Pos.CENTER_RIGHT);
        lineNumberLabel = new Label(lineNumberText);
        columnNumberLabel = new Label(columnNumberText);
        statusPane.getChildren().addAll(lineNumberLabel, columnNumberLabel);

        gridPane.setLeft(fileNamePane);
        gridPane.setRight(statusPane);
        return gridPane;
    }

    private void updateStatusBar() {
        getCurrentLineNumber();
        if (currentFile != null) {
            fileNameLabel.setText("File: " + currentFile.getName());
        } else {
            fileNameLabel.setText("");
        }
    }

    private void setUpTextArea() {
        // Setup TextArea
//        String testString = "Sample Text String Text Text";
//        text.setText(testString);
        text.setPromptText("Edit ...");
        text.setWrapText(true);
        text.positionCaret(text.getText().length());
        text.setStyle(getFontStyle());
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    String old_val, String new_val) {
                textModified = true;
            }
        });

        text.caretPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                cursorCount = new_val.intValue();
                updateStatusBar();
            }
        });
    }

    private MenuBar getMenuBar(final Stage primaryStage) {
        boolean mnemonicsEnabled = false;

        MenuBar menuBar = new MenuBar();

        // Create File menu
        Menu menuFile = new Menu("_File");
        menuFile.setMnemonicParsing(mnemonicsEnabled);
        MenuItem menuNew = new MenuItem("_New");
        MenuItem menuOpen = new MenuItem("_Open");
        final MenuItem menuSave = new MenuItem("_Save");
        MenuItem menuSaveAs = new MenuItem("Save _As...");
        MenuItem menuPageSetup = new MenuItem("Page Set_up...");
        MenuItem menuPrint = new MenuItem("_Print...");
        MenuItem menuExit = new MenuItem("E_xit");

        menuNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        menuOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
//        menuSave.setAccelerator(new KeyCodeCombination(
//                KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuSaveAs.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuPrint.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuExit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        menuFile.getItems().addAll(menuNew, menuOpen, menuSave, menuSaveAs,
                new SeparatorMenuItem(), menuPageSetup, menuPrint,
                new SeparatorMenuItem(), menuExit);

        // Create Edit Menu
        Menu menuEdit = createEditMenu(primaryStage);

        // Create Format menu
        Menu menuFormat = new Menu("F_ormat");
        menuFormat.setMnemonicParsing(mnemonicsEnabled);

        MenuItem menuWordWrap = new MenuItem("Word Wrap");
        MenuItem menuFont = new MenuItem("Font...");

        menuFormat.getItems().addAll(menuWordWrap, menuFont);

        menuWordWrap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                text.setWrapText(!text.isWrapText());
            }
        });

        menuFont.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FontDialogBox fdbox = new FontDialogBox();
                Stage FDStage = fdbox.getFontDialogBox(primaryStage, text);
                FDStage.show();
                fdbox.fontFamilyProperty().addListener(new ChangeListener() {
                    @Override
                 public void changed(ObservableValue ov, Object t, Object t1) {
                throw new UnsupportedOperationException("Not supported yet."); 
               //To change body of generated methods, choose Tools | Templates.
                    }
                });
            }
        });

        // Create Help menu
        Menu menuHelp = new Menu("_Help");
        menuHelp.setMnemonicParsing(mnemonicsEnabled);
        MenuItem menuAbout = new MenuItem("About...");
        menuHelp.getItems().addAll(menuAbout);


        menuNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (textModified) {
                    menuSave.fire();
                } else {
                    text.setText("");
                    currentFile = null;
                    currentFontFamily = "Courier";
                    currentFontSize = 12;
                    currentFontWeight = "normal";
                    textFillColor = "black";
                    textBackgroundColor = "white";
                    text.setStyle(getFontStyle());
                }
            }
        });

        menuSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleSave();
                updateStatusBar();
            }
        });
        menuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                displayFileInformation(initialDirectory);
                fileChooser.setInitialDirectory(initialDirectory);

                //Set extension filter
//                    FileChooser.ExtensionFilter extFilter = 
//               new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                    fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    writeFile(file, text.getText());
                    textModified = false;
                }
                currentFile = file;
                updateStatusBar();
            }
        });
        menuOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(initialDirectory);
                //Set extension filter
                FileChooser.ExtensionFilter extFilter = 
                        new FileChooser.ExtensionFilter(
                        "TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    text.setText(readFile(file));
                    text.setStyle(getFontStyle());
                    currentFile = file;
                }
            }
        });

        menuPageSetup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                PageFormat pf = pj.pageDialog(pj.defaultPage());
            }
        });

        menuPrint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    try {
                        pj.print();
                    } catch (PrinterException exc) {
                        System.out.println(exc);
                    }
                }
            }
        });

        menuExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleExit(false);
            }
        });
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                final Stage dialogStage = new Stage();
                // initOwner needed so that WINDOW_MODAL will work properly
                dialogStage.initOwner(primaryStage);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                VBox aboutVBox = new VBox();
                Text applicationName = new Text("Simple Editor");
                Text author = new Text("Dr. Richard Reese");
                aboutVBox.getChildren().addAll(applicationName, author);
                aboutVBox.setAlignment(Pos.CENTER);
                aboutVBox.setPadding(new Insets(5));

                Scene aboutScene = new Scene(aboutVBox, 300, 100);
                dialogStage.setScene(aboutScene);
                dialogStage.setTitle("About Simple Editor");
                dialogStage.show();
            }
        });

        menuBar.getMenus().addAll(menuFile, menuEdit, menuFormat, menuHelp);
        menuBar.setFocusTraversable(true);

        return menuBar;
    }

    private Menu createEditMenu(final Stage primaryStage) {
        Menu menuEdit = new Menu("_Edit");
        menuEdit.setMnemonicParsing(true);
        MenuItem menuUndo = new MenuItem("_Undo");
        MenuItem menuRedo = new MenuItem("Redo");
        MenuItem menuCut = new MenuItem("Cu_t");
        MenuItem menuCopy = new MenuItem("_Copy");
        MenuItem menuPaste = new MenuItem("_Paste");
        MenuItem menuDelete = new MenuItem("De_lete");
        final MenuItem menuFind = new MenuItem("_Find...");
        MenuItem menuFindNext = new MenuItem("Find _Next");
        MenuItem menuReplace = new MenuItem("_Replace...");
        MenuItem menuGoTo = new MenuItem("_Go To...");
        MenuItem menuSelectAll = new MenuItem("Select _All");
        MenuItem menuTimeDate = new MenuItem("Time/_Date");

        menuUndo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        menuCut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        menuCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        menuPaste.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        menuDelete.setAccelerator(KeyCombination.keyCombination("Del"));
        menuFind.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        menuFindNext.setAccelerator(KeyCombination.keyCombination("F3"));
        menuReplace.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        menuGoTo.setAccelerator(KeyCombination.keyCombination("Ctrl+G"));
        menuSelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        menuTimeDate.setAccelerator(KeyCombination.keyCombination("F5"));

        menuUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                ((TextInputControlBehavior) ((TextInputControlSkin) 
                        text.getSkin()).getBehavior()).callAction("Undo");
            }
        });
        menuRedo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent paramT) {
                ((TextInputControlBehavior) ((TextInputControlSkin) 
                        text.getSkin()).getBehavior()).callAction("Redo");
            }
        });

        menuCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(text.getSelectedText());
                clipboard.setContent(content);
            }
        });

        menuCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(text.getSelectedText());
                clipboard.setContent(content);
                text.replaceText(text.getSelection(), "");
            }
        });

        menuPaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                String str = clipboard.getString();
                if ("".equals(text.getSelectedText())) {
                    text.insertText(text.getCaretPosition(), str);
                } else {
                    text.replaceText(text.getSelection(), str);
                }
            }
        });

        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                text.replaceText(text.getSelection(), "");
            }
        });

        menuFind.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // Find                    
                    URL location = 
                         getClass().getResource("FXMLBasedFindDialogBox.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

                    Parent root = (
                            Parent) fxmlLoader.load(location.openStream());
                    findDialogBoxController = fxmlLoader.getController();
                    findDialogBoxController.setTargetString(text);

                    Stage stage = new Stage();
                    stage.setTitle("FXML Find ...");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(
                   SimpleEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuFindNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (findDialogBoxController != null) {
                    findDialogBoxController.findNextAction(e);
                } else {
                    menuFind.fire();
                }
            }
        });

        menuReplace.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // Find                    
                    URL location = 
                            getClass().getResource("ReplaceDialogBox.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

                    final Parent root = 
                            (Parent) fxmlLoader.load(location.openStream());
                    final ReplaceDialogBoxController controller = 
                            fxmlLoader.getController();
                    controller.setTargetString(text);

                    Stage stage = new Stage();
                    stage.setTitle("Replace");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(
                    SimpleEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuGoTo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // Find                    
                   URL location = getClass().getResource("GoToDialogBox.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

                    final Parent root = 
                            (Parent) fxmlLoader.load(location.openStream());
                    final GoToDialogBoxController controller = 
                            fxmlLoader.getController();

                    controller.lineNumber.addListener(new ChangeListener() {
                        @Override
                        public void changed(ObservableValue o, Object oldVal,
                                Object newVal) {
               // Requires good knowledge of String class - Quiz them first?
                            int num = ((Integer) newVal).intValue();
                            String target = text.getText();
                            if (num < 0 || num > target.length()) {
                                // End of string
                            } else {
                                if (num == 1) {
                                    text.positionCaret(0);
                                } else {
                                    int count = 1;
                                    int oldPosition = 0;
                                    for (int i = 0; i < target.length(); i++) {
                                        if (target.charAt(i) == '\n') {
                                            if (count == num) {
                                               text.positionCaret(oldPosition);
                                               return;
                                            } else {
                                                count++;
                                                oldPosition = i + 1;
                                            }
                                        } else {
                                            // Check next character
                                        }
                                        if (i == target.length() - 1) {
                                            if (count == num) {
                                               text.positionCaret(oldPosition);
                                               return;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    });

                    Stage stage = new Stage();
                    stage.setTitle("Go To Line");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(
                     SimpleEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuSelectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Select All
                text.selectAll();
            }
        });

        menuTimeDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Time Date
                Calendar calendar = Calendar.getInstance();
                Date timeDate = calendar.getTime();
                String str = timeDate.toString();
                if ("".equals(text.getSelectedText())) {
                    text.insertText(text.getCaretPosition(), str);
                } else {
                    text.replaceText(text.getSelection(), str);
                }
            }
        });

        menuEdit.getItems().addAll(menuUndo, menuRedo,
        new SeparatorMenuItem(), menuCut, menuCopy, menuPaste, menuDelete,
        new SeparatorMenuItem(), menuFind, menuFindNext, menuReplace, menuGoTo,
        new SeparatorMenuItem(), menuSelectAll, menuTimeDate);

        return menuEdit;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException {

        // We have only one page, and 'page'
        // is zero-based
        if (page > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        // User (0,0) is typically outside the
        // imageable area, so we must translate
        // by the X and Y values in the PageFormat
        // to avoid clipping.
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Now we perform our rendering
        g.drawString(text.getText(), 100, 100);

        // tell the caller that this page is part
        // of the printed document
        return Printable.PAGE_EXISTS;
    }

    private void getCurrentLineNumber() {
        currentLineCount = 1;
        currentColumnCount = 1;
        String currentText = this.text.getText();

        for (int i = 0; i < cursorCount; i++) {
            char c = currentText.charAt(i);
            currentColumnCount++;
            if (c == '\n') {
                currentLineCount++;
                currentColumnCount = 0;
            }
        }
        lineNumberLabel.setText(lineNumberText + currentLineCount);
        columnNumberLabel.setText(columnNumberText + currentColumnCount);
    }

    private void displayFileInformation(File file) {
        System.out.println("\n\nFile Information");
        System.out.println("----------------");
        if (file != null) {
            if (file.isDirectory()) {
                System.out.print("Directory ");
            } else if (file.isFile()) {
                System.out.print("File ");
            }
            System.out.println("name: " + file.getName());
        }
        System.out.println();
    }

    private String readFile(File file) {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text + "\n");
            }
        } catch (FileNotFoundException ex) {
//Logger.getLogger(
//            JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//Logger.getLogger(
//            JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
//Logger.getLogger(
//            JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
    }

    private void writeFile(File file, String data) {
        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(data);
        } catch (FileNotFoundException ex) {
//Logger.getLogger(
//            JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//Logger.getLogger(
//            JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException ex) {
//Logger.getLogger(
//                JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public static void main(String[] args) {
        launch(args);
    }
}
