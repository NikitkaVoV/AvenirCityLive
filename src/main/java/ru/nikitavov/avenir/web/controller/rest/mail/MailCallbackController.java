package ru.nikitavov.avenir.web.controller.rest.mail;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.mail.IMailEntity;
import ru.nikitavov.avenir.database.model.security.mail.MailChange;
import ru.nikitavov.avenir.database.model.security.mail.TempUser;
import ru.nikitavov.avenir.database.repository.interfaces.IMailEntityRepository;
import ru.nikitavov.avenir.database.repository.realisation.MailChangeRepository;
import ru.nikitavov.avenir.database.repository.realisation.TempUserRepository;
import ru.nikitavov.avenir.database.repository.realisation.UserRepository;
import ru.nikitavov.avenir.general.model.tuple.Tuple2;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.security.service.UserPrincipalService;
import ru.nikitavov.avenir.web.security.service.UserService;
import ru.nikitavov.avenir.web.security.service.auth.AuthenticationService;
import ru.nikitavov.avenir.web.security.util.TimeUtil;

import java.util.Optional;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("mail/callback")
public class MailCallbackController {

    private final TempUserRepository tempUserRepository;
    private final MailChangeRepository mailChangeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserPrincipalService principalService;

    private <E extends IMailEntity, R extends JpaRepository<E, ?> & IMailEntityRepository<E>>
    Tuple2<E, ResponseEntity<MessageWrapper<Void>>> checkToken(String token, R repository) {

        Optional<E> tempOptional = repository.findByToken(token);
        if (tempOptional.isEmpty()) {
            return new Tuple2<>(null, ResponseEntity.badRequest().body(MessageWrapper.create(null, 1)));
        }

        E temp = tempOptional.get();
        repository.delete(temp);
        if (TimeUtil.isExpired(temp.getExpiryDate())) {
            return new Tuple2<>(null, ResponseEntity.badRequest().body(MessageWrapper.create(null, 2)));
        }

        return new Tuple2<>(temp, null);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - токен не найден в БД;" +
                    "<br>2 - время жизни токена истекло;" +
                    "<br>3 - аккаунт уже зарегистрирован"
            ),
    })
    @GetMapping("/signup/{token}")
    public ResponseEntity<MessageWrapper<Void>> registrationCallback(@PathVariable String token) {
        Tuple2<TempUser, ResponseEntity<MessageWrapper<Void>>> tuple = checkToken(token, tempUserRepository);
        if (tuple.param2() != null) {
            return tuple.param2();
        }

        TempUser tempUser = tuple.param1();
        if (userRepository.existsByEmail(tempUser.getEmail())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }

        User user = userService.registryUser(tempUser.getEmail(), tempUser.getPassword());

        String url = authenticationService.callbackUrlAfterAuthByUrl(user, tempUser.getRedirectUrl());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", url)
                .body(null);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - токен не найден в БД;" +
                    "<br>2 - время жизни токена истекло"
            ),
    })
    @GetMapping("/mail_change/{token}")
    public ResponseEntity<MessageWrapper<Void>> mailChangeCallback(@PathVariable String token) {
        Tuple2<MailChange, ResponseEntity<MessageWrapper<Void>>> tuple = checkToken(token, mailChangeRepository);
        if (tuple.param2() != null) {
            return tuple.param2();
        }

        MailChange mailChange = tuple.param1();
        if (userRepository.existsByEmail(mailChange.getEmail())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }

        mailChange.getUser().setEmail(mailChange.getEmail());

        String url = authenticationService.callbackUrlAfterAuthByUrl(mailChange.getUser(), mailChange.getRedirectUrl());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", url)
                .body(null);
    }
}
