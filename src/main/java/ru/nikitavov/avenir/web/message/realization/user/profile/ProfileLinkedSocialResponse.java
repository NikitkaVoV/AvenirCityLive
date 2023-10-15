package ru.nikitavov.avenir.web.message.realization.user.profile;

import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;

public record ProfileLinkedSocialResponse(
        SocialNetworkType type,
        boolean contain,
        boolean needMailing,
        boolean enabledMailing
) implements IResponse {
}
