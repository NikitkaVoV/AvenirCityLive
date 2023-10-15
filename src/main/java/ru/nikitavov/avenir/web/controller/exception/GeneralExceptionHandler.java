package ru.nikitavov.avenir.web.controller.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nikitavov.avenir.general.localization.I18n;
import ru.nikitavov.avenir.web.message.intefaces.IResponseMessage;
import ru.nikitavov.avenir.web.message.model.responsemessage.FieldResponseMessage;
import ru.nikitavov.avenir.web.message.model.responsemessage.ResponseMessageFactory;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> generalExceptionHandler(Exception exception) throws Exception {
        if (exception instanceof BindException e) return bindingExceptionHandler(e);

        throw exception;
    }

    private ResponseEntity<Object> bindingExceptionHandler(BindException exception) throws BindException {
        ArrayList<IResponseMessage> messages = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            if (fieldError == null) throw exception;
            if (!fieldError.contains(TypeMismatchException.class)) throw exception;
            TypeMismatchException source = fieldError.unwrap(TypeMismatchException.class);
            Throwable cause = source.getCause();
            if (cause instanceof NumberFormatException) {
                FieldResponseMessage fieldMessage = ResponseMessageFactory.createMessageFieldLocalized(fieldError.getField(),
                        I18n.format("message.warn.convert.number.format", fieldError.getRejectedValue()));
                messages.add(fieldMessage);
            } else if (cause instanceof ConversionFailedException e) {
                FieldResponseMessage fieldMessage = ResponseMessageFactory.createMessageFieldLocalized(fieldError.getField(),
                        I18n.format("message.warn.field.enum.notfound", e.getValue(), e.getTargetType().getType().getSimpleName()));
                messages.add(fieldMessage);
            } else if (cause instanceof DateTimeParseException e) {
                System.out.println();
            }
            else {
                throw exception;
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageWrapper<>(null, messages));
    }
}
