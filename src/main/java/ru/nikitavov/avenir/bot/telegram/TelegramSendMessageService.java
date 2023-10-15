package ru.nikitavov.avenir.bot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.nikitavov.avenir.bot.general.ISendMessageService;
import ru.nikitavov.avenir.database.model.base.LinkedSocialNetwork;
import ru.nikitavov.avenir.database.repository.realisation.LinkedSocialNetworkRepository;
import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.general.model.tuple.Tuple2;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Log
@RequiredArgsConstructor
@Service
public class TelegramSendMessageService implements ISendMessageService {
    private final LinkedSocialNetworkRepository linkedSocialNetworkRepository;
    private final BlockingQueue<SendMessage> messageQueue = new LinkedBlockingQueue<>();
    private final TelegramBot telegramBot;

    private long lastExecutionTime = 0;

    @PostConstruct
    public void startMessageProcessorThread() {
        Thread messageProcessorThread = new Thread(() -> {
            int limitMs = 35;/*28 req/s*/
            while (true) {
                try {
                    SendMessage message = messageQueue.take(); // Блокирующее ожидание сообщения
                    long currentTime = System.currentTimeMillis();
                    long timeSinceLastExecution = currentTime - lastExecutionTime;

                    if (timeSinceLastExecution < limitMs) {
                        // Вычисляем оставшееся время ожидания
                        long remainingWaitTime = limitMs - timeSinceLastExecution;

                        // Ожидаем оставшееся время
                        try {
                            Thread.sleep(remainingWaitTime);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    try {
                        Message execute = telegramBot.execute(message);
                        lastExecutionTime = System.currentTimeMillis();
                    } catch (TelegramApiException e) {
                        if (e instanceof TelegramApiRequestException re) {
                            if (re.getErrorCode().equals(403)) {
                                Optional<LinkedSocialNetwork> socialNetwork = linkedSocialNetworkRepository.findByUserSocialNetworkIdAndSocialNetworkType(message.getChatId(), SocialNetworkType.TELEGRAM);
                                if (socialNetwork.isEmpty()) {
                                    continue;
                                }
                                socialNetwork.get().setEnabledMailing(false);
                                linkedSocialNetworkRepository.save(socialNetwork.get());
                            }
                        }
                        log.warning(e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // Прерываем поток при прерывании
                }
            }
        });
        messageProcessorThread.start();
    }

    public void executeLimitedRequest(int limitMs, Runnable execute) {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastExecution = currentTime - lastExecutionTime;

        if (timeSinceLastExecution < limitMs) {
            // Вычисляем оставшееся время ожидания
            long remainingWaitTime = limitMs - timeSinceLastExecution;

            // Ожидаем оставшееся время
            try {
                Thread.sleep(remainingWaitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        execute.run();

        lastExecutionTime = System.currentTimeMillis();
    }

    public void sendBatch(SendMessage query) {
        executeLimitedRequest(35 /*28 req/s*/, () -> {
            try {
                telegramBot.execute(query);
            } catch (TelegramApiException e) {
                log.warning(e.getMessage());
            }
        });
    }

    @Override
    public void sendMessage(List<Tuple2<List<String>, String>> messages) {
        messages.stream().flatMap(m -> m.param1().stream().map(cId -> new SendMessage(cId, m.param2()))).forEach(messageQueue::add);
    }

    public void sendMessage(String chatId, String message) {
        messageQueue.add(new SendMessage(chatId, message));
    }
    public void sendMessage(SendMessage message) {
        messageQueue.add(message);
    }
}
