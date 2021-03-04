package com.syn.issuetracker.notification;

import javax.mail.MessagingException;

public interface NotificationExecutor {

    void sendNotification(String username, String userEmail, String taskTitle,
                          String taskDescription, String taskPriority, String taskId) throws InterruptedException, MessagingException;
}
