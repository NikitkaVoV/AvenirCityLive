package ru.nikitavov.avenir.general.util.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.web.security.util.RequestHelper;

import javax.servlet.http.HttpServletRequest;

@Service
public class UrlService {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    public String buildServerCallback(String path) {
        HttpServletRequest request = RequestHelper.currentRequest();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        String fullPath = scheme + "://" + serverName;
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            fullPath += ":" + serverPort;
        }
        fullPath += contextPath;
        fullPath += path;

        return fullPath;
    }

}
