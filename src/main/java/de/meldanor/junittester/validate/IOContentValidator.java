package de.meldanor.junittester.validate;

import de.meldanor.junittester.validate.ContentValidateEngine.ContentValidatorResult;

public class IOContentValidator implements ContentValidator {

    // TODO: Replace the contains with a more complex regex (check if the
    // package name is used in a string)
    public ContentValidatorResult validateCode(String code) {
        if (code.contains("java.io")) {
            return new ContentValidatorResult("package java.io is not allowed");
        } else if (code.contains("java.nio"))
            return new ContentValidatorResult("package java.nio is not allowed");
        else
            return new ContentValidatorResult();
    }
}
