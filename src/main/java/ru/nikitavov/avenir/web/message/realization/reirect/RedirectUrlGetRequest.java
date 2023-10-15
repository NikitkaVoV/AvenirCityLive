package ru.nikitavov.avenir.web.message.realization.reirect;

import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record RedirectUrlGetRequest(String url) implements IRequest {
}
