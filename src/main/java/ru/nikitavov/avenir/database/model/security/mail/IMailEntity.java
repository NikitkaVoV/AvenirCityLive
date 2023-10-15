package ru.nikitavov.avenir.database.model.security.mail;

import java.util.Date;

public interface IMailEntity {
    String getToken();

    Date getExpiryDate();

    String getRedirectUrl();
}
