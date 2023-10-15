package ru.nikitavov.avenir.web.controller.rest.misc;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.reirect.RedirectUrlGetRequest;
import ru.nikitavov.avenir.web.message.realization.reirect.RedirectUrlGetResponse;
import ru.nikitavov.avenir.web.message.service.RedirectUrlService;

@RestController
@RequestMapping("/redirect")
@RequiredArgsConstructor
public class RedirectUrlController {

private final RedirectUrlService redirectUrlService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ содержит тело с хэшем и код 0"),
            @ApiResponse(responseCode = "400", description = "Ответ содержит пустое тело и один из кодов:" +
                    "<br>1 - url пуст или не разрешён для использования"
            ),
    })
    @GetMapping("/hash")
    public ResponseEntity<MessageWrapper<RedirectUrlGetResponse>> getHash(RedirectUrlGetRequest request) {
        String hashUrl = redirectUrlService.hashUrl(request.url());
        if (hashUrl == null) {
            return ResponseEntity.badRequest().body(new MessageWrapper<>(null, 1));
        }
        return ResponseEntity.ok(new MessageWrapper<>(new RedirectUrlGetResponse(hashUrl)));
    }
}
