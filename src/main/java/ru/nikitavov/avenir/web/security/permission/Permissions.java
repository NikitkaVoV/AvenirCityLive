package ru.nikitavov.avenir.web.security.permission;

import lombok.Getter;

@Getter
public enum Permissions {

    NONE("none"),

    PERMISSIONS_ME("permissions.me"),

    PROFILE_ME("profile.me"),
    PROFILE_UPDATING_NAME("profile.updating.name"),
    PROFILE_UPDATING_MAIL("profile.updating.mail"),
    PROFILE_UPDATING_PASSWORD("profile.updating.password"),
    PROFILE_UPDATING_REMOVE_LINKED_NETWORK("profile.updating.remove_linked_network"),
    PROFILE_UPDATING_MAILING("profile.updating.mailing"),
    PROFILE_UPDATING_LINKED_SCHEDULE("profile.updating.linked_schedule"),

    SCHEDULE_UPDATING_REPLACEMENT("schedule.updating.replacement"),
    SCHEDULE_UPDATING_STANDARD("schedule.updating.standard"),

    VERIFIED_ADD("verified.add"),
    VERIFIED_REMOVE("verified.remove"),
    VERIFIED_FIND("verified.find"),

    ROLE_MANAGER_AVAILABLE("role.manager.available"),
    ROLE_MANAGER_VIEW("role.manager.view"),
    ROLE_MANAGER_ADD("role.manager.add"),
    ROLE_MANAGER_REMOVE("role.manager.remove"),
    ;

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }
}
