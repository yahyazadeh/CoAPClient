/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient;

import coapclient.util.CommandUtil;
import java.util.concurrent.ExecutionException;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author daniel
 */
public class CoAPClient extends Application {

    final private CommandUtil cmd = new CommandUtil();
    private Label rcvLabel;
    private TextArea binaryTextArea;
    private TextField destTextField;
    private String sendMsgCommand = "echo '%s' | xxd -r -p | nc -u -w1 %s 5683";

    @Override

    public void start(Stage primaryStage) {
        Label label = new Label("Destination IP:");
        destTextField = new TextField();
        HBox destHBox = new HBox(label, destTextField);
        destHBox.setStyle("-fx-spacing: 5");
        destHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(destTextField, Priority.ALWAYS);

        Separator separator0 = new Separator();

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
        binaryTextArea.setFont(new Font("Courier New",15));
        HBox textAreaHBox = new HBox(binaryTextArea);
        textAreaHBox.setStyle("-fx-spacing: 5");
        textAreaHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(binaryTextArea, Priority.ALWAYS);
        Button btnSend = new Button();
        btnSend.setText("Send");
        btnSend.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!binaryTextArea.getText().equals("") && !destTextField.getText().equals("")) {
                    String hexStr = convertBinaryToHex (binaryTextArea.getText().replaceAll("\\s",""));
                    String command = String.format(sendMsgCommand, hexStr, destTextField.getText());

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

        VBox vBox = new VBox(destHBox, separator0, helpHBox, textAreaHBox, buttonHBox, separator1, label2, rcvLabelHBox);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
