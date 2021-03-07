package com.syn.issuetracker.common;

public class ValidationErrorMessages {

    public static final String USERNAME_BLANK = "Username should not be empty!";
    public static final String USERNAME_LENGTH = "Username length should be between 3 and 30 characters.";

    public static final String PASSWORD_BLANK = "Password should not be empty!";
    public static final String PASSWORD_LENGTH = "Password length should be minimum 6 characters.";

    public static final String EMAIL_BLANK = "Email should not be empty!";
    public static final String EMAIL_NOT_VALID = "Please, enter a valid email! (username@mail.com)";

    public static final String TITLE_BLANK = "Title should not be empty!";
    public static final String TITLE_LENGTH = "Title length should be minimum 2 characters.";

    public static final String DATE_NULL = "Date can not be null";
    public static final String DATE_FUTURE = "Date cannot be in the future";

    public static final String PRIORITY_NOT_VALID = "Priority is not valid!";
    public static final String STATUS_NOT_VALID = "Status is not valid!";
}
