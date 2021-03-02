package com.syn.issuetracker.common;

public class ValidationErrorMessages {

    public static final String NAME_BLANK = "Name should not be empty!";
    public static final String NAME_LENGTH = "Name length should be between 2 and 50 characters.";

    public static final String TITLE_BLANK = "Title should not be empty!";
    public static final String TITLE_LENGTH = "Title length should be minimum 2 characters.";

    public static final String DATE_NULL = "Date can not be null";
    public static final String DATE_FUTURE = "Date cannot be in the future";

    public static final String STATUS_BLANK = "Status can not be empty!";
    public static final String DEVELOPER_NULL = "Developer can not be null!";
    public static final String PRIORITY_BLANK = "Priority can not be empty!";
}
