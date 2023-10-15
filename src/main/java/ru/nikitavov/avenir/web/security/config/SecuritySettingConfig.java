package ru.nikitavov.avenir.web.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@RequiredArgsConstructor
@Configuration
public class SecuritySettingConfig {

//    @Bean("clientRegistrationRepositoryCustom")
//    public InMemoryClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
//        List<ClientRegistration> registrations = new ArrayList<>(OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
//        registrations.add(vkClientRegistration());
//
//        return new InMemoryClientRegistrationRepository(registrations);
//    }

    private ClientRegistration vkClientRegistration() {
        return ClientRegistration.withRegistrationId("vk")
                .clientId("51619094")
                .clientSecret("FICW27I3bARoAKajm7cl")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/oauth2/callback/{registrationId}")
                .scope("email")
                .authorizationUri("https://oauth.vk.com/authorize?v=5.131")
                .tokenUri("https://oauth.vk.com/access_token")
                .userInfoUri("https://api.vk.com/method/users.get?{user_id}&fields=bdate&v=5.131")
                .userNameAttributeName("response")
                .clientName("VK")
                .build();
    }

}
