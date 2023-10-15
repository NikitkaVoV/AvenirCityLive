package ru.nikitavov.avenir.web.data.action.realization;

import lombok.Data;
import org.springframework.http.HttpStatus;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;
import ru.nikitavov.avenir.web.data.action.ActionResultType;
import ru.nikitavov.avenir.web.data.action.IResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ActionResult<E> implements IResult<E> {
    protected E data;
    protected ActionResultType actionType = ActionResultType.OK;
    protected HttpStatus customStatus;
    protected List<IResponseMessage> messages = new ArrayList<>();

    public ActionResult(E data) {
        this.data = data;
    }

    public ActionResult(E data, ActionResultType actionType, IResponseMessage... messages) {
        this.data = data;
        this.actionType = actionType;
        this.messages.addAll(Arrays.asList(messages));
    }

    public ActionResult(E data, ActionResult<?> actionResult) {
        this.data = data;
        this.actionType = actionResult.getActionType();
        this.messages.addAll(actionResult.getMessages());
    }

    public boolean hasCustomStatus() {
        return customStatus != null;
    }

    @Override
    public boolean isOk() {
        return data != null || statusOk();
    }

    @Override
    public boolean isServerError() {
        return getActualStatus().is5xxServerError();
    }

    @Override
    public HttpStatus getActualStatus() {
        return hasCustomStatus() ? customStatus : actionType.getDefaultStatus();
    }

    public boolean statusOk() {
        if (customStatus != null) return customStatus.is2xxSuccessful();
        return actionType.getDefaultStatus().is2xxSuccessful();
    }

    public void addMessage(IResponseMessage message) {
        messages.add(message);
    }

    public void setActionType(ActionResultType actionType) {
        this.actionType = actionType;
        customStatus = null;
    }
}
