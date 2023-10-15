package ru.nikitavov.avenir.web.message.model.responsemessage;

import ru.nikitavov.avenir.general.localization.I18n;

public class ResponseMessageFactory {
    /**
     * Создание сообщения
     *
     * @param message Сообщение которое необходимо обернуть в объект
     */
    public static ResponseMessage createMessage(String message) {
        return new ResponseMessage(message);
    }

    public static ResponseMessage createMessageThrow(Throwable throwable) {
        //TODO: Пересмотреть подход отправки ошибки клиенту
        return new ResponseMessage(throwable.getMessage());
    }

    /**
     * Создание локализованного сообщения
     *
     * @param key  Ключ сообщения которое необходимо локализировать
     * @param args Объекты которые необходимо
     */
    public static ResponseMessage createMessageLocalized(String key, Object... args) {
        return createMessage(I18n.format(key, args));
    }

    /**
     * Создание сообщение для конкретного поля с информаций или ошибкой
     *
     * @param entityClazz Класс сущьностости для которой необходимо локализировать название поля
     * @param fieldName   Название поле котрое необходимо локализировать
     * @param key         Ключ сообщения которое необходимо найти
     */
    public static FieldResponseMessage createMessageFieldLocalized(Class<?> entityClazz, String fieldName, String key, Object... args) {
        String localizedFieldName = I18n.fieldFormat(entityClazz, fieldName, true);
        return createMessageFieldLocalized(fieldName, localizedFieldName, key, args);
    }

    public static FieldResponseMessage createMessageFieldLocalized(String fieldName, String localizedFieldName, String key, Object... args) {
        localizedFieldName = I18n.format(localizedFieldName);
        String message = I18n.format(key, args);
        return new FieldResponseMessage(fieldName, localizedFieldName, message);
    }

    public static FieldResponseMessage createMessageFieldLocalized(String fieldName, String key, Object... args) {
        return createMessageFieldLocalized(fieldName, fieldName, key, args);
    }
}
