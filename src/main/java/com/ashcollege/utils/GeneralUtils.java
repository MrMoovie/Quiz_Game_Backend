package com.ashcollege.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class GeneralUtils {
    private static final int OTP_MIN_VALUE = 100000;
    private static final int OTP_MAX_VALUE = 999999;



    public static String hashMd5 (String username, String password) {
        String source = username + password;
        try {
            return DatatypeConverter.printHexBinary( MessageDigest.getInstance("MD5").digest(source.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateOtp() {
        Random random = new Random();
        return String.valueOf(random.nextInt(OTP_MIN_VALUE, OTP_MAX_VALUE ));
    }




}
