/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient.entities;

/**
 *
 * @author daniel
 */
public class Option {
    private String delta;
    private String length;
    private String deltaExtended;
    private String lengthExtended;
    private String value;

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDeltaExtended() {
        return deltaExtended;
    }

    public void setDeltaExtended(String deltaExtended) {
        this.deltaExtended = deltaExtended;
    }

    public String getLengthExtended() {
        return lengthExtended;
    }

    public void setLengthExtended(String lengthExtended) {
        this.lengthExtended = lengthExtended;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
