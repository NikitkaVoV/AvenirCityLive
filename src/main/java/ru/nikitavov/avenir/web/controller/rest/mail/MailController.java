package ru.nikitavov.avenir.web.controller.rest.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final JavaMailSender javaMailSender;

    @PostMapping("send")
    public void sendEmail(@RequestBody MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mail.kkep.service@gmail.com");
        message.setTo(request.to());
        message.setSubject(request.subject());
        message.setText(request.text());
        javaMailSender.send(message);
    }
}
