package ru.nikitavov.avenir.web.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
@Service
public class ResponseConverterService {

    private final MappingJackson2HttpMessageConverter jsonConverterHttp;
    private final ObjectMapper jsonConverter;
    private final RedirectUrlFactory redirectUrlFactory;

    /**
     * Функция позволяющая вне контролера записывать данные в ответ на основе {@link ResponseEntity}
     *
     * @param responseEntity Сущность которую необходимо записать в ответ
     * @param response       Ответ для клиента куда будет записана сущность
     */
    public void writeREntityInResponse(ResponseEntity<?> responseEntity, HttpServletResponse response) {

        response.setStatus(responseEntity.getStatusCodeValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HttpHeaders responseHeaders = responseEntity.getHeaders();
        for (String headerName : responseHeaders.keySet()) {
            response.setHeader(headerName, responseHeaders.getFirst(headerName));
        }

        if (responseEntity.getBody() != null) {
            try {
                OutputStream outputStream = response.getOutputStream();
                jsonConverterHttp.write(responseEntity.getBody(), MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeMessageWrapperInResponseParamAndRedirect(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              MessageWrapper<?> messageWrapper,
                                                              String key) {

        RedirectUrlObject redirectUrlObject = redirectUrlFactory.create(request).addParam(key, messageWrapper, true);
//        redirectUrl.
//        response.
    }
}
