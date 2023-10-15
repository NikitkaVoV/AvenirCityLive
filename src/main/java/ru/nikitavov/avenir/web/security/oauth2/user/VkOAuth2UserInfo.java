package ru.nikitavov.avenir.web.security.oauth2.user;

import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;

import java.util.List;
import java.util.Map;

public class VkOAuth2UserInfo extends OAuth2UserInfo {

    @SuppressWarnings("unchecked")
    public VkOAuth2UserInfo(Map<String, Object> attributes) {
        super(((List<Map<String, Object>>)attributes.get("response")).get(0));
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return attributes.get("first_name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("username").toString() + "@telegram.com";
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public SocialNetworkType getType() {
        return SocialNetworkType.VK;
    }

}