package ru.nikitavov.avenir.bot.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nikitavov.avenir.web.security.service.auth.TelegramUserService;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class TelegramMessageService {

    private final TelegramBot telegramBot;
    private final TelegramSendMessageService sendMessageService;
    private final TelegramUserService telegramUserService;

    @PostConstruct
    public void init() {
        telegramBot.setTelegramMessageService(this);
    }

    public void handlerMessage(TelegramBot telegramBot, Update update) {
        if (update.getMessage().hasEntities()) {
            try {
                handlerCommand(telegramBot, update);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handlerCommand(TelegramBot telegramBot, Update update) throws TelegramApiException {
        if (update.getMessage().getEntities().size() != 1) {
//            update.getMessage().getChatId()
//            SendMessage build = SendMessage.builder().chatId().build();
//            telegramBot.execute(build)
            return;
        }
        MessageEntity entity = update.getMessage().getEntities().get(0);
        if (!entity.getType().equals("bot_command")) {
            return;
        }

        if (entity.getText().equals("/start")) {
            commandStartHandler(telegramBot, update);
            return;
        }

        if (entity.getText().equals("/login")) {
            commandLoginHandler(telegramBot, update);
            return;
        }

    }

    private void commandStartHandler(TelegramBot telegramBot, Update update) {
        Message message = update.getMessage();
        String[] strings = message.getText().split(" ");
        if (strings.length != 2) return;

        String hash = strings[1];
        telegramUserService.userInteract(update.getMessage().getChatId(), hash);
    }

    private void commandLoginHandler(TelegramBot telegramBot, Update update) {
        Message message = update.getMessage();
        String[] strings = message.getText().split(" ");
        if (strings.length != 2) return;
        String url = strings[1];
        telegramUserService.userInteract(update.getMessage().getChatId(), url);
    }

//    private SendMessage sendMessage(TelegramBot telegramBot, ) {
//
//    }

}
