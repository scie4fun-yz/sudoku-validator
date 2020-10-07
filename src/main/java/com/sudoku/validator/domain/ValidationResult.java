package com.sudoku.validator.domain;

public class ValidationResult {

    private final ValidationResultType type;
    private final String filePath;
    private final String errorMessage;

    public ValidationResultType getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private ValidationResult(ValidationResultType type, String filePath) {
        this(type, filePath, null);
    }

    private ValidationResult(ValidationResultType type, String filePath, String errorMessage) {
        this.type = type;
        this.filePath = filePath;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult createValid(String filePath) {
        return new ValidationResult(ValidationResultType.VALID, filePath);
    }

    public static ValidationResult createInvalid(String filePath, String errorMessage) {
        return new ValidationResult(ValidationResultType.INVALID, filePath, errorMessage);
    }

    public static String getOutputMessage(ValidationResult result) {
        final String VALIDATION_RESULT_TAG = "[VALIDATION RESULT]";

        if (result.getType().equals(ValidationResultType.VALID)) {
            return String.format("%s%n\tresult code is <%s> [%s],%n\tfile path: %s",
                    VALIDATION_RESULT_TAG, ValidationResultType.VALID.getCode(), ValidationResultType.VALID.name(),
                    result.getFilePath());
        } else {
            return String.format("%s%n\tresult code is <%s> [%s],%n\terror message: <%s>,%n\tfile path: %s",
                    VALIDATION_RESULT_TAG, ValidationResultType.INVALID.getCode(), ValidationResultType.INVALID.name(),
                    result.getErrorMessage(), result.getFilePath());
        }
    }

    public enum ValidationResultType {
        VALID(0), INVALID(1);

        private final int code;

        public int getCode() {
            return code;
        }

        ValidationResultType(int resultCode) {
            this.code = resultCode;
        }
    }
}
