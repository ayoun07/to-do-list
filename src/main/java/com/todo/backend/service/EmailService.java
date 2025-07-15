package com.todo.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String token) {
        String confirmationLink = "http://localhost:8080/auth/confirm?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmation de votre inscription");
        message.setText("Cliquez sur ce lien pour activer votre compte :\n" + confirmationLink);
        message.setFrom("ton.email@gmail.com");

        mailSender.send(message);

        System.out.println("✅ E-mail de confirmation envoyé à : " + to);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Réinitialisation de mot de passe";
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String text = "Cliquez sur ce lien pour réinitialiser votre mot de passe :\n" + resetLink;
        sendEmail(to, subject, text);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("ton.email@gmail.com");
        mailSender.send(message);
    }
}
