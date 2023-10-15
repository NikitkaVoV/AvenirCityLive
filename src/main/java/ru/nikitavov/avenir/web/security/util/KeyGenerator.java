package ru.nikitavov.avenir.web.security.util;


import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String string(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
