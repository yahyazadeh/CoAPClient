/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coapc;

import coapc.entities.CoapMessage;
import coapc.entities.Option;
import coapc.util.CommandUtil;
import coapc.util.StringUtil;
import coapc.validators.CodeValidator;
import coapc.validators.FourBitsValidator;
import coapc.validators.MessageTypeValidator;
import coapc.validators.NonNegativeValidator;
import coapc.validators.OptionValidator;
import coapc.validators.PayloadMarkerValidator;
import coapc.validators.SixteenBitsValidator;
import coapc.validators.TokenValidator;
import coapc.validators.TwoBitsValidator;
import com.beust.jcommander.Parameter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author daniel
 */
public class Client {

    @Parameter(names = {"-ip"}, required = true, description = "Destination IP")
    private String ip;

    @Parameter(names = {"-port"}, validateWith = NonNegativeValidator.class, required = true, description = "Destination port")
    private String port;

    @Parameter(names = {"-v"}, validateWith = TwoBitsValidator.class, description = "CoAP version")
    private String version;

    @Parameter(names = {"-t"}, validateWith = MessageTypeValidator.class, description = "Message types: r|con|non|ack|rst|0~3")
    private String messageType;

    @Parameter(names = {"-tkl"}, validateWith = FourBitsValidator.class, description = "Token length")
    private String tokenLength;

    @Parameter(names = {"-c"}, validateWith = CodeValidator.class, description = "Message code: c.dd, where c is class code & dd is the detail code")
    private String code;

    @Parameter(names = {"-mid"}, validateWith = SixteenBitsValidator.class, description = "Message ID")
    private String messageId;

    @Parameter(names = {"-tk"}, validateWith = TokenValidator.class, description = "Token")
    private String token;

    @Parameter(names = {"-tkcc"}, description = "Use if you want token correlation check to be performed")
    private boolean hasTokenCC = false;

    @Parameter(names = "-op", variableArity = true, validateWith = OptionValidator.class, description = "It can have one or multiple arguments like <delta>:<length>:<deltaEx>:<lengthEx>:<value>")
    public List<String> ops = new ArrayList<>();

    @Parameter(names = {"-opcc"}, description = "Use if you want option correlation check to be performed")
    private boolean hasOptionCC = false;

    @Parameter(names = {"-pm"}, validateWith = PayloadMarkerValidator.class, description = "The value can be 'r' or and 8-bit binaray for payload marker")
    private String payloadMarker;

    @Parameter(names = {"-p"}, description = "Payload")
    private String payload;

    @Parameter(names = {"-rp"}, description = "Use if you want to use random payload")
    private boolean randomPayload = false;

    @Parameter(names = {"-rpminl"}, validateWith = NonNegativeValidator.class, description = "Specify min payload length for the case the payload is random")
    private String randomePayloadMinLength = "0";

    @Parameter(names = {"-rpmaxl"}, validateWith = NonNegativeValidator.class, description = "Specify max payload length for the case the payload is random")
    private String randomePayloadMaxLength = "40";

    @Parameter(names = {"--random-all"}, description = "Use this option if you want to randomize everything")
    private boolean random = false;

    @Parameter(names = {"--cc-all"}, description = "Use this option if you want to perform all correlation check in the case of randomization")
    private boolean correlationCheck = false;

    private int codeClass;
    private int codeDetail;
    private List<Option> options = new ArrayList();
    private String sendMsgCommand = "echo '%s' | xxd -r -p | nc -u -w1 %s %s";

    public void send() {
        CommandUtil cmd = new CommandUtil();
        preprocessParams();
        CoapMessage coapMessage = getCoapMessage();
        String binaryString = coapMessage.toBitString();   
        String hexStr = convertBinaryToHex(binaryString);
        String command = String.format(sendMsgCommand, hexStr, ip, port);
        System.out.println(cmd.executeCommand("", command, false, ""));
    }

