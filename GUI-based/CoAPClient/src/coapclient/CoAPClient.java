/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient;

import coapclient.dialogs.NewOptionDialog;
import coapclient.entities.CoapMessage;
import coapclient.entities.Option;
import coapclient.enums.MessageType;
import coapclient.util.CommandUtil;
import coapclient.util.StringUtil;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author daniel
 */
public class CoAPClient extends Application {

    final private CommandUtil cmd = new CommandUtil();
    private Label rcvLabel;
    private TextArea binaryTextArea;
    private TextField destIPTextField;
    private TextField destPortTextField;
    private String sendMsgCommand = "echo '%s' | xxd -r -p | nc -u -w1 %s %s";

    private Spinner<Integer> versionSpinner;
    private CheckBox versionRandom;
    private ComboBox<MessageType> cbxMsgType;
    private CheckBox msgTypeRandom;
    private Spinner<Integer> tklSpinner;
    private CheckBox tklRandom;
    private Spinner<Integer> codeClassSpinner;
    private Spinner<Integer> codeDetailSpinner;
    private CheckBox codeRandom;
    private Spinner<Integer> msgIDSpinner;
    private CheckBox msgIDRandom;
    private TextField tokenTextField;
    private CheckBox tokenRandom;
    private TableView<Option> optionsTable = new TableView<>();
    private ObservableList<Option> options = FXCollections.observableArrayList();
    private CheckBox optionRandom;
    private CheckBox enablePayload;
    private CheckBox defaultPayloadMarker;
    private CheckBox payloadMarkerRandom;
    private TextField payloadMarkerTextField;
    private TextArea payloadTextArea;
    private CheckBox payloadRandom;

    @Override

    public void start(Stage primaryStage) {

        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0
                        || parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> numberFormatter0 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        TextFormatter<Integer> numberFormatter1 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        TextFormatter<Integer> numberFormatter2 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        TextFormatter<Integer> numberFormatter3 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        TextFormatter<Integer> numberFormatter4 = new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter);

        HBox destSepHBox = createNameSeparator("Destination");

        Label iplabel = new Label("IP:");
        destIPTextField = new TextField();
        Label portlabel = new Label("Port:");
        destPortTextField = new TextField();
        destPortTextField.setPromptText("e.g., 5683");
        HBox destHBox = new HBox(iplabel, destIPTextField, portlabel, destPortTextField);
        destHBox.setStyle("-fx-spacing: 5");
        destHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(destIPTextField, Priority.ALWAYS);

        HBox headerSepHBox = createNameSeparator("Header");

        Label verLabel = new Label("Version:");
        versionSpinner = new Spinner<>(0, 3, 0);
        versionSpinner.setEditable(true);
        versionSpinner.setMaxWidth(60);
        versionSpinner.getEditor().setTextFormatter(numberFormatter4);
        versionRandom = new CheckBox("R?");
        versionRandom.setTooltip(new Tooltip("Random selection"));
        versionRandom.setSelected(false);
        versionRandom.setOnAction(e -> handleVersionRandomAction(e));
        Label msgTypeLabel = new Label("  Msg. Type:");
        cbxMsgType = new ComboBox<>();
        cbxMsgType.getItems().setAll(MessageType.values());
        cbxMsgType.getSelectionModel().selectFirst();
        cbxMsgType.setMaxWidth(100);
        msgTypeRandom = new CheckBox("R?");
        msgTypeRandom.setTooltip(new Tooltip("Random selection"));
        msgTypeRandom.setSelected(false);
        msgTypeRandom.setOnAction(e -> handleMsgTypeRandomAction(e));
        
        HBox header0HBox = new HBox(verLabel, versionSpinner, versionRandom, msgTypeLabel, cbxMsgType, msgTypeRandom); //, tklLabel, tklSpinner);
        header0HBox.setStyle("-fx-spacing: 5");
        header0HBox.setAlignment(Pos.CENTER_LEFT);
        
        Label tklLabel = new Label("Token Length:");
        tklSpinner = new Spinner<>(0, 15, 0);
        tklSpinner.setEditable(true);
        tklSpinner.getEditor().setTextFormatter(numberFormatter0);
        tklSpinner.setMaxWidth(70);
        tklRandom = new CheckBox("R?");
        tklRandom.setTooltip(new Tooltip("Random selection"));
        tklRandom.setSelected(false);
        tklRandom.setOnAction(e -> handleTklRandomAction(e));
        
