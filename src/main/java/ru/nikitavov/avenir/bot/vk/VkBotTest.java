package ru.nikitavov.avenir.bot.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import java.util.Random;

public class VkBotTest {

    private static final Integer GROUP_ID = 220122071;
    private static final String ACCESS_TOKEN = "vk1.a.fzl7v-HvT2OmEzC1w39HIUtUxWgoVqFt10Lgj--1TVy5R9RS9ak5ArV7VNAYia2AHDDKxOB3qtDGJ41I1QWXCnEEzKDiHFui9pfg8LcWqpCBOtgFpVKsDbgfV2Nvv4Rbgl5Xf_3D2ZfKMZ9p7AwveF9xRocAA4ep-KLcehg0La6fMtGkuUKVtVUGbNHAzsWDhjFsmqP_Bt1DmmMaOUlQ7A";
    private static final GroupActor ACTOR = new GroupActor(GROUP_ID, ACCESS_TOKEN);
    private static final VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

    public static void main(String[] args) throws ClientException, ApiException {

        vk.oAuth()
                .serviceClientCredentialsFlow(51754481, "wH6gLxHnqWPsizCszGLE")
                .execute();

//        vk.messages().send(ACTOR)
//                .message("Hello, World!")
//                .userId(174063987)
//                .randomId(new Random().nextInt(100000000))
//                .execute();
        MessagesSendQuery messagesSendQuery = vk.messages().send(ACTOR)
                .message("Hello, World!")
                .userId(174063987)
                .randomId(new Random().nextInt(100000000));

        vk.execute().batch(ACTOR, messagesSendQuery).execute();

//        UserActor actor = new UserActor(1, "TOKEN");

        String code = "return [" +
                "API.messages.send({\"user_id\":174063987, \"message\":\"Hello from Java #1!\"})," +
                "API.messages.send({\"user_id\":174063987, \"message\":\"Hello from Java #2!\"})" +
                "];";
//        JsonElement execute = vk.execute().code(ACTOR, code).execute().;

//        List<SendResponse> responses = vk.execute().code(actor, code).execute();
//        for (SendResponse response : responses) {
//            Message message = response.getMessage();
//            System.out.println("Sent message with id: " + message.getId() + ", text: " + message.getText());
//        }

        System.out.println();
    }
}
