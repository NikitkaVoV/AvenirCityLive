package ru.nikitavov.avenir.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    public final VkBot vk = new VkBot();
    public final TelegramBot telegram = new TelegramBot();

    @Getter
    @Setter
    public static class VkBot {
        private int clientId;
        private String clientSecret;
        private int groupId;
        private String groupAccess;
    }

    @Getter
    @Setter
    public static class TelegramBot {
        private String token;
    }
}
