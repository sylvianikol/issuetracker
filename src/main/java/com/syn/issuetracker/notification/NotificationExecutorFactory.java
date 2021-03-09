package com.syn.issuetracker.notification;

import com.syn.issuetracker.model.enums.NotificationType;

public class NotificationExecutorFactory {

    public static NotificationExecutor getExecutor(NotificationType type) {

        switch (type) {
            case EMAIL:
                return new EmailNotificationExecutor();
            case IN_APP :
                return new InAppNotificationExecutor();
            default:
                return null;
        }
    }
}
