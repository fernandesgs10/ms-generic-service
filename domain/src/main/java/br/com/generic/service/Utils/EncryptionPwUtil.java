package br.com.generic.service.Utils;

import java.nio.charset.StandardCharsets;

public final class EncryptionPwUtil {

    private EncryptionPwUtil() {}

    public static byte[] base64ToByte(String str) {
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        return decoder.decode(str);
    }

    public static String encrypt(String str) {
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        return encoder.encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String encstr) {
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(encstr);
        return new String(decodedByteArray);
    }

}