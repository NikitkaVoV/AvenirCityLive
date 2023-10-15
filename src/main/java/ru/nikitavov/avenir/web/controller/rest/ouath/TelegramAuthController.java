package ru.nikitavov.avenir.web.controller.rest.ouath;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.LinkSocialNetworkToken;
import ru.nikitavov.avenir.database.repository.realisation.LinkSocialNetworkTokenRepository;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.service.RedirectUrlService;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.service.UserService;
import ru.nikitavov.avenir.web.security.service.auth.AuthenticationService;
import ru.nikitavov.avenir.web.security.util.KeyGenerator;
import ru.nikitavov.avenir.web.security.util.TimeUtil;

import java.util.Date;

@RestController
@RequestMapping("/auth/telegram")
@RequiredArgsConstructor
public class TelegramAuthController {

    private final RedirectUrlService redirectUrlService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Value("${bot.telegram.bot-name}")
    String botName;
    private final LinkSocialNetworkTokenRepository linkSocialNetworkTokenRepository;

    @ApiResponses({
            @ApiResponse(responseCode = "304", description = "Редирект на авторизацию телегам"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - url пуст или не разрешён для использования"
            ),
    })
    @GetMapping("/login")
    public ResponseEntity<MessageWrapper<Void>> login(@RequestParam("redirectUrl") String url) {
        String hashUrl = redirectUrlService.hashUrl(url);
        if (hashUrl == null) {
            return ResponseEntity.badRequest().body(new MessageWrapper<>(null, 1));
        }

        String resultUrl = "https://t.me/%s?start=login_%s".formatted(botName, hashUrl);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", resultUrl)
                .body(null);
    }

    @GetMapping("/link")
    public ResponseEntity<MessageWrapper<Void>> link(@RequestParam("token") String tokenAccess) {

        UserPrincipal userPrincipal = authenticationService.authenticationUserByJwt(tokenAccess);
        if (userPrincipal == null) {
            return ResponseEntity.badRequest().body(new MessageWrapper<>(null, 1));
        }
        authenticationService.authenticationUser(userPrincipal);
        User user = userService.getActiveUser();

        String token = KeyGenerator.string(32);
        Date expiryDate = TimeUtil.createExpiryDate(300000); //5 min

        linkSocialNetworkTokenRepository.save(LinkSocialNetworkToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryDate)
                .build());


        String resultUrl = "https://t.me/%s?start=link_%s".formatted(botName, token);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", resultUrl)
                .body(null);
    }
}
