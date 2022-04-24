package com.school.stanthony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmailValidator {
    private Pattern pattern;
    private Matcher matcher;
    private static EmailValidator sInstance;


    public static EmailValidator getInstance() {
        if (sInstance == null) {
            sInstance = new EmailValidator();
        }
        return sInstance;
    }
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
}