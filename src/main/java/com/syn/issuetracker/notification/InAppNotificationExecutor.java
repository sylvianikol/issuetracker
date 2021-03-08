package com.syn.issuetracker.notification;

import com.syn.issuetracker.model.entity.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;

public class InAppNotificationExecutor implements NotificationExecutor  {

    private static final Logger log = LoggerFactory.getLogger(InAppNotificationExecutor.class);

    @Override
    public void sendNotification(Notification notification) throws InterruptedException, MessagingException {

    }
}
