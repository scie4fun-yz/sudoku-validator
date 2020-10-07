package com.sudoku.validator.utils;

import com.sudoku.validator.domain.ValidationResult;
import org.junit.jupiter.api.Test;

import static com.sudoku.validator.utils.CsvSudokuValidator.SUDOKU_MATRIX_SIDE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvSudokuValidatorTest {

    private static final String INVALID_PATH_TO_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/invalid-path-to-sudoku-matrix.csv";
    private static final String INVALID_NON_UNIQUE_ROW_COLUMN_VALUES_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/invalid-non-unique-row-column-values-sudoku-matrix.txt";
    private static final String INVALID_DIMENSIONS_SIZE_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/invalid-dimensions-size-sudoku-matrix.csv";
    private static final String INVALID_FORMAT_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/invalid-format-sudoku-matrix";
    private static final String INVALID_NON_UNIQUE_SUB_SQUARE_VALUES_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/invalid-non-unique-sub-square-values-sudoku-matrix.csv";
    private static final String VALID_SUDOKU_EMPTY_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/valid-empty-sudoku-matrix.csv";
    private static final String VALID_MIXED_VALUES_SUDOKU_EMPTY_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/valid-mixed-values-sudoku-matrix.csv";
    private static final String VALID_SUDOKU_MATRIX_FILE_PATH =
            "src/test/resources/sudoku-matrices/valid-sudoku-matrix.csv";
    private static final SudokuValidator CSV_SUDOKU_MATRIX_VALIDATOR = new CsvSudokuValidator();

    @Test
    void validateMatrixInvalidFilePath() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                INVALID_PATH_TO_SUDOKU_MATRIX_FILE_PATH);
        String validationResultOutputMessage = ValidationResult.getOutputMessage(result);

        assertEquals(ValidationResult.ValidationResultType.INVALID, result.getType());
        assertTrue(validationResultOutputMessage.contains("File <" + result.getFilePath() + "> does not exists"));
    }

    @Test
    void validateMatrixInvalidDimensionsSize() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                INVALID_DIMENSIONS_SIZE_SUDOKU_MATRIX_FILE_PATH);
        String validationResultOutputMessage = ValidationResult.getOutputMessage(result);
        String invalidMatrixSideSize = String.format(
                "Sudoku matrix side should be equal to <%s>",
                SUDOKU_MATRIX_SIDE_SIZE);

        assertEquals(ValidationResult.ValidationResultType.INVALID, result.getType());
        assertTrue(validationResultOutputMessage.contains(invalidMatrixSideSize));
    }

    @Test
    void validateMatrixInvalidFormat() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                INVALID_FORMAT_SUDOKU_MATRIX_FILE_PATH);
        String validationResultOutputMessage = ValidationResult.getOutputMessage(result);
        String invalidMatrixFormat =
                "Sudoku matrix should contain only single digits 0 - 9 or . (dot) for an empty cell. " +
                        "Each value should be separated with comma in order to match CSV format requirements";

        assertEquals(ValidationResult.ValidationResultType.INVALID, result.getType());
        assertTrue(validationResultOutputMessage.contains(invalidMatrixFormat));
    }

    @Test
    void validateMatrixInvalidNonUniqueRowColumnValues() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                INVALID_NON_UNIQUE_ROW_COLUMN_VALUES_SUDOKU_MATRIX_FILE_PATH);
        String validationResultOutputMessage = ValidationResult.getOutputMessage(result);

        assertEquals(ValidationResult.ValidationResultType.INVALID, result.getType());
        assertTrue(
                validationResultOutputMessage.contains("Sudoku matrix rows/columns should contain only unique values"));
    }

    @Test
    void validateMatrixInvalidNonUniqueSubSquareValues() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                INVALID_NON_UNIQUE_SUB_SQUARE_VALUES_SUDOKU_MATRIX_FILE_PATH);
        String validationResultOutputMessage = ValidationResult.getOutputMessage(result);

        assertEquals(ValidationResult.ValidationResultType.INVALID, result.getType());
        assertTrue(
                validationResultOutputMessage.contains("Sudoku matrix sub-squares should contain only unique values"));
    }

    @Test
    void validateEmptyMatrixValid() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(VALID_SUDOKU_EMPTY_MATRIX_FILE_PATH);

        assertEquals(ValidationResult.ValidationResultType.VALID, result.getType());
    }

    @Test
    void validateMixedValuesMatrixValid() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(
                VALID_MIXED_VALUES_SUDOKU_EMPTY_MATRIX_FILE_PATH);

        assertEquals(ValidationResult.ValidationResultType.VALID, result.getType());
    }

    @Test
    void validateMatrixValid() {
        ValidationResult result = CSV_SUDOKU_MATRIX_VALIDATOR.validateMatrix(VALID_SUDOKU_MATRIX_FILE_PATH);
        final String VALIDATION_RESULT_TAG = "[VALIDATION RESULT]";
        String validationResultOutputMessage = String.format("%s%n\tresult code is <%s> [%s],%n\tfile path: %s",
                VALIDATION_RESULT_TAG, ValidationResult.ValidationResultType.VALID.getCode(),
                ValidationResult.ValidationResultType.VALID.name(),
                result.getFilePath());

        assertEquals(ValidationResult.ValidationResultType.VALID, result.getType());
        assertEquals(validationResultOutputMessage, ValidationResult.getOutputMessage(result));
    }
}