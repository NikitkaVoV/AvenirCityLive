package ru.nikitavov.avenir.web.controller.rest.profile;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.mail.MailChange;
import ru.nikitavov.avenir.database.repository.realisation.*;
import ru.nikitavov.avenir.general.mail.service.MailService;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.general.util.EmailUtil;
import ru.nikitavov.avenir.general.util.UrlUtil;
import ru.nikitavov.avenir.general.util.service.UrlService;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.user.profile.ProfileLinkedSocialResponse;
import ru.nikitavov.avenir.web.message.realization.user.profile.ProfileResponse;
import ru.nikitavov.avenir.web.message.realization.user.profile.update.*;
import ru.nikitavov.avenir.web.security.data.PermissionAuthorize;
import ru.nikitavov.avenir.web.security.permission.Permissions;
import ru.nikitavov.avenir.web.security.service.UserService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
    private final UserService userService;
    private final MailService mailService;
    private final UrlService urlService;

    @Transactional
    @GetMapping("/me")
    @PermissionAuthorize(Permissions.PROFILE_ME)
    public ResponseEntity<MessageWrapper<ProfileResponse>> getMyProfile() {
        User user = userService.getActiveUser();

        ArrayList<ProfileLinkedSocialResponse> linkedSocialResponses = new ArrayList<>();

        Map<SocialNetworkType, LinkedSocialNetwork> mapSocial = user.getLinkedSocialNetworks().stream()
                .collect(Collectors.toMap(LinkedSocialNetwork::getSocialNetworkType,
                        linkedSocialNetwork -> linkedSocialNetwork, (a, b) -> b));

        for (SocialNetworkType value : SocialNetworkType.values()) {
            LinkedSocialNetwork socialNetwork = mapSocial.get(value);
            ProfileLinkedSocialResponse socialResponse;
            if (socialNetwork == null) {
                socialResponse = new ProfileLinkedSocialResponse(value, false, value.isNeedMailing(), false);
            } else {
                socialResponse = new ProfileLinkedSocialResponse(value, true, value.isNeedMailing(), socialNetwork.getEnabledMailing());
            }
            linkedSocialResponses.add(socialResponse);
        }

        boolean containPassword = StringUtils.hasText(user.getPassword());

        ProfileResponse response = new ProfileResponse(linkedSocialResponses, containPassword,
                user.getEmail(), user.getName(), user.getSurname(), user.getPatronymic(), user.getUuid());
        return ResponseEntity.ok(MessageWrapper.create(response));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - почта пуста;" +
                    "<br>2 - строка не является почтой;" +
                    "<br>3 - почта занята;" +
                    "<br>4 - redirectUrl пуст или не является ссылкой"
            ),
    })
    @PostMapping("/update/mail")
    @PermissionAuthorize(Permissions.PROFILE_UPDATING_MAIL)
    public ResponseEntity<MessageWrapper<Void>> updateMail(@RequestBody ProfileUpdateMailRequest request) {
        String mail = request.mail();
        if (!StringUtils.hasText(mail)) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }
        if (!EmailUtil.isEmail(mail)) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }
        if (userRepository.existsByEmail(mail)) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }
        if (!StringUtils.hasText(request.redirectUrl()) || !UrlUtil.isValidHttpUrl(request.redirectUrl())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 4));
        }

        MailChange mailChange = userService.changeMail(request.mail(), request.redirectUrl());

        Context context = new Context();
        context.setVariable("tokenUrl", urlService.buildServerCallback("/mail/callback/mail_change/" + mailChange.getToken()));

        mailService.sendEmail(request.mail(), "Изменение почты", "emailChange", context);

        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - старый пароль пуст;" +
                    "<br>2 - новый пароль пуст;" +
                    "<br>3 - старый пароль не совпадает;" +
                    "<br>4 - пароль совпадает с предыдущим"),
    })
    @PostMapping("/update/password")
    @PermissionAuthorize(Permissions.PROFILE_UPDATING_PASSWORD)
    public ResponseEntity<MessageWrapper<Void>> updatePassword(@RequestBody ProfileUpdatePasswordRequest request) {
        User user = userService.getActiveUser();

        String newPassword = request.newPassword();
        if (!StringUtils.hasText(newPassword)) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }
        String oldPassword = request.oldPassword();
        if (!StringUtils.hasText(oldPassword)) {
            if (!StringUtils.hasText(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);

                return ResponseEntity.ok(MessageWrapper.create(null, 0));
            }
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 4));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит пустое тело и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - тип пуст;" +
                    "<br>2 - соц сеть с таким типом не привязана;" +
                    "<br>3 - это последний вид входа в систему (привяжите другую соц сеть или добавьте логин и пароль)"),
    })
    @PostMapping("/update/remove_linked_network")
    @PermissionAuthorize(Permissions.PROFILE_UPDATING_REMOVE_LINKED_NETWORK)
    public ResponseEntity<MessageWrapper<Void>> updateLinkedNetwork(@RequestBody ProfileRemoveLinkedNetworkRequest request) {
        User user = userService.getActiveUser();

        SocialNetworkType type = request.type();
        if (type == null) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }

        Optional<LinkedSocialNetwork> networkTypeOptional = linkedSocialNetworkRepository.findByUser_IdAndSocialNetworkType(user.getId(), type);
        if (networkTypeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }

        long count = linkedSocialNetworkRepository.countByUser_Id(user.getId());
        if (count <= 1 && (!StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword()))) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));
        }

        linkedSocialNetworkRepository.deleteById(networkTypeOptional.get().getId());

        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит тело с новой информацией о рассылке и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - тип пуст;" +
                    "<br>2 - соц сеть с таким типом не привязана"),
    })
    @PostMapping("/update/mailing")
    @PermissionAuthorize(Permissions.PROFILE_UPDATING_MAILING)
    public ResponseEntity<MessageWrapper<ProfileUpdateMailingResponse>> updateMailing(@RequestBody ProfileUpdateMailingRequest request) {
        User user = userService.getActiveUser();

        SocialNetworkType type = request.type();
        if (type == null) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        }

        Optional<LinkedSocialNetwork> networkTypeOptional = linkedSocialNetworkRepository.findByUser_IdAndSocialNetworkType(user.getId(), type);
        if (networkTypeOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        }

        LinkedSocialNetwork socialNetwork = networkTypeOptional.get();
        socialNetwork.setEnabledMailing(!socialNetwork.getEnabledMailing());
        socialNetwork = linkedSocialNetworkRepository.save(socialNetwork);

        return ResponseEntity.ok(MessageWrapper.create(new ProfileUpdateMailingResponse(socialNetwork.getEnabledMailing()), 0));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит тело с новой информацией о рассылке и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - имя пусто;" +
                    "<br>2 - фамилия пуста;" +
                    "<br>3 - отчество пусто"),
    })
    @PostMapping("/update/fullname")
    @PermissionAuthorize(Permissions.PROFILE_UPDATING_NAME)
    public ResponseEntity<MessageWrapper<Void>> updateFullName(@RequestBody ProfileUpdateFullNameRequest request) {
        User user = userService.getActiveUser();

        if (!StringUtils.hasText(request.name()))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 1));
        if (!StringUtils.hasText(request.surname()))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 2));
        if (!StringUtils.hasText(request.patronymic()))
            return ResponseEntity.badRequest().body(MessageWrapper.create(null, 3));

        user.setName(request.name());
        user.setSurname(request.surname());
        user.setPatronymic(request.patronymic());

        userRepository.save(user);

        return ResponseEntity.ok(MessageWrapper.create(null, 0));
    }

}
