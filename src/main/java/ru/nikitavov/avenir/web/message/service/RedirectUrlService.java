package ru.nikitavov.avenir.web.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.security.RedirectUrl;
import ru.nikitavov.avenir.database.repository.RedirectUrlRepository;
import ru.nikitavov.avenir.web.security.AppProperties;
import ru.nikitavov.avenir.web.security.util.KeyGenerator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedirectUrlService {

    private final AppProperties appProperties;
    private final RedirectUrlRepository redirectUrlRepository;

    public String hashUrl(String url) {
        if (url == null || appProperties.getCors().getAllowedOrigins().stream().noneMatch(url::startsWith)) {
            return "";
        }

        Optional<RedirectUrl> urlOptional = redirectUrlRepository.findByUrl(url);
        if (urlOptional.isPresent()) {
            return urlOptional.get().getHash();
        }

        String hash = KeyGenerator.string(32);
        return redirectUrlRepository.save(RedirectUrl.builder().url(url).hash(hash).build()).getHash();
    }
}
