package de.meldanor.junittester.check;

import de.meldanor.junittester.check.ContentValidateEngine.ContentValidatorResult;

public class NetworkingContentValidator implements ContentValidator {

    // TODO: Replace the contains with a more complex regex (check if the
    // package name is used in a string)
    public ContentValidatorResult validateCode(String code) {
        if (code.contains("java.net")) {
            return new ContentValidatorResult("package java.net is not allowed");
        } else if (code.contains("javax.net"))
            return new ContentValidatorResult("package javax.net is not allowed");
        else
            return new ContentValidatorResult();
    }
}
