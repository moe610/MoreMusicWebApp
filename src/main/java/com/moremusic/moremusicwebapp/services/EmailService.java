package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.repository.ApplicationUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${application.link}")
    private String applicationLink;


    public String sendRegistrationEmail(ApplicationUser user) throws MessagingException {
        String result = "";
        String link = applicationLink + "/api/v1/applicationUsers/acceptRegistration/" + user.getUsername();
        String emailBody = String.format(
                "A new user has registered." +
                        "\n\nUsername: %s" +
                        "\nEmail: %s " +
                        "\nTo verify the user, click the following link: <a href=\"%s\">Verify</a>",
                user.getUsername(), user.getEmail(), link
        );

        sendEmail(adminEmail, emailBody);
        result = "User registration has been sent to Admin.";
        return result;
    }

    public void sendSuccessfulRegistrationEmail(ApplicationUser user) throws MessagingException {
        String link = applicationLink + "/login";

        String emailBody = String.format(
            "New user %s have been successfully registered." +
            "\n Please login %s",
                user.getUsername(), link
        );

        sendEmail(user.getEmail(), emailBody);
    }

    public void sendResetPasswordEmail(String resetToken, ApplicationUser user) throws MessagingException {
        String link = applicationLink + "/api/v1/applicationUsers/resetPassword/" + resetToken;
        String emailBody = String.format(
                "To reset your password please use the following link: " +
                "<a href=\"%s\">Reset Password</a>",
                link
        );

        sendEmail(user.getEmail(), emailBody);
    }

    private void sendEmail(String emailAddress, String emailBody) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailAddress);
            helper.setSubject("New User Registration");
            helper.setText(emailBody, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }
}
