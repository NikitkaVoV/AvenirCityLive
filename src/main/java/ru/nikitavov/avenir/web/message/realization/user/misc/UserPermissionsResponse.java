package ru.nikitavov.avenir.web.message.realization.user.misc;

import java.util.Collection;

public record UserPermissionsResponse(Collection<String> permissions) {
}
