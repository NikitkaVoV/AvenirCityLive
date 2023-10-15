package ru.nikitavov.avenir.bot.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nikitavov.avenir.bot.BotProperties;

@RequiredArgsConstructor
@Configuration
public class VkBotConfig {

    private final BotProperties properties;

    @Bean
    public VkApiClient vkApiClient() {
        VkApiClient client = new VkApiClient(HttpTransportClient.getInstance());

        try {
            client.oAuth().serviceClientCredentialsFlow(properties.vk.getClientId(), properties.vk.getClientSecret()).execute();
        } catch (ApiException e) {
            throw new RuntimeException("Failed to login through the service, please check the data:", e);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        return client;
    }

    @Bean
    public GroupActor groupActor() {
        return new GroupActor(properties.vk.getGroupId(), properties.vk.getGroupAccess());
    }

}
