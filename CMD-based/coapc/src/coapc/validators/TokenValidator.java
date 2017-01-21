/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapc.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import static java.lang.Math.pow;

/**
 *
 * @author daniel
 */
public class TokenValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {

        double min = 0;
        double max = pow(2, 120);

        if (!value.toLowerCase().equals("r")) {
            try {
                double n = Double.parseDouble(value);
                if (n < min || n > max) {
                    throw new ParameterException("Parameter " + name + " should be 'R' or 'r' or a number between "
                            + min + " and " + max + ". (found " + value + ")");
                }
            } catch (NumberFormatException ex) {
                throw new ParameterException("Parameter " + name + " should be 'R' or 'r' or a number between "
                        + min + " and " + max + ". (found " + value + ")");
            }
        }
    }
}
