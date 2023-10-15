package ru.nikitavov.avenir.web.security.service.auth.oauth2;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс конвертор. Необходим для преобразования ответа от сервера авторизации в объект токена.
 * Это кастомная реализация необходимая для дополнительной обработки от сервера авторизации VK,
 * по причине того что он не возвращает тип токена
 */
@Component
public class OAuth2TokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
        String accessToken = tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
        long expiresIn = Long.parseLong(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN));


        OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

        Map<String, Object> additionalParameters = new HashMap<>(tokenResponseParameters);

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(accessTokenType)
                .expiresIn(expiresIn)
                .additionalParameters(additionalParameters)
                .build();
    }
}