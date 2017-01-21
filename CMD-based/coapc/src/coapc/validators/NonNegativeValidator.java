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
public class NonNegativeValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {

        int n = Integer.parseInt(value);
        if (n < 0) {
            throw new ParameterException("Parameter " + name + " should be non-negative. (found " + value + ")");
        }
    }
}
