package ru.nikitavov.avenir.web.message.realization.user.misc;

import lombok.Getter;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.web.message.intefaces.IResponse;
import ru.nikitavov.avenir.web.security.permission.Roles;
import ru.nikitavov.avenir.web.util.role.RoleUtil;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record FindUsersResponse(Collection<UserInfo> users, Long totalElements, Integer totalPages) implements IResponse {

    @Getter
    public static final class UserInfo {
        private final Integer id;
        private final UUID uuid;
        private final String name;
        private final String surname;
        private final String patronymic;
        private final boolean verified;
        private final List<String> roles;

        public UserInfo(User user, List<String> roles) {
            this.id = user.getId();
            this.uuid = user.getUuid();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.patronymic = user.getPatronymic();
            this.verified = RoleUtil.containRole(user.getRoles(), Roles.VERIFIED);
            this.roles = roles;
        }
    }
}
