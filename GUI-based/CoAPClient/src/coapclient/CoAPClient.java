/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient;

import coapclient.enums.MessageType;
import coapclient.enums.Version;
import coapclient.util.CommandUtil;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.concurrent.ExecutionException;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
        ComboBox<Version> cbxVersion = new ComboBox<>();
        cbxVersion.getItems().setAll(Version.values());
        Label msgTypeLabel = new Label("Msg. Type:");
        ComboBox<MessageType> cbxMsgType = new ComboBox<>();
        cbxMsgType.getItems().setAll(MessageType.values());
        cbxMsgType.setMaxWidth(70);
        Label tklLabel = new Label("Token Length:");
        Spinner<Integer> tklSpinner = new Spinner<>(0, 15, 1);
        tklSpinner.setEditable(true);
        tklSpinner.getEditor().setTextFormatter(numberFormatter0);
        tklSpinner.setMaxWidth(70);

        HBox header1HBox = new HBox(verLabel, cbxVersion, msgTypeLabel, cbxMsgType, tklLabel, tklSpinner);
        header1HBox.setStyle("-fx-spacing: 5");
        header1HBox.setAlignment(Pos.CENTER_LEFT);

        Label codeLabel = new Label("Code:");
        Spinner<Integer> codeClassSpinner = new Spinner<>(0, 7, 1);
        codeClassSpinner.setEditable(true);
        codeClassSpinner.getEditor().setTextFormatter(numberFormatter1);
        codeClassSpinner.setMaxWidth(60);
        Spinner<Integer> codeDetailSpinner = new Spinner<>(0, 31, 1);
        codeDetailSpinner.setEditable(true);
        codeDetailSpinner.getEditor().setTextFormatter(numberFormatter2);
        codeDetailSpinner.setMaxWidth(70);
        Label msgID = new Label("Msg. ID:");
        Spinner<Integer> msgIDSpinner = new Spinner<>(0, 65535, 1);
        msgIDSpinner.setEditable(true);
        msgIDSpinner.getEditor().setTextFormatter(numberFormatter3);

        HBox header2HBox = new HBox(codeLabel, codeClassSpinner, codeDetailSpinner, msgID, msgIDSpinner);
        header2HBox.setStyle("-fx-spacing: 5");
        header2HBox.setAlignment(Pos.CENTER_LEFT);
        
        HBox tokenSepHBox = createNameSeparator("Token");

        Label label1 = new Label("CoAP Message (Binary):");
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
        Pane helpPane = new Pane();
        HBox helpHBox = new HBox(label1, helpPane, btnHelp);
        helpHBox.setStyle("-fx-spacing: 5");
        helpHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(helpPane, Priority.ALWAYS);

        binaryTextArea = new TextArea();
        binaryTextArea.setWrapText(true);
        binaryTextArea.setFont(new Font("Courier New", 15));
        HBox textAreaHBox = new HBox(binaryTextArea);
        textAreaHBox.setStyle("-fx-spacing: 5");
        textAreaHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(binaryTextArea, Priority.ALWAYS);
        Button btnSend = new Button();
        btnSend.setText("Send");
        btnSend.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!binaryTextArea.getText().equals("") && !destIPTextField.getText().equals("")) {
                    String hexStr = convertBinaryToHex(binaryTextArea.getText().replaceAll("\\s", ""));
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

        HBox buttonHBox = new HBox(btnSend, btnClear);
        buttonHBox.setStyle("-fx-spacing: 5");
        buttonHBox.setAlignment(Pos.CENTER_LEFT);

        Separator separator1 = new Separator();

        Label label2 = new Label("Received Packet's Content:");

        rcvLabel = new Label("Nothing Yet!");

        HBox rcvLabelHBox = new HBox(rcvLabel);
        rcvLabelHBox.setStyle("-fx-spacing: 5");
        rcvLabelHBox.setAlignment(Pos.CENTER_LEFT);

        VBox vBox = new VBox(destSepHBox, destHBox, headerSepHBox, header1HBox, header2HBox, tokenSepHBox, helpHBox, textAreaHBox, buttonHBox, separator1, label2, rcvLabelHBox);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20, 20, 20, 20));

        StackPane root = new StackPane();
        root.getChildren().add(vBox);

        Scene scene = new Scene(root, 600, 500);

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
