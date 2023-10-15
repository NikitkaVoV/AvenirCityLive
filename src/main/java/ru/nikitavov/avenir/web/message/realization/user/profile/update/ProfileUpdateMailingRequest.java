package ru.nikitavov.avenir.web.message.realization.user.profile.update;

import ru.nikitavov.avenir.general.model.socuialnetwork.SocialNetworkType;
import ru.nikitavov.avenir.web.message.intefaces.IRequest;

public record ProfileUpdateMailingRequest(SocialNetworkType type) implements IRequest {
}
