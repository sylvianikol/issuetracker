package com.syn.issuetracker.common;

public class NotificationTemplates {

    private static final String TEMPLATE_SEPARATOR =
            "<p>------------------------------------------</p>";
    private static final String TEMPLATE_FOOTER =
            "Best regards,<br/> <a href=\"http://localhost:4200/\">SynTasker</a>";

    public static final String NEW_TASK_EMAIL_SUBJECT = "You have a new task on SynTasker";
    public static final String NEW_TASK_INAPP_SUBJECT = "You have a new task: %s";

    public static final String TASK_NOTICE_TEMPLATE =
            "<h3>Greetings, %s!</h3> <p>A new task has been assigned to you!</p>" +
                    TEMPLATE_SEPARATOR +
                    "<p>Task title: %s</p>" +
                    "<p>Description: %s</p>" +
                    "<p>Priority: %s</p> " +
                    "<p>Link: http://localhost:4200/tasks/%s</p>" +
                    TEMPLATE_SEPARATOR + TEMPLATE_FOOTER;
}
