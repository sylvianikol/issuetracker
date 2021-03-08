package com.syn.issuetracker.notification;

import com.syn.issuetracker.model.entity.Notification;

import javax.mail.MessagingException;

public interface NotificationExecutor {

    void sendNotification(Notification notification) throws InterruptedException, MessagingException;
}
