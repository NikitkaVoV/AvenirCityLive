package ru.nikitavov.avenir.web.message.realization.user.role;

import java.util.Collection;

public record UserAvailableRolesResponse(Collection<String> roles) {
}
