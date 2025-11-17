package com.project.expenseTracker.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void accountCreationEmailSender(String recipient, String username) {
        SimpleMailMessage emailDetails = new SimpleMailMessage();
        emailDetails.setTo(recipient);
        emailDetails.setSubject("Expense Tracker");
        emailDetails.setText("Hi " + username + "\n\nYour new expense account has been successfully created :)\n\nThanks\nExpense Tracker Team");

        javaMailSender.send(emailDetails);
    }
}
