package ru.nikitavov.avenir.web.message.realization.reirect;

import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public record RedirectUrlGetResponse(String hash) implements IResponse {
}
