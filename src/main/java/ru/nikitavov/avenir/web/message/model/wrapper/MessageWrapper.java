package ru.nikitavov.avenir.web.message.model.wrapper;

import lombok.Data;
import ru.nikitavov.avenir.general.util.CollectionUtil;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;

import java.util.Arrays;
import java.util.List;

@Data
public class MessageWrapper<R> {
    private final R response;
    private final List<IResponseMessage> messages;
    private final int code;

    public MessageWrapper(R response, List<IResponseMessage> messages) {
        this.response = response;
        this.messages = messages;
        this.code = 0;
        CollectionUtil.clearOfNull(this.messages);
    }
    public MessageWrapper(R response, IResponseMessage... messages) {
        this.response = response;
        this.messages = Arrays.asList(messages);
        this.code = 0;
        CollectionUtil.clearOfNull(this.messages);
    }

    public MessageWrapper(R response, int code, List<IResponseMessage> messages) {
        this.response = response;
        this.messages = messages;
        this.code = code;
        CollectionUtil.clearOfNull(this.messages);
    }

    public MessageWrapper(R response, int code, IResponseMessage... messages) {
        this.response = response;
        this.messages = Arrays.asList(messages);
        this.code = code;
        CollectionUtil.clearOfNull(this.messages);
    }

    public static <R> MessageWrapper<R> create(R response, List<IResponseMessage> messages) {
        return new MessageWrapper<>(response, messages);
    }

    public static <R> MessageWrapper<R> create(R response, IResponseMessage... messages) {
        return new MessageWrapper<>(response, messages);
    }

    public static <R> MessageWrapper<R> create(R response, int code, List<IResponseMessage> messages) {
        return new MessageWrapper<>(response, code, messages);
    }

    public static <R> MessageWrapper<R> create(R response, int code, IResponseMessage... messages) {
        return new MessageWrapper<>(response, code, messages);
    }
}
