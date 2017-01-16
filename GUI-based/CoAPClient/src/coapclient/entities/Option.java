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
    private int delta;
    private int length;
    private int deltaExtended;
    private int lengthExtended;
    private String value;

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDeltaExtended() {
        return deltaExtended;
    }

    public void setDeltaExtended(int deltaExtended) {
        this.deltaExtended = deltaExtended;
    }

    public int getLengthExtended() {
        return lengthExtended;
    }

    public void setLengthExtended(int lengthExtended) {
        this.lengthExtended = lengthExtended;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
