package com.syn.issuetracker.service;

import com.syn.issuetracker.model.entity.Notification;
import com.syn.issuetracker.model.entity.Task;

public interface NotificationService {

    Notification create(Task task);
}
