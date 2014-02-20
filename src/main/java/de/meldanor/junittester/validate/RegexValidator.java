package de.meldanor.junittester.validate;

import java.util.regex.Pattern;

import de.meldanor.junittester.validate.ContentValidateEngine.ContentValidatorResult;

public class RegexValidator implements ContentValidator {

    private final Pattern pattern;
    private final String errorMessage;

    public RegexValidator(String regex, String errorMessage) {
        this.pattern = Pattern.compile(regex);
        this.errorMessage = errorMessage;
    }

    public ContentValidatorResult validateCode(String code) {
        if (pattern.matcher(code).matches()) {
            return new ContentValidatorResult(errorMessage);
        } else
            return new ContentValidatorResult();
    }
}
