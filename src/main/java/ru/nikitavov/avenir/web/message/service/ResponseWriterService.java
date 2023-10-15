package ru.nikitavov.avenir.web.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResponseWriterService {

    private final ObjectMapper jsonConverter;

    public void write(HttpServletResponse response, HttpStatus status, MessageWrapper<?> body) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());
        try {
            response.getWriter().write(jsonConverter.writeValueAsString(body));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
