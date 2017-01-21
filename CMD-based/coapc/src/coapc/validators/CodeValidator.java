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
public class CodeValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {

        int cMin = 0;
        int cMax = 7;

        int dMin = 0;
        int dMax = 31;

        if (!value.toLowerCase().equals("r")) {
            try {
                if (value.contains(".")) {
                    String[] parts = value.split("\\.");
                    int c = Integer.parseInt(parts[0]);
                    if (c < cMin || c > cMax) {
                        throw new ParameterException("Parameter " + name + " should be 'R', 'r', or c.dd such that c is between "
                                + cMin + " and " + cMax + " and d is between " + dMin + " and " + dMax + ". (found " + value + ")");
                    } else {
                        int dd = Integer.parseInt(parts[1]);
                        if (dd < dMin || dd > dMax) {
                            throw new ParameterException("Parameter " + name + " should be 'R', 'r', or c.dd such that c is between "
                                    + cMin + " and " + cMax + " and d is between " + dMin + " and " + dMax + ". (found " + value + ")");
                        }
                    }
                } else {
                    throw new ParameterException("Parameter " + name + " should be 'R', 'r', or c.dd such that c is between "
                            + cMin + " and " + cMax + " and d is between " + dMin + " and " + dMax + ". (found " + value + ")");
                }

            } catch (NumberFormatException ex) {
                throw new ParameterException("Parameter " + name + " should be 'R', 'r', or c.dd such that c is between "
                        + cMin + " and " + cMax + " and d is between " + dMin + " and " + dMax + ". (found " + value + ")");
                
            }
        }
    }
}
