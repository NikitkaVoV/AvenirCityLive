package ru.nikitavov.avenir.web.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    private final Cors cors = new Cors();
    private final Misc misc = new Misc();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private String tokenRefreshSecret;
        private long tokenRefreshExpirationMsec;
    }

    @Getter
    @Setter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }

    @Getter
    @Setter
    public static final class Cors {
        private ArrayList<String> allowedOrigins;
    }

    @Getter
    @Setter
    public static final class Misc {

        private final Tokens tokens = new Tokens();

        @Getter
        @Setter
        public static final class Tokens {
            private long registrationTokenExpirationMsec;
            private long changeEmailTokenExpirationMsec;
        }
    }

}
