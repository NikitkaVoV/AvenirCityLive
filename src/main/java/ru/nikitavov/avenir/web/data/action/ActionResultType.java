package ru.nikitavov.avenir.web.data.action;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ActionResultType {
    //OK
    OK(HttpStatus.OK),
    CREATE(HttpStatus.CREATED),

    //Client errors
    NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_PARAMS(HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA(HttpStatus.CONFLICT),

    //Server errors
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus defaultStatus;

    ActionResultType(HttpStatus defaultStatus) {
        this.defaultStatus = defaultStatus;
    }
}
