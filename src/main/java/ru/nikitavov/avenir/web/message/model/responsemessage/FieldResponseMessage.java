package ru.nikitavov.avenir.web.message.model.responsemessage;


import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;

public record FieldResponseMessage(String fieldName, String localizedFieldName, String message) implements IResponseMessage {
}
