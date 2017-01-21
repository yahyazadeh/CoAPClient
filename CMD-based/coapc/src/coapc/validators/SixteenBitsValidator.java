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
public class SixteenBitsValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {
        
        int min = 0;
        int max = 65535;

        if (!value.toLowerCase().equals("r")) {
            try {
                int n = Integer.parseInt(value);
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
