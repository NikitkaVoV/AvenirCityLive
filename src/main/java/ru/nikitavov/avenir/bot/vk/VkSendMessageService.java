package ru.nikitavov.avenir.bot.vk;

import com.vk.api.sdk.client.AbstractQueryBuilder;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.bot.general.ISendMessageService;
import ru.nikitavov.avenir.general.model.tuple.Tuple2;
import ru.nikitavov.avenir.general.util.CollectionUtil;

import java.util.Collection;
import java.util.List;

@Log
@RequiredArgsConstructor
@Service
public class VkSendMessageService implements ISendMessageService {

    private final VkApiClient vk;
    private final GroupActor groupActor;

    private long lastExecutionTime = 0;

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

    public void sendBatch(List<? extends AbstractQueryBuilder> queries) {
        executeLimitedRequest(250, () -> {
            try {
                vk.execute().batch(groupActor, (List<AbstractQueryBuilder>)queries).execute();
            } catch (ApiException | ClientException e) {
                log.warning(e.getMessage());
            }
        });
    }


    @Override
    @Async
    public void sendMessage(List<Tuple2<List<String>, String>> messages) {
        List<MessagesSendQuery> sendQueries = messages.stream().map(this::createQuery).flatMap(Collection::stream).toList();
        CollectionUtil.splitList(sendQueries, 25).forEach(this::sendBatch);


    }

    private List<MessagesSendQuery> createQuery(Tuple2<List<String>, String> messageAndUsers) {
        List<Integer> userIds = messageAndUsers.param1().stream().map(Integer::valueOf).toList();
        String message = messageAndUsers.param2();
        return CollectionUtil.splitList(userIds, 100)
                .stream()
                .map(ids -> vk.messages().send(groupActor).message(message).randomId(0).userIds(ids))
                .toList();
    }
}
