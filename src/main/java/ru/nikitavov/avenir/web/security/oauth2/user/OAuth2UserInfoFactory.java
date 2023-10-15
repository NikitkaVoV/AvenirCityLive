package ru.nikitavov.avenir.web.security.oauth2.user;

import ru.nikitavov.avenir.web.security.exception.OAuth2AuthenticationProcessingException;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(SocialNetworkType.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialNetworkType.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(SocialNetworkType.VK.toString())) {
            return new VkOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.", 1);
        }
    }
}
