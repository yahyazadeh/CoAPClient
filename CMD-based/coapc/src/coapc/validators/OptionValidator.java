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
public class OptionValidator implements IParameterValidator {

    public void validate(String name, String value) throws ParameterException {

        int dMin = 0;
        int dMax = 15;

        int lMin = 0;
        int lMax = 15;

        int deMin = 0;
        int deMax = 65535;

        int leMin = 0;
        int leMax = 65535;

        int vMax = 65804;

        if (!value.toLowerCase().equals("r")) {
            try {
                if (value.contains(":")) {
                    String[] parts = value.split("\\:");
                    if (parts.length == 5) {
                        int d = Integer.parseInt(parts[0]);
                        if (d < dMin || d > dMax) {
                            throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                    + " delta is between " + dMin + " and " + dMax
                                    + " and length is between " + lMin + " and " + lMax
                                    + " and deltaExt is between " + deMin + " and " + deMax
                                    + " and lengthExt is between " + leMin + " and " + leMax
                                    + " and value length should be less than " + vMax + ". (found " + value + ")");
                        } else {
                            int l = Integer.parseInt(parts[1]);
                            if (l < dMin || l > dMax) {
                                throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                        + " delta is between " + dMin + " and " + dMax
                                        + " and length is between " + lMin + " and " + lMax
                                        + " and deltaExt is between " + deMin + " and " + deMax
                                        + " and lengthExt is between " + leMin + " and " + leMax
                                        + " and value length should be less than " + vMax + ". (found " + value + ")");
                            } else {
                                int de = Integer.parseInt(parts[2]);
                                if (de < deMin || de > deMax) {
                                    throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                            + " delta is between " + dMin + " and " + dMax
                                            + " and length is between " + lMin + " and " + lMax
                                            + " and deltaExt is between " + deMin + " and " + deMax
                                            + " and lengthExt is between " + leMin + " and " + leMax
                                            + " and value length should be less than " + vMax + ". (found " + value + ")");
                                } else {
                                    int le = Integer.parseInt(parts[3]);
                                    if (le < leMin || le > leMax) {
                                        throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                                + " delta is between " + dMin + " and " + dMax
                                                + " and length is between " + lMin + " and " + lMax
                                                + " and deltaExt is between " + deMin + " and " + deMax
                                                + " and lengthExt is between " + leMin + " and " + leMax
                                                + " and value length should be less than " + vMax + ". (found " + value + ")");
                                    } else if (parts[4].length() > vMax) {
                                        throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                                + " delta is between " + dMin + " and " + dMax
                                                + " and length is between " + lMin + " and " + lMax
                                                + " and deltaExt is between " + deMin + " and " + deMax
                                                + " and lengthExt is between " + leMin + " and " + leMax
                                                + " and value length should be less than " + vMax + ". (found " + value + ")");
                                    }
                                }
                            }
                        }
                    } else {
                        throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                                + " delta is between " + dMin + " and " + dMax
                                + " and length is between " + lMin + " and " + lMax
                                + " and deltaExt is between " + deMin + " and " + deMax
                                + " and lengthExt is between " + leMin + " and " + leMax
                                + " and value length should be less than " + vMax + ". (found " + value + ")");
                    }
                } else {
                    throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                            + " delta is between " + dMin + " and " + dMax
                            + " and length is between " + lMin + " and " + lMax
                            + " and deltaExt is between " + deMin + " and " + deMax
                            + " and lengthExt is between " + leMin + " and " + leMax
                            + " and value length should be less than " + vMax + ". (found " + value + ")");
                }

            } catch (NumberFormatException ex) {
                throw new ParameterException("Parameter " + name + " should be 'R', 'r', or delta:length:deltaExt:lengthExt:value such that"
                        + " delta is between " + dMin + " and " + dMax
                        + " and length is between " + lMin + " and " + lMax
                        + " and deltaExt is between " + deMin + " and " + deMax
                        + " and lengthExt is between " + leMin + " and " + leMax
                        + " and value length should be less than " + vMax + ". (found " + value + ")");
            }
        }
    }
}
