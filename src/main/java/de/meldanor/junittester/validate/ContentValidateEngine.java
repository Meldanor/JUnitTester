package de.meldanor.junittester.validate;

import java.util.ArrayList;
import java.util.List;

public class ContentValidateEngine {

    private List<ContentValidator> contentCheckerList;

    public ContentValidateEngine() {
        this.contentCheckerList = new ArrayList<ContentValidator>();
    }

    public ContentValidateEngine registerContentChecker(ContentValidator cChecker) {
        this.contentCheckerList.add(cChecker);
        return this;
    }

    public ContentValidatorResult isValid(String sourceCode) {
        if (contentCheckerList.isEmpty()) {
            return new ContentValidatorResult();
        }
        if (sourceCode == null || sourceCode.isEmpty()) {
            System.out.println("SourceCode is null or empty - no checks neccessary");
            return new ContentValidatorResult();
        }

        ContentValidatorResult result = null;

        for (ContentValidator cChecker : contentCheckerList) {
            result = cChecker.validateCode(sourceCode);
        }

        return result;
    }

    public static class ContentValidatorResult {

        private final String reason;

        public ContentValidatorResult() {
            this.reason = null;
        }

        public ContentValidatorResult(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }

        public boolean isValid() {
            return reason == null;
        }

        @Override
        public String toString() {
            return "ContentValidatorResult={isValid: " + isValid() + ";reason = " + reason + "}";
        }
    }

}
