package ru.nikitavov.avenir.web.controller.rest.mail;

public record MailRequest(String to, String subject, String text) {
}
