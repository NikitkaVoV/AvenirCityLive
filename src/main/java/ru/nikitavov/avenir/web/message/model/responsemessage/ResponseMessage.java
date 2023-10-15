package ru.nikitavov.avenir.web.message.model.responsemessage;

import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;

public record ResponseMessage(String message) implements IResponseMessage {
}
