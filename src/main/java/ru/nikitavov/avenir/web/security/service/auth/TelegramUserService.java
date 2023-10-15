package ru.nikitavov.avenir.web.security.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.nikitavov.avenir.bot.telegram.TelegramSendMessageService;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.LinkSocialNetworkToken;
import ru.nikitavov.avenir.database.model.security.RedirectUrl;
import ru.nikitavov.avenir.database.repository.RedirectUrlRepository;
import ru.nikitavov.avenir.database.repository.realisation.LinkSocialNetworkTokenRepository;
import ru.nikitavov.avenir.database.repository.realisation.LinkedSocialNetworkRepository;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.web.security.service.UserService;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TelegramUserService {
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
    private final LinkSocialNetworkTokenRepository linkSocialNetworkTokenRepository;
    private final RedirectUrlRepository redirectUrlRepository;

    private final UserService userService;
    private final TelegramSendMessageService sendMessageService;
    private final AuthenticationService authenticationService;

    public void userInteract(Long chatId, String param) {
        if (param.startsWith("login_")) {
            userLogin(chatId, param);
        } else if (param.startsWith("link_")) {
            userLink(chatId, param);
        }
    }

    private void userLogin(Long chatId, String param) {
        try {
            String hash = param.substring(6);
            Optional<RedirectUrl> urlOptional = redirectUrlRepository.findByHash(hash);
            if (urlOptional.isEmpty()) {
                sendMessageService.sendMessage(chatId.toString(), "Не удалось авторизоваться, попробуйте ещё раз!");
                return;
            }
            Optional<LinkedSocialNetwork> networkOptional = linkedSocialNetworkRepository
                    .findByUserSocialNetworkIdAndSocialNetworkType(chatId.toString(), SocialNetworkType.TELEGRAM);

            User user;
            if (networkOptional.isEmpty()) {
                user = userService.registryUser(SocialNetworkType.TELEGRAM, chatId.toString());
            } else {
                user = networkOptional.get().getUser();
            }

            String url = authenticationService.callbackUrlAfterAuthByUrl(user, urlOptional.get().getUrl());
            boolean isHttps = url.startsWith("https");
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы успешно авторизовались на сайте, воспользуйтесь кнопкой для перехода на сайт!\nНикому не передавайте ссылку из сообщения, во избежания потери доступа!")
                    .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(InlineKeyboardButton.builder().text("Перейти на сайт").url(url).build()))))
                    .protectContent(true)
                    .build();
            sendMessageService.sendMessage(message);
            if (!isHttps) {
                sendMessageService.sendMessage(chatId.toString(), url);
            }
        } catch (Exception e) {
            log.error(e);
            sendMessageService.sendMessage(chatId.toString(), "При авторизации произошла ошибка!");
        }
    }

    private void userLink(Long chatId, String param) {
        try {
            String token = param.substring(5);
            Optional<LinkSocialNetworkToken> networkTokenOptional = linkSocialNetworkTokenRepository.findByToken(token);
            if (networkTokenOptional.isEmpty()) {
                sendMessageService.sendMessage(chatId.toString(), "Не удалось найти запрос на привязку, попробуйте ещё раз!");
                return;
            }
            LinkSocialNetworkToken linkSocialNetworkToken = networkTokenOptional.get();
            linkSocialNetworkTokenRepository.delete(networkTokenOptional.get());

            Optional<LinkedSocialNetwork> networkOptional = linkedSocialNetworkRepository
                    .findByUserSocialNetworkIdAndSocialNetworkType(chatId.toString(), SocialNetworkType.TELEGRAM);

            if (networkOptional.isPresent()) {
                sendMessageService.sendMessage(chatId.toString(), "Вы уже имеет привязанный аккаунт!");
                return;
            }

            User user = linkSocialNetworkToken.getUser();
            Optional<LinkedSocialNetwork> networkOptional2 = linkedSocialNetworkRepository
                    .findByUser_IdAndSocialNetworkType(user.getId(), SocialNetworkType.TELEGRAM);

            if (networkOptional2.isPresent()) {
                sendMessageService.sendMessage(chatId.toString(), "Данный телеграм уже привязан к другому аккаунту!");
                return;
            }

            userService.addSocialNetwork(SocialNetworkType.TELEGRAM, chatId.toString(), user);
            sendMessageService.sendMessage(chatId.toString(), "Вы успешно привязали аккаунт к телеграмму!\nТеперь мы можете авторизоваться на сайте через telegram");

        } catch (Exception e) {
            log.error(e);
            sendMessageService.sendMessage(chatId.toString(), "При привязке аккаунта произошла ошибка!");
        }
    }
}
