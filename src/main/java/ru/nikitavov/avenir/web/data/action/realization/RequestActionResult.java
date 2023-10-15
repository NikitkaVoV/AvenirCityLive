package ru.nikitavov.avenir.web.data.action.realization;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

@Getter
public class RequestActionResult<RE extends IResponse, E> extends ActionResult<E> {
    private final RE response;

    public RequestActionResult(RE response, ActionResult<E> result) {
        super(result.data);
        this.response = response;
        this.actionType = result.actionType;
        this.customStatus = result.customStatus;
        this.messages = result.messages;
    }

    public ResponseEntity<MessageWrapper<RE>> toDefaultResponse() {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(getActualStatus());
        return builder.body(new MessageWrapper<>(getResponse(), getMessages()));
    }
}
