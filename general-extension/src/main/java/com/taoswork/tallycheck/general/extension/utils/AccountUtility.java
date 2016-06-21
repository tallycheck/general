package com.taoswork.tallycheck.general.extension.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class AccountUtility {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    //      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    /**
     * Matches:     Example:
     * +####        +86
     * (####)       86
     * 00####       0086
     */
   public static final Pattern VALID_PHONE_COUNTRY_CODE =
            Pattern.compile("^("+
                "(\\+\\d{1,4})|" +     //  +86
                "(\\(\\d{1,4}\\))|"+    //(86)
                "(\\(\\+\\d{1,4}\\))|"+    //(+86)
                "(00\\d{1,4}(\\s|\\-))|"+    //0086-
                "(\\(00\\d{1,4}\\))"+    // (0086)
                ")");
    public static final Pattern VALID_PHONE_IN_COUNTRY_NUMBER_HAVING_DIGITAL_ONLY =
            Pattern.compile("^\\d{8,16}$");

    public static final String VALID_PHONE_NUMBERS[] = {
        "+xx xxxxxxxx",
        "(xx) xxxxxxxx",
        "(+xx) xxxxxxxx",
        "00xx xxxxxxxx",
        "(00xx) xxxxxxxx",
    };
    public static final String VALID_PHONE_NUMBERS_STRING = "+xx xxxxxxxx, (+xx) xxxxxxxx, (00xx) xxxxxxxx";

    public static boolean isEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private static String makeStandardCountryCode(String str){
        String code = str.trim()
            .replaceAll(" ", "")
            .replaceAll("\\+", "")
            .replaceAll("-", "")
            .replaceAll("\t", "")
            .replaceAll("\\((.*?)\\)", "$1");
        if(!code.startsWith("00")){
            code = "00" + code;
        }
        return code;
    }

    private static String phoneNumberTrimSpaces(String str){
        return str.trim()
                .replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll("\t", "")
                .replaceAll("\\((.*?)\\)", "$1");
    }

    public static String makeStandardPhoneNumber(String phoneNumber){
        String countryCode = "";
        String localCodeRaw = phoneNumber;
        Matcher matcher = VALID_PHONE_COUNTRY_CODE.matcher(phoneNumber);
        if(matcher.find()){
            String tempCountryCode = matcher.group();
            localCodeRaw = phoneNumber.substring(tempCountryCode.length());
            countryCode = makeStandardCountryCode(tempCountryCode);
        }
        localCodeRaw = phoneNumberTrimSpaces(localCodeRaw);
        Matcher matcherLocalNumber = VALID_PHONE_IN_COUNTRY_NUMBER_HAVING_DIGITAL_ONLY.matcher(localCodeRaw);
        if(matcherLocalNumber.find()){
            String localPhoneNumber = matcherLocalNumber.group();
            return "".equals(countryCode) ? localPhoneNumber : countryCode + "-" + localPhoneNumber;
        }
        return "";
    }

    public static boolean isPhone(String mobile) {
        String standardPhoneNumber = makeStandardPhoneNumber(mobile);
        return !("".equals(standardPhoneNumber));
    }
}
