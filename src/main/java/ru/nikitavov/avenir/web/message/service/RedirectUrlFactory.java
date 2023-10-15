package ru.nikitavov.avenir.web.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class RedirectUrlFactory {

    private final ObjectMapper jsonConverter;

    public RedirectUrlObject create(HttpServletRequest request) {
        return new RedirectUrlObject(request, jsonConverter);
    }
}
