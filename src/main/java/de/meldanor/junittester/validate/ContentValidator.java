package de.meldanor.junittester.validate;

import de.meldanor.junittester.validate.ContentValidateEngine.ContentValidatorResult;

public interface ContentValidator {

    public ContentValidatorResult validateCode(String code);
}
