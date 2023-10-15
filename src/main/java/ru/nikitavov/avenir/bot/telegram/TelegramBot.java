package ru.nikitavov.avenir.bot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.AfterBotRegistration;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private TelegramMessageService telegramMessageService;
    @Value("${bot.telegram.bot-name}")
    String name;

    public TelegramBot(@Value("${bot.telegram.token}") String token) {
        super(token);
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramMessageService.handlerMessage(this, update);
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    public void setTelegramMessageService(TelegramMessageService telegramMessageService) {
        this.telegramMessageService = telegramMessageService;
    }

    @AfterBotRegistration
    public void afterBotRegistration() {
    }


}