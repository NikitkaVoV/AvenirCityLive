//package ru.nikitavov.kkep.bot.telegram;
//
//import com.orgyflame.springtelegrambotapi.api.method.BotApiMethod;
//import com.orgyflame.springtelegrambotapi.api.method.send.SendMessage;
//import com.orgyflame.springtelegrambotapi.api.object.Chat;
//import com.orgyflame.springtelegrambotapi.api.object.User;
//import com.orgyflame.springtelegrambotapi.api.service.TelegramApiService;
//import com.orgyflame.springtelegrambotapi.bot.container.BotCallbackQueryContainer;
//import com.orgyflame.springtelegrambotapi.bot.mapping.BotController;
//import com.orgyflame.springtelegrambotapi.bot.mapping.BotMapping;
//import com.orgyflame.springtelegrambotapi.bot.mapping.parameter.ChatParam;
//import com.orgyflame.springtelegrambotapi.bot.mapping.parameter.FromParam;
//import com.orgyflame.springtelegrambotapi.exceptions.TelegramApiValidationException;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@BotController
//public class TelegramBotController {
//
//    private final TelegramApiService telegramApiService;
//    private final BotCallbackQueryContainer botCallbackQueryContainer;
//
//    private void send(BotApiMethod botApiMethod) {
//        try {
//            telegramApiService.sendApiMethod(botApiMethod).subscribe();
//        } catch (TelegramApiValidationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @BotMapping(value = "/start", description = "Start")
//    public void start(@FromParam User user, @ChatParam Chat chat) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chat.getId()));
//        sendMessage.setText("Hello," + user.getFirstName());
//        send(sendMessage);
//    }
//
//    @BotMapping(value = "/hello", description = "Greets the user")
//    public void hello(@FromParam User user, @ChatParam Chat chat) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chat.getId()));
//        sendMessage.setText("Hello," + user.getFirstName());
//        send(sendMessage);
//    }
//
//    @BotMapping
//    public void defaultMapping(@ChatParam Chat chat) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chat.getId()));
//        sendMessage.setText("default");
//        send(sendMessage);
//    }
//}