        Label codeLabel = new Label("   Code:");
        codeClassSpinner = new Spinner<>(0, 7, 0);
        codeClassSpinner.setEditable(true);
        codeClassSpinner.getEditor().setTextFormatter(numberFormatter1);
        codeClassSpinner.setMaxWidth(60);
        codeDetailSpinner = new Spinner<>(0, 31, 0);
        codeDetailSpinner.setEditable(true);
        codeDetailSpinner.getEditor().setTextFormatter(numberFormatter2);
        codeDetailSpinner.setMaxWidth(70);
        codeRandom = new CheckBox("R?");
        codeRandom.setTooltip(new Tooltip("Random selection"));
        codeRandom.setSelected(false);
        codeRandom.setOnAction(e -> handleCodeRandomAction(e));

        HBox header1HBox = new HBox(tklLabel, tklSpinner, tklRandom, codeLabel, codeClassSpinner, codeDetailSpinner, codeRandom);
        header1HBox.setStyle("-fx-spacing: 5");
        header1HBox.setAlignment(Pos.CENTER_LEFT);

        
        Label msgID = new Label("Msg. ID:");
        msgIDSpinner = new Spinner<>(0, 65535, 0);
        msgIDSpinner.setEditable(true);
        msgIDSpinner.getEditor().setTextFormatter(numberFormatter3);
        msgIDRandom = new CheckBox("R?");
        msgIDRandom.setTooltip(new Tooltip("Random selection"));
        msgIDRandom.setSelected(false);
        msgIDRandom.setOnAction(e -> handleMsgIDRandomAction(e));

        HBox header2HBox = new HBox(msgID, msgIDSpinner, msgIDRandom);
        header2HBox.setStyle("-fx-spacing: 5");
        header2HBox.setAlignment(Pos.CENTER_LEFT);

        HBox tokenSepHBox = createNameSeparator("Token");

