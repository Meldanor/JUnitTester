package de.meldanor.junittester.check;

import de.meldanor.junittester.check.ContentValidateEngine.ContentValidatorResult;

public interface ContentValidator {

    public ContentValidatorResult validateCode(String code);
}
