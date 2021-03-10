package com.syn.issuetracker.service.impl;

import com.syn.issuetracker.model.entity.Notification;
import com.syn.issuetracker.model.entity.Task;
import com.syn.issuetracker.model.entity.UserEntity;
import com.syn.issuetracker.model.enums.NotificationType;
import com.syn.issuetracker.notification.NotificationExecutorFactory;
import com.syn.issuetracker.repository.NotificationRepository;
import com.syn.issuetracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import static com.syn.issuetracker.common.NotificationTemplates.NEW_TASK_INAPP_SUBJECT;
import static com.syn.issuetracker.common.NotificationTemplates.TASK_NOTICE_TEMPLATE;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification create(Task task) {
        String title = task.getTitle();
        UserEntity user = task.getDeveloper();

        Notification notification = new Notification();

        notification.setSubject(String.format(NEW_TASK_INAPP_SUBJECT, title));
        notification.setMessage(String.format(TASK_NOTICE_TEMPLATE,
                user.getUsername(), title, task.getDescription(), task.getPriority(), task.getId()));
        notification.setUser(user);

        this.notificationRepository.save(notification);

        return notification;
    }

    @Override
    public void sendNotification(Notification notification) throws MessagingException, InterruptedException {
        NotificationExecutorFactory.getExecutor(NotificationType.IN_APP)
                .sendNotification(notification);

//        NotificationExecutorFactory.getExecutor(NotificationType.EMAIL)
//                .sendNotification(notification);
    }
}
