package ru.nikitavov.avenir.web.controller.rest.ouath;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.RefreshToken;
import ru.nikitavov.avenir.database.model.security.mail.TempUser;
import ru.nikitavov.avenir.database.repository.realisation.RefreshTokenMyRepository;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.general.mail.service.MailService;
import ru.nikitavov.avenir.general.model.tuple.Tuple3;
import ru.nikitavov.avenir.general.util.EmailUtil;
import ru.nikitavov.avenir.general.util.UrlUtil;
import ru.nikitavov.avenir.general.util.service.UrlService;
import ru.nikitavov.avenir.general.util.string.HashUtil;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.user.auth.LoginRequest;
import ru.nikitavov.avenir.web.message.realization.user.auth.RefreshRequest;
import ru.nikitavov.avenir.web.message.realization.user.auth.SignUpRequest;
import ru.nikitavov.avenir.web.message.realization.user.auth.TokensResponse;
import ru.nikitavov.avenir.web.security.data.UserPrincipal;
import ru.nikitavov.avenir.web.security.service.UserPrincipalService;
import ru.nikitavov.avenir.web.security.service.UserService;
import ru.nikitavov.avenir.web.security.service.auth.AuthenticationService;
import ru.nikitavov.avenir.web.security.service.auth.TokenProvider;
import ru.nikitavov.avenir.web.security.util.TimeUtil;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserPrincipalService principalService;
    private final RefreshTokenMyRepository refreshTokenRepository;
    private final UserService userService;
    private final MailService mailService;
    private final UrlService urlService;

    private TokensResponse createTokens() {
        Tuple3<String, String, Date> tokens = tokenProvider.createTokens();
        return new TokensResponse(tokens.param1(), tokens.param2(), tokens.param3().getTime());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит токены и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - почта пуста или email не почта;" +
                    "<br>2 - юзер не найден;" +
                    "<br>3 - пароль не совпадает"
            ),
    })
    @PostMapping("/login")
    public ResponseEntity<MessageWrapper<TokensResponse>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        if (!StringUtils.hasText(loginRequest.email()))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.email());
        if (optionalUser.isEmpty())
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));

        if (!passwordEncoder.matches(loginRequest.password(), optionalUser.get().getPassword())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }

        UserPrincipal principal = principalService.create(optionalUser.get());
        authenticationService.authenticationUser(principal);

        return ResponseEntity.ok(new MessageWrapper<>(createTokens()));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - почта пуста или email не почта;" +
                    "<br>2 - пароль пуст;" +
                    "<br>3 - redirectUrl пуст или не является ссылкой;" +
                    "<br>4 - почта занята"
            ),
    })
    @PostMapping("/signup")
    public ResponseEntity<MessageWrapper<Void>> registerUser(@Valid @RequestBody SignUpRequest request) {
        if (!StringUtils.hasText(request.email()) || !EmailUtil.isEmail(request.email())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }
        if (!StringUtils.hasText(request.password())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }
        if (!StringUtils.hasText(request.redirectUrl()) || !UrlUtil.isValidHttpUrl(request.redirectUrl())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 4));
        }

        String password = passwordEncoder.encode(request.password());
        TempUser tempUser = userService.startRegistration(request.email(), password, request.redirectUrl());

        Context context = new Context();
        context.setVariable("tokenUrl", urlService.buildServerCallback("/mail/callback/signup/" + tempUser.getToken()));

        mailService.sendEmail(request.email(), "Регистрация нового пользователя", "emailRegistration", context);

        return ResponseEntity.ok(new MessageWrapper<>(null));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит тело с токенами и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - токен не найден в БД;" +
                    "<br>2 - время жизни токена истекло"
            ),
    })
    @PostMapping("/refresh")
    @Transactional
    public ResponseEntity<MessageWrapper<TokensResponse>> refreshTokens(@RequestBody RefreshRequest request) {
        if (!StringUtils.hasText(request.refreshToken())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }
        String refreshTokenHash = HashUtil.sha256(request.refreshToken());
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findFirstByRefreshTokenHash(refreshTokenHash);

        if (optionalRefreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }

        RefreshToken refreshToken = optionalRefreshToken.get();
        if (TimeUtil.isExpired(refreshToken.getRefreshExpiryDate())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }

        UserPrincipal principal = principalService.create(refreshToken.getUser());
        authenticationService.authenticationUser(principal);

        refreshTokenRepository.delete(refreshToken);

        return ResponseEntity.ok(new MessageWrapper<>(createTokens()));
    }

}
