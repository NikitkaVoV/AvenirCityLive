package ru.nikitavov.avenir.web.message.realization.user.auth;

import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public record TokensResponse(String accessToken, String refreshToken, long expiry) implements IResponse {

    @Override
    public HttpStatus defaultStatus() {
        return HttpStatus.OK;
    }
}
