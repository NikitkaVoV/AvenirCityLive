package ru.nikitavov.avenir.web.security.util;

import java.util.Date;

public class TimeUtil {

    public static Date createExpiryDate(long expireMsc) {
        return new Date(System.currentTimeMillis() + expireMsc);
    }

    public static boolean isExpired(Date expiryDate) {
        return expiryDate.before(new Date());
    }
}
