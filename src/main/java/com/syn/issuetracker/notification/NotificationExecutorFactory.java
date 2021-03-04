package com.syn.issuetracker.notification;

import com.syn.issuetracker.enums.NotificationType;

public class NotificationExecutorFactory {

    public static NotificationExecutor getExecutor(NotificationType type) {

        switch (type) {
            case EMAIL -> {
                return new EmailNotificationExecutor();
            }
            case IN_APP -> {
                // todo: add case for 'In-app notification'
            }
        }
        return null;
    }
}
