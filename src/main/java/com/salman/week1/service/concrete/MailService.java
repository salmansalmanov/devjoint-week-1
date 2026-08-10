package com.salman.week1.service.concrete;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendUserCreationEmail(String to) {
        String subject = "Welcome to the Bookstore";
        String text = "Your account has been created successfully.";
        sendMail(to, subject, text);
    }

    @Async
    public void sendUserUpdateEmail(String to) {
        String subject = "Account Update";
        String text = "Your account has been updated successfully.";
        sendMail(to, subject, text);
    }

    public void sendUserDeletionEmail(String to) {
        String subject = "Account Deletion";
        String text = "Your account has been deleted successfully.";
        sendMail(to, subject, text);
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setFrom(from);
        message.setText(text);
        mailSender.send(message);
    }
}
