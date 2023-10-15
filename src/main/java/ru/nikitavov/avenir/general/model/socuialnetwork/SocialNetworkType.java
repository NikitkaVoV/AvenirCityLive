package ru.nikitavov.avenir.general.model.socuialnetwork;

import lombok.Getter;

@Getter
public enum SocialNetworkType {
    VK(true), TELEGRAM(true), GOOGLE(false), GITHUB(false);

    private final boolean needMailing;

    SocialNetworkType(boolean needMailing) {

        this.needMailing = needMailing;
    }
}
