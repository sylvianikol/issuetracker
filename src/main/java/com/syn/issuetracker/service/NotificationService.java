package com.syn.issuetracker.service;

import com.syn.issuetracker.model.entity.Notification;
import com.syn.issuetracker.model.entity.Task;

import javax.mail.MessagingException;

public interface NotificationService {

    Notification create(Task task);

    void sendNotification(Notification notification) throws MessagingException, InterruptedException;
}
