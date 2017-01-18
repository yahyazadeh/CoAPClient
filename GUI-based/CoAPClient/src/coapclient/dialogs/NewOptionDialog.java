/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient.dialogs;

import coapclient.entities.Option;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author daniel
 */
public class NewOptionDialog extends Dialog<Option> {

    private Spinner<Integer> deltaSpinner;
    private Spinner<Integer> lengthSpinner;
    private Spinner<Integer> deltaExtSpinner;
    private Spinner<Integer> lengthExtSpinner;
    private TextArea valueTextArea;
    private Option option;

    public NewOptionDialog() {
        option = new Option();
        setTitle("New Option");
        setHeaderText("Please enter the option information:");

        ButtonType addOptionButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addOptionButtonType, ButtonType.CANCEL);

        GridPane optionGrid = new GridPane();
        optionGrid.setAlignment(Pos.CENTER);
        optionGrid.setHgap(10);
        optionGrid.setVgap(10);
        optionGrid.setPadding(new Insets(20, 20, 20, 20));
        
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
        

        Label label0 = new Label("Delta:");
        optionGrid.add(label0, 0, 0, 1, 1);

        deltaSpinner = new Spinner<>(0, 15, 1);
        deltaSpinner.setMaxWidth(70);
        deltaSpinner.setEditable(true);
        deltaSpinner.getEditor().setTextFormatter(numberFormatter0);
        optionGrid.add(deltaSpinner, 1, 0, 1, 1);
        
        Label label1 = new Label("Length:");
        label1.setMinWidth(60);
        optionGrid.add(label1, 2, 0, 1, 1);
        
        lengthSpinner = new Spinner<>(0, 15, 1);
        lengthSpinner.setMaxWidth(70);
        lengthSpinner.setEditable(true);
        lengthSpinner.getEditor().setTextFormatter(numberFormatter1);
        optionGrid.add(lengthSpinner, 3, 0, 1, 1);

        Label label2 = new Label("Delta Ext.:");
        optionGrid.add(label2, 0, 1, 1, 1);

        deltaExtSpinner = new Spinner<>(0, 65535, 1);
        deltaExtSpinner.setMaxWidth(220);
        deltaExtSpinner.setEditable(true);
        deltaExtSpinner.getEditor().setTextFormatter(numberFormatter2);
        optionGrid.add(deltaExtSpinner, 1, 1, 3, 1);

        Label label3 = new Label("Length Ext.:");
        optionGrid.add(label3, 0, 2, 1, 1);

        lengthExtSpinner = new Spinner<>(0, 65535, 1);
        lengthExtSpinner.setMaxWidth(220);
        lengthExtSpinner.setEditable(true);
        lengthExtSpinner.getEditor().setTextFormatter(numberFormatter3);
        optionGrid.add(lengthExtSpinner, 1, 2, 3, 1);
        
        Label label4 = new Label("Value:");
        optionGrid.add(label4, 0, 3, 1, 1);

        valueTextArea = new TextArea();
        valueTextArea.setMaxHeight(70);
        valueTextArea.setMaxWidth(220);
        optionGrid.add(valueTextArea, 1, 3, 3, 1);
       
        getDialogPane().setContent(optionGrid);

        setResultConverter(dialogButton -> {
            if (dialogButton == addOptionButtonType) {
                option.setDelta(deltaSpinner.getValue());
                option.setLength(lengthSpinner.getValue());
                option.setDeltaExtended(deltaExtSpinner.getValue());
                option.setLengthExtended(lengthExtSpinner.getValue());
                option.setValue(valueTextArea.getText());
                return option;
            }
            return null;
        });
    }


    public Option getGateway() {
        return option;
    }
}

