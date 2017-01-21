/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapc.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author daniel
 */
public class PayloadMarkerValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {

        int length = 8;

        if (!value.toLowerCase().equals("r")) {
            if (value.length() <= length && value.length() > 0) {
                char[] myChars = value.toCharArray();
                for (int i = 0; i < myChars.length; i++) {
                    if (myChars[i] != '0') {
                        if (myChars[i] != '1') {
                            throw new ParameterException("Parameter " + name + " should be 'R', 'r' or "
                                    + length + "-bit in binary format. (found " + value + ")");
                        }
                    }
                }
            } else {
                throw new ParameterException("Parameter " + name + " should be 'R', 'r' or "
                        + length + "-bit in binary format. (found " + value + ")");
            }
        }
    }
}
