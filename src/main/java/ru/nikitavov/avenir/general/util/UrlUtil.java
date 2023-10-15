package ru.nikitavov.avenir.general.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {
    public static boolean isValidHttpUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            return "http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
