/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapclient.entities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
public class CoapMessage {

    private int version;
    private int msgType;
    private int tklLength;
    private int codeClass;
    private int codeDetail;
    private int msgID;
    private BigInteger token;
    private List<Option> options;
    private boolean hasPayload;
    private String payloadMarker;
    private String payload;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getTklLength() {
        return tklLength;
    }

    public void setTklLength(int tklLength) {
        this.tklLength = tklLength;
    }

    public int getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(int codeClass) {
        this.codeClass = codeClass;
    }

    public int getCodeDetail() {
        return codeDetail;
    }

    public void setCodeDetail(int codeDetail) {
        this.codeDetail = codeDetail;
    }

    public int getMsgID() {
        return msgID;
    }

    public void setMsgID(int msgID) {
        this.msgID = msgID;
    }

    public BigInteger getToken() {
        return token;
    }

    public void setToken(BigInteger token) {
        this.token = token;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public boolean isHasPayload() {
        return hasPayload;
    }

    public void setHasPayload(boolean hasPayload) {
        this.hasPayload = hasPayload;
    }

    public String getPayloadMarker() {
        return payloadMarker;
    }

    public void setPayloadMarker(String payloadMarker) {
        this.payloadMarker = payloadMarker;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String toBitString() {
        String bits = "";
        bits = bits + String.format("%2s", Integer.toBinaryString(version)).replace(' ', '0');
        bits = bits + String.format("%2s", Integer.toBinaryString(msgType)).replace(' ', '0');
        bits = bits + String.format("%4s", Integer.toBinaryString(tklLength)).replace(' ', '0');
        bits = bits + String.format("%3s", Integer.toBinaryString(codeClass)).replace(' ', '0');
        bits = bits + String.format("%5s", Integer.toBinaryString(codeDetail)).replace(' ', '0');
        bits = bits + String.format("%16s", Integer.toBinaryString(msgID)).replace(' ', '0');
        if (tklLength != 0) {
            if (token != null) {
                bits = bits + String.format("%" + tklLength * 8 + "s", token.toString(2)).replace(' ', '0');
            }      
        }
        for (Option o : options) {
            bits = bits + String.format("%4s", Integer.toBinaryString(o.getDelta())).replace(' ', '0');
            bits = bits + String.format("%4s", Integer.toBinaryString(o.getLength())).replace(' ', '0');
            if (o.getDelta() == 13 || o.getDelta() == 14) {
                bits = bits + String.format("%" + ((o.getDelta() == 13) ? "8" : "16") + "s", Integer.toBinaryString(o.getDeltaExtended())).replace(' ', '0');
            }
            if (o.getLength() == 13 || o.getLength() == 14) {
                bits = bits + String.format("%" + ((o.getLength() == 13) ? "8" : "16") + "s", Integer.toBinaryString(o.getLengthExtended())).replace(' ', '0');
            }
            try {
                byte[] b = o.getValue().getBytes("UTF-8");
                for (int i = 0; i < b.length; i++) {
                    bits = bits + String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (hasPayload) {
            bits = bits + String.format("%8s", payloadMarker.replace(' ', '0'));
            try {
                byte[] b = payload.getBytes("UTF-8");
                for (int i = 0; i < b.length; i++) {
                    bits = bits + String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(bits);
        }
        return bits;
    }
}