        Label tokenLabel = new Label("Token:");
        tokenTextField = new TextField();
        tokenRandom = new CheckBox("R?");
        tokenRandom.setTooltip(new Tooltip("Random selection"));
        tokenRandom.setSelected(false);
        tokenRandom.setOnAction(e -> handleTokenRandomAction(e));
        HBox tokenHBox = new HBox(tokenLabel, tokenTextField, tokenRandom);
        tokenHBox.setStyle("-fx-spacing: 5");
        tokenHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tokenTextField, Priority.ALWAYS);

        HBox optionsSepHBox = createNameSeparator("Option(s)");

        TableColumn deltaCol = new TableColumn("Delta");
        deltaCol.setCellValueFactory(new PropertyValueFactory("delta"));
        TableColumn lengthCol = new TableColumn("Length");
        lengthCol.setCellValueFactory(new PropertyValueFactory("length"));
        TableColumn deltaExtendedCol = new TableColumn("Delta Extended");
        deltaExtendedCol.setCellValueFactory(new PropertyValueFactory("deltaExtended"));
        TableColumn lengthExtendedCol = new TableColumn("Length Extended");
        lengthExtendedCol.setCellValueFactory(new PropertyValueFactory("lengthExtended"));
        TableColumn valueCol = new TableColumn("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory("value"));
        optionsTable.getColumns().addAll(deltaCol, lengthCol, deltaExtendedCol, lengthExtendedCol, valueCol);
        optionsTable.setItems(options);

        Button btnAdd = new Button();
        btnAdd.setText("+");
        btnAdd.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                newOption();
            }
        });

        Button btnRemove = new Button();
        btnRemove.setText("-");
        btnRemove.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                deleteOption();
            }
        });
        
        optionRandom = new CheckBox("R?");
        optionRandom.setTooltip(new Tooltip("Random selection"));
        optionRandom.setSelected(false);
        optionRandom.setOnAction(e -> handleOptionRandomAction(e));

        HBox optionsBtnHBox = new HBox(btnAdd, btnRemove, optionRandom);
        optionsBtnHBox.setStyle("-fx-spacing: 5");
        optionsBtnHBox.setAlignment(Pos.CENTER_LEFT);

        HBox payloadHBox = createNameSeparator("Payload");
        enablePayload = new CheckBox("Including payload");
        enablePayload.setSelected(false);
        enablePayload.setOnAction(e -> handleEnablePayloadAction(e));
        defaultPayloadMarker = new CheckBox("Default Payload Marker");
        defaultPayloadMarker.setSelected(true);
        defaultPayloadMarker.setDisable(true);
        defaultPayloadMarker.setOnAction(e -> handleDefaultPayloadMarkerAction(e));
        payloadMarkerRandom = new CheckBox("R?");
        payloadMarkerRandom.setTooltip(new Tooltip("Random selection"));
        payloadMarkerRandom.setSelected(false);
        payloadMarkerRandom.setDisable(true);
        payloadMarkerRandom.setOnAction(e -> handlePayloadMarkerRandomAction(e));
        payloadMarkerTextField = new TextField();
        payloadMarkerTextField.setText("11111111");
        payloadMarkerTextField.setDisable(true);
        HBox pmHBox = new HBox(defaultPayloadMarker, payloadMarkerRandom, payloadMarkerTextField);
        pmHBox.setStyle("-fx-spacing: 5");
        pmHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(payloadMarkerTextField, Priority.ALWAYS);

        Label pLabel = new Label("Payload:");
        payloadRandom = new CheckBox("R?");
        payloadRandom.setTooltip(new Tooltip("Random selection"));
        payloadRandom.setSelected(false);
        payloadRandom.setDisable(true);
        payloadRandom.setOnAction(e -> handlePayloadRandomAction(e));
        
        HBox pLabelHBox = new HBox(pLabel, payloadRandom);
        pLabelHBox.setStyle("-fx-spacing: 5");
        pLabelHBox.setAlignment(Pos.CENTER_LEFT);
        
        payloadTextArea = new TextArea();
        payloadTextArea.setMaxWidth(460);
        payloadTextArea.setDisable(true);

        Separator separator1 = new Separator();

        Button btnSend = new Button();
        btnSend.setText("Send");
        btnSend.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                CoapMessage coapMessage = getCoapMessage();
                String binaryString = coapMessage.toBitString();
                if (!binaryString.equals("") && !destIPTextField.getText().equals("") && !destPortTextField.getText().equals("")) {
                    String hexStr = convertBinaryToHex(binaryString.replaceAll("\\s", ""));
                    String command = String.format(sendMsgCommand, hexStr, destIPTextField.getText(), destPortTextField.getText());

                    Task<String> task = new Task<String>() {
                        @Override
                        public String call() throws Exception {
                            return cmd.executeCommand("", command, false, "");
                        }
                    };

                    task.setOnRunning((e) -> {
                        rcvLabel.setText("Waiting...");
                    });

                    task.setOnSucceeded((e) -> {
                        try {
                            String result = task.get();
                            rcvLabel.setText(result);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CoAPClient.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(CoAPClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    new Thread(task).start();

                }
            }
        });
        Button btnClear = new Button();
        btnClear.setText("Clear");
        btnClear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                binaryTextArea.setText("");
            }
        });

        Button btnBinary = new Button();
        btnBinary.setText("01");
        btnBinary.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                CoapMessage coapMessage = getCoapMessage();
                if (coapMessage != null) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("01");
                    alert.setHeaderText("CoAP Message (Binary):");
                    alert.setContentText(coapMessage.toBitString());
                    alert.showAndWait();
                }
            }
        });
        Button btnHelp = new Button();
        btnHelp.setText(" ? ");
        btnHelp.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Dialog helpDialog = new Dialog();
                helpDialog.initModality(Modality.NONE);
                helpDialog.setContentText("| Ver(2) | Type(2) | TKL(4) | Code(8) | MsgID(16) |\n"
                        + "| Token (if any)... \n| Options[Op. relative no(4)|Op. length(4)|Op. value]\n| 11111111 [optional] | Payload (if any)");
                helpDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                helpDialog.show();
            }
        });
        Pane buttonPane = new Pane();

        HBox buttonHBox = new HBox(btnSend, btnClear, buttonPane, btnBinary, btnHelp);
        buttonHBox.setStyle("-fx-spacing: 5");
        buttonHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(buttonPane, Priority.ALWAYS);

        VBox reqVBox = new VBox(destSepHBox, destHBox, headerSepHBox, header0HBox, header1HBox, header2HBox,
                tokenSepHBox, tokenHBox, optionsSepHBox, optionsTable, optionsBtnHBox, payloadHBox,
                enablePayload, pmHBox, pLabelHBox, payloadTextArea, separator1, buttonHBox);
        reqVBox.setSpacing(10);
        reqVBox.setPadding(new Insets(20, 20, 20, 20));

        Separator sepVert = new Separator();
        sepVert.setOrientation(Orientation.VERTICAL);
        sepVert.setValignment(VPos.CENTER);

        Label label2 = new Label("Received Packet's Content:");

        rcvLabel = new Label("Nothing Yet!");

        HBox rcvLabelHBox = new HBox(rcvLabel);
        rcvLabelHBox.setStyle("-fx-spacing: 5");
        rcvLabelHBox.setAlignment(Pos.CENTER_LEFT);

        VBox resVBox = new VBox(label2, rcvLabelHBox);
        resVBox.setSpacing(10);
        resVBox.setPadding(new Insets(20, 20, 20, 20));

        HBox hbox = new HBox(reqVBox, sepVert, resVBox);

        StackPane root = new StackPane();
        root.getChildren().add(hbox);

        Scene scene = new Scene(root, 960, 700);

        primaryStage.setTitle("CoAP Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String convertBinaryToHex(String binInPut) {
        int chunkLength = binInPut.length() / 4, startIndex = 0, endIndex = 4;
        String chunkVal = null;
        String hex = "";
        for (int i = 0; i < chunkLength; i++) {
            chunkVal = binInPut.substring(startIndex, endIndex);
            hex = hex + Integer.toHexString(Integer.parseInt(chunkVal, 2));
            startIndex = endIndex;
            endIndex = endIndex + 4;
        }

        return hex;

    }

    private HBox createNameSeparator(String name) {
        Separator separator = new Separator();
        Label label = new Label(name);
        label.setTextFill(Color.DARKGRAY);
        HBox hBox = new HBox(label, separator);
        hBox.setStyle("-fx-spacing: 5");
        hBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(separator, Priority.ALWAYS);
        return hBox;
    }

    private void newOption() {
        NewOptionDialog pd = new NewOptionDialog();
        Optional<Option> result = pd.showAndWait();
        result.ifPresent(option -> options.add(option));
    }

    private void deleteOption() {
        Option selectedOption = optionsTable.getSelectionModel().getSelectedItem();
        if (selectedOption != null) {
            options.remove(selectedOption);
        }
    }

    private void handleDefaultPayloadMarkerAction(ActionEvent e) {
        if (defaultPayloadMarker.isSelected()) {
            payloadMarkerRandom.setSelected(false);
            payloadMarkerTextField.setText("11111111");
            payloadMarkerTextField.setDisable(true);
        } else {
            payloadMarkerTextField.setDisable(false);
        }
    }

    private void handleEnablePayloadAction(ActionEvent e) {
        if (enablePayload.isSelected()) {
            defaultPayloadMarker.setDisable(false);
            payloadMarkerRandom.setDisable(false);
            payloadTextArea.setDisable(false);
            payloadRandom.setDisable(false);
            if (defaultPayloadMarker.isSelected()) {
                payloadMarkerTextField.setText("11111111");
                payloadMarkerTextField.setDisable(true);
            } else {
                payloadMarkerTextField.setDisable(false);
            }
        } else {
            defaultPayloadMarker.setDisable(true);
            payloadMarkerRandom.setDisable(true);
            payloadMarkerTextField.setDisable(true);
            payloadTextArea.setDisable(true);
            payloadRandom.setDisable(true);
        }
    }

    private void handleVersionRandomAction(ActionEvent e) {
        if (versionRandom.isSelected()) {
            Random rand = new Random();
            versionSpinner.getEditor().setText(String.valueOf(rand.nextInt(4)));
        } else {
            versionSpinner.getEditor().setText(String.valueOf("0"));
        }
    }
    
    private void handleMsgTypeRandomAction(ActionEvent e) {
        if (msgTypeRandom.isSelected()) {
            Random rand = new Random();
            cbxMsgType.getSelectionModel().select(rand.nextInt(4));
        } else {
            cbxMsgType.getSelectionModel().selectFirst();
        }
    }
    
    private void handleTklRandomAction(ActionEvent e) {
        if (tklRandom.isSelected()) {
            Random rand = new Random();
            tklSpinner.getEditor().setText(String.valueOf(rand.nextInt(16)));
        } else {
            tklSpinner.getEditor().setText(String.valueOf("0"));
        }
    }
    
    private void handleCodeRandomAction(ActionEvent e) {
        if (codeRandom.isSelected()) {
            Random rand1 = new Random();
            Random rand2 = new Random();
            codeClassSpinner.getEditor().setText(String.valueOf(rand1.nextInt(8)));
            codeDetailSpinner.getEditor().setText(String.valueOf(rand2.nextInt(32)));
        } else {
            codeClassSpinner.getEditor().setText(String.valueOf("0"));
            codeDetailSpinner.getEditor().setText(String.valueOf("0"));
        }
    }
    
    private void handleMsgIDRandomAction(ActionEvent e) {
        if (msgIDRandom.isSelected()) {
            Random rand = new Random();
            msgIDSpinner.getEditor().setText(String.valueOf(rand.nextInt(65536)));
        } else {
            msgIDSpinner.getEditor().setText(String.valueOf("0"));
        }
    }
    
    private void handleTokenRandomAction(ActionEvent e) {
        if (tokenRandom.isSelected()) {
            tokenTextField.setText(new BigInteger(120, new Random()).toString());
        } else {
            tokenTextField.setText("");
        }
    }
    
    private void handlePayloadMarkerRandomAction(ActionEvent e) {
        if (payloadMarkerRandom.isSelected()) {
            defaultPayloadMarker.setSelected(false);
            payloadMarkerTextField.setText(new StringUtil().getRandomBinary(8));
        } 
    }
    
    private void handlePayloadRandomAction(ActionEvent e) {
        if (payloadRandom.isSelected()) {
            payloadTextArea.setText(new StringUtil().getRandom(new Random().nextInt(80)));
        } else {
            payloadTextArea.setText("");
        }
       
    }
    
    private void handleOptionRandomAction(ActionEvent e) {
        if (optionRandom.isSelected()) {
            options.clear();
            Random rand1 = new Random();
            Random rand2 = new Random();
            Random rand3 = new Random();
            Random rand4 = new Random();
            Random rand5 = new Random();
            for (int i = 0; i < rand1.nextInt(12); i++) {
                Option o = new Option();
                o.setDelta(rand2.nextInt(16));
                o.setDeltaExtended(rand2.nextInt(65536));
                o.setLength(rand3.nextInt(16));
                o.setLengthExtended(rand4.nextInt(65536));
                o.setValue(new StringUtil().getRandom(rand5.nextInt(80)));
                options.add(o);
            }
        } else {
            options.clear();
        }
    }

    private CoapMessage getCoapMessage() {
        if (reqCheckPassed()) {
            CoapMessage coapMessage = new CoapMessage();
            coapMessage.setVersion(Integer.parseInt(versionSpinner.getEditor().getText()));
            coapMessage.setMsgType(cbxMsgType.getSelectionModel().getSelectedItem().ordinal());
            coapMessage.setTklLength(Integer.parseInt(tklSpinner.getEditor().getText()));
            coapMessage.setCodeClass(Integer.parseInt(codeClassSpinner.getEditor().getText()));
            coapMessage.setCodeDetail(Integer.parseInt(codeDetailSpinner.getEditor().getText()));
            coapMessage.setMsgID(Integer.parseInt(msgIDSpinner.getEditor().getText()));
            if (!tokenTextField.getText().equals("")) {
                coapMessage.setToken(new BigInteger(tokenTextField.getText()));
            }
            coapMessage.setOptions(options);
            coapMessage.setHasPayload(enablePayload.isSelected());
            coapMessage.setPayloadMarker(payloadMarkerTextField.getText());
            coapMessage.setPayload(payloadTextArea.getText());
            return coapMessage;
        } else {
            return null;
        }
    }

    private boolean reqCheckPassed() {
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
