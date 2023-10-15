package ru.nikitavov.avenir.web.message.realization.user.profile;

import ru.nikitavov.avenir.web.message.intefaces.IResponse;

import java.util.List;
import java.util.UUID;

public record ProfileResponse(
        List<ProfileLinkedSocialResponse> linkedSocial,
        boolean containPassword,
        String mail,
        String name,
        String surname,
        String patronymic,
        UUID uuid
) implements IResponse {

}