    private void preprocessParams() {
        if (random) {
            version = String.valueOf(getRandomInt(0, 3));
            messageType = String.valueOf(getRandomInt(0, 3));
            tokenLength = String.valueOf(getRandomInt(0, 15));
            codeClass = getRandomInt(0, 7);
            codeDetail = getRandomInt(0, 31);
            messageId = String.valueOf(getRandomInt(0, 65535));
            token = new BigInteger(120, new Random()).toString();
            options = getRandomOptions();
            payloadMarker = new StringUtil().getRandomBinary(8);
            payload = new StringUtil().getRandom(((Integer.parseInt(randomePayloadMaxLength)
                    - Integer.parseInt(randomePayloadMinLength)) + 1)
                    + Integer.parseInt(randomePayloadMinLength));
            if (correlationCheck) {
                hasTokenCC = true;
                hasOptionCC = true;
            }
        } else {
            if (version.toLowerCase().equals("r")) {
                version = String.valueOf(getRandomInt(0, 3));
            }
            if (messageType.toLowerCase().equals("r")) {
                messageType = String.valueOf(getRandomInt(0, 3));
            }
            if (tokenLength.toLowerCase().equals("r")) {
                tokenLength = String.valueOf(getRandomInt(0, 15));
            }
            if (code.toLowerCase().equals("r")) {
                codeClass = getRandomInt(0, 7);
                codeDetail = getRandomInt(0, 31);
            } else {
                String[] parts = code.split("\\.");
                codeClass = Integer.parseInt(parts[0]);
                codeDetail = Integer.parseInt(parts[1]);
            }
            if (messageId.toLowerCase().equals("r")) {
                messageId = String.valueOf(getRandomInt(0, 65535));
            }
            if (token != null) {
                if (token.toLowerCase().equals("r")) {
                    token = new BigInteger(120, new Random()).toString();
                }
            }
            if (ops.contains("R") || ops.contains("r")) {
                options = getRandomOptions();
            } else {
                options.clear();
                for (String op : ops) {
                    Option option = new Option();
                    String[] parts = op.split("\\:");
                    option.setDelta(Integer.parseInt(parts[0]));
                    option.setLength(Integer.parseInt(parts[1]));
                    option.setDeltaExtended(Integer.parseInt(parts[2]));
                    option.setLengthExtended(Integer.parseInt(parts[3]));
                    option.setValue(parts[4]);
                    options.add(option);
                }
            }
            if (payloadMarker.toLowerCase().equals("r")) {
                payloadMarker = new StringUtil().getRandomBinary(8);
            }
            if (payload == null) {
                if (randomPayload) {
                    payload = new StringUtil().getRandom(((Integer.parseInt(randomePayloadMaxLength)
                            - Integer.parseInt(randomePayloadMinLength)) + 1)
                            + Integer.parseInt(randomePayloadMinLength));
                }
            }
        }
    }

    private CoapMessage getCoapMessage() {

        CoapMessage coapMessage = new CoapMessage();
        coapMessage.setVersion(Integer.parseInt(version));
        coapMessage.setMsgType(Integer.parseInt(messageType));
        coapMessage.setTklLength(Integer.parseInt(tokenLength));
        coapMessage.setCodeClass(codeClass);
        coapMessage.setCodeDetail(codeDetail);
        coapMessage.setMsgID(Integer.parseInt(messageId));
        if (token != null) {
            coapMessage.setToken(new BigInteger(token));
        }
        coapMessage.setOptions(options);
        coapMessage.setPayloadMarker(payloadMarker);
        coapMessage.setPayload(payload);
        coapMessage.setHasTokenCC(hasTokenCC);
        coapMessage.setHasOptionCC(hasOptionCC);
        return coapMessage;
    }

    private int getRandomInt(int min, int max) {
        Random rand = new Random();
        return (rand.nextInt((max - min) + 1) + min);
    }

    private List<Option> getRandomOptions() {
        List<Option> myOptions = new ArrayList();
        for (int i = 0; i < getRandomInt(0, 11); i++) {
            Option o = new Option();
            o.setDelta(getRandomInt(0, 15));
            o.setDeltaExtended(getRandomInt(0, 65535));
            o.setLength(getRandomInt(0, 15));
            o.setLengthExtended(getRandomInt(0, 65535));
            // Maximum value length could be 269 + (2^16) - 1 = 65804, 
            // In the case when option.length is 14 and option.lengthExtended is 16-bit all 1 (=65535)
            o.setValue(new StringUtil().getRandom(getRandomInt(0, 65804)));
            myOptions.add(o);
        }
        return myOptions;
    }

    private String convertBinaryToHex(String binInPut) {
        int chunkLength = binInPut.length() / 4, startIndex = 0, endIndex = 4;
        String chunkVal = null;
        String hex = "";
        for (int i = 0; i < chunkLength; i++) {
            chunkVal = binInPut.substring(startIndex, endIndex);
            hex = hex + Integer.toHexString(Integer.parseInt(chunkVal, 2));
            startIndex = endIndex;
            endIndex = endIndex + 4;
        }

        return hex;

    }
}
