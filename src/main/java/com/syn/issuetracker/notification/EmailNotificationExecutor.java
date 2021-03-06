package com.syn.issuetracker.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.syn.issuetracker.common.NotificationTemplates.NEW_TASK_EMAIL_SUBJECT;
import static com.syn.issuetracker.common.NotificationTemplates.TASK_NOTICE_TEMPLATE;

@Component
public class EmailNotificationExecutor implements NotificationExecutor {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationExecutor.class);

    @Override
    @Async
    public void sendNotification(String username, String userEmail, String taskTitle,
                                 String taskDescription, String taskPriority, String taskId) throws InterruptedException, MessagingException {
        
        JavaMailSender javaMailSender = javaMailSender();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String emailTemplate = String.format(TASK_NOTICE_TEMPLATE,
                username,
                taskTitle,
                taskDescription,
                taskPriority,
                taskId);

        helper.setText(emailTemplate, true);
        helper.setTo(userEmail);
        helper.setSubject(NEW_TASK_EMAIL_SUBJECT);
        helper.setFrom(System.getenv("MAIL_USER"));

        log.info("Sending...");
        javaMailSender.send(mimeMessage);

        log.info("========== NEW TASK NOTIFICATION SENT ============");
    }

    private JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(System.getenv("MAIL_HOST"));
        mailSender.setUsername(System.getenv("MAIL_USER"));
        mailSender.setPassword(System.getenv("MAIL_PASSWORD"));
        mailSender.setPort(Integer.parseInt(System.getenv("MAIL_PORT")));

        Properties properties = new Properties();
        properties.setProperty("mail.from.email", System.getenv("MAIL_USER"));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
