/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapc;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author daniel
 */
public class Coapc {

    public static void main(String[] args) {
        // TODO code application logic here

        try {
            Client client = new Client();
            JCommander jc = new JCommander(client, args);
            jc.setProgramName("java -jar coapc.jar");
            try {
                client.send();
            } catch (NullPointerException ex) {
                jc.usage();
            }
        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
