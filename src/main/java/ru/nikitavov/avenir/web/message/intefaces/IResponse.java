package ru.nikitavov.avenir.web.message.intefaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public interface IResponse {
    @JsonIgnore
    default HttpStatus defaultStatus() {
        return HttpStatus.OK;
    }
}
