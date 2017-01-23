/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapc.entities;

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
    private String payloadMarker;
    private String payload;
    private boolean hasTokenCC;
    private boolean hasOptionCC;

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

    public boolean isHasTokenCC() {
        return hasTokenCC;
    }

    public void setHasTokenCC(boolean hasTokenCC) {
        this.hasTokenCC = hasTokenCC;
    }

    public boolean isHasOptionCC() {
        return hasOptionCC;
    }

    public void setHasOptionCC(boolean hasOptionCC) {
        this.hasOptionCC = hasOptionCC;
    }

    public String toBitString() {
        String bits = "";
        bits = bits + String.format("%2s", Integer.toBinaryString(version)).replace(' ', '0');
        System.out.println(bits);
        bits = bits + String.format("%2s", Integer.toBinaryString(msgType)).replace(' ', '0');
        System.out.println(bits);
        bits = bits + String.format("%4s", Integer.toBinaryString(tklLength)).replace(' ', '0');
        System.out.println(bits);
        bits = bits + String.format("%3s", Integer.toBinaryString(codeClass)).replace(' ', '0');
        System.out.println(bits);
        bits = bits + String.format("%5s", Integer.toBinaryString(codeDetail)).replace(' ', '0');
        System.out.println(bits);
        bits = bits + String.format("%16s", Integer.toBinaryString(msgID)).replace(' ', '0');
        System.out.println(bits);
        int tklNumBits = tklLength * 8;
        if (hasTokenCC) {
            if (tklLength != 0) {
                if (token == null) {
                    token = new BigInteger("0");
                }
                String tokenBinaryString = token.toString(2);
                if (tklNumBits >= tokenBinaryString.length()) {
                    bits = bits + String.format("%" + tklNumBits + "s", tokenBinaryString).replace(' ', '0');
                } else {
                    bits = bits + tokenBinaryString.substring(tokenBinaryString.length() - tklNumBits, tokenBinaryString.length());
                }
            }
        } else if (token != null) {
            String tokenBinaryString = token.toString(2);
            bits = bits + tokenBinaryString;
        }
        System.out.println(bits);
        if (hasOptionCC) {
            for (Option o : options) {
                try {
                    if (!o.getValue().equals("")) {
                        byte[] b = o.getValue().getBytes("UTF-8");
                        if (b.length < 13) {
                            o.setLength(b.length);
                        } else if (13 <= b.length && b.length < 269) {
                            o.setLength(b.length);
                            o.setLengthExtended(b.length - 13);
                        } else if (b.length >= 269) {
                            o.setLength(14);
                            o.setLengthExtended(b.length - 269);
                        }

                        bits = bits + String.format("%4s", Integer.toBinaryString(o.getDelta())).replace(' ', '0');
                        bits = bits + String.format("%4s", Integer.toBinaryString(o.getLength())).replace(' ', '0');

                        if (o.getDelta() == 13 || o.getDelta() == 14) {
                            bits = bits + String.format("%" + ((o.getDelta() == 13) ? "8" : "16") + "s", Integer.toBinaryString(o.getDeltaExtended())).replace(' ', '0');
                        }
                        if (o.getLength() == 13 || o.getLength() == 14) {
                            bits = bits + String.format("%" + ((o.getLength() == 13) ? "8" : "16") + "s", Integer.toBinaryString(o.getLengthExtended())).replace(' ', '0');
                        }

                        for (int i = 0; i < b.length; i++) {
                            bits = bits + String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
                        }
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(CoapMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            for (Option o : options) {
                bits = bits + String.format("%4s", Integer.toBinaryString(o.getDelta())).replace(' ', '0');
                bits = bits + String.format("%4s", Integer.toBinaryString(o.getLength())).replace(' ', '0');
                String deltaExtBinaryString = Integer.toBinaryString(o.getDeltaExtended());
                if (deltaExtBinaryString.length() <= 8) {
                    bits = bits + String.format("%8s", deltaExtBinaryString).replace(' ', '0');
                } else {
                    bits = bits + String.format("%16s", deltaExtBinaryString).replace(' ', '0');
                }
                String lengthExtBinaryString = Integer.toBinaryString(o.getLengthExtended());
                if (lengthExtBinaryString.length() <= 8) {
                    bits = bits + String.format("%8s", lengthExtBinaryString).replace(' ', '0');
                } else {
                    bits = bits + String.format("%16s", lengthExtBinaryString).replace(' ', '0');
                }
                try {
                    if (!o.getValue().equals("")) {
                        byte[] b = o.getValue().getBytes("UTF-8");
                        for (int i = 0; i < b.length; i++) {
                            bits = bits + String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
                        }
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(CoapMessage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println(bits);
        if (payloadMarker != null) {
            bits = bits + String.format("%8s", payloadMarker.replace(' ', '0'));
        }
        System.out.println(bits);
        if (payload != null) {
            try {
                byte[] b = payload.getBytes("UTF-8");
                for (int i = 0; i < b.length; i++) {
                    bits = bits + String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(CoapMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(bits);
        return bits;
    }
}
