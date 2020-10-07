package com.sudoku.validator.utils;

import com.sudoku.validator.domain.CsvSudokuMatrix;
import com.sudoku.validator.domain.MatrixPart;
import com.sudoku.validator.domain.SudokuMatrix;
import com.sudoku.validator.domain.ValidationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sudoku.validator.domain.CsvSudokuMatrix.CSV_SPLITERATOR;

public class CsvSudokuValidator implements SudokuValidator {

    public static final int SUDOKU_MATRIX_SIDE_SIZE = 9;
    public static final String SUDOKU_MATRIX_EMPTY_VALUE = ".";

    private List<String> sudokuMatrixFileLines;
    private SudokuMatrix sudokuMatrix;
    private String filePath;

    @Override
    public ValidationResult validateMatrix(String filePath) {
        this.filePath = Paths.get(filePath).toAbsolutePath().toString().replaceAll("(\\.\\.\\\\)|(\\.\\\\)", "");
        if (!Paths.get(filePath).toFile().exists()) {
            String fileNotFoundErrorMessage = String.format("File <%s> does not exists", this.filePath);
            return ValidationResult.createInvalid(this.filePath, fileNotFoundErrorMessage);
        }

        ValidationResult result = validateSudokuMatrixSizeAndFormat();

        if (result.getType().equals(ValidationResult.ValidationResultType.VALID)) {
            this.sudokuMatrix = new CsvSudokuMatrix(sudokuMatrixFileLines);

            List<Supplier<ValidationResult>> validations = Arrays.asList(
                    this::validateSequence,
                    this::validateSubSquares
            );
            for (Supplier<ValidationResult> validation : validations) {
                result = validation.get();
                if (result.getType().equals(ValidationResult.ValidationResultType.INVALID)) {
                    break;
                }
            }
        }

        return result;
    }

    private ValidationResult validateSudokuMatrixSizeAndFormat() {
        ValidationResult result = ValidationResult.createValid(filePath);
        try (Stream<String> fileLines = Files.lines(Paths.get(filePath))) {
            this.sudokuMatrixFileLines = fileLines.collect(Collectors.toList());

            long totalLines = sudokuMatrixFileLines.size();
            boolean areNotValidColumns = !sudokuMatrixFileLines.stream().allMatch(
                    line -> line.split(CSV_SPLITERATOR).length == SUDOKU_MATRIX_SIDE_SIZE);
            if (totalLines != SUDOKU_MATRIX_SIDE_SIZE || areNotValidColumns) {
                String invalidMatrixSideSize = String.format(
                        "Sudoku matrix side should be equal to <%s>",
                        SUDOKU_MATRIX_SIDE_SIZE);

                return ValidationResult.createInvalid(filePath, invalidMatrixSideSize);
            }

            final String VALID_FORMAT_PATTERN = "^([0-9.],){8}([0-9.])$";
            boolean hasInvalidFormat = !sudokuMatrixFileLines.stream().allMatch(
                    line -> line.matches(VALID_FORMAT_PATTERN));
            if (hasInvalidFormat) {
                String invalidMatrixFormat =
                        "Sudoku matrix should contain only single digits 0 - 9 or . (dot) for an empty cell. " +
                                "Each value should be separated with comma in order to match CSV format requirements";
                result = ValidationResult.createInvalid(filePath, invalidMatrixFormat);
            }
        } catch (IOException e) {
            result = ValidationResult.createInvalid(filePath, e.getMessage());
        }

        return result;
    }

    private ValidationResult validateSequence() {
        Map<MatrixPart, String[][]> matrix = this.sudokuMatrix.getMatrix();
        for (Map.Entry<MatrixPart, String[][]> entry : matrix.entrySet()) {
            for (String[] column : entry.getValue()) {
                List<String> values = Stream.of(column)
                        .filter(val -> !val.equals(SUDOKU_MATRIX_EMPTY_VALUE))
                        .collect(Collectors.toList());
                Set<String> distinctValues = new HashSet<>(values);
                if (distinctValues.size() != values.size()) {
                    /* As far as non-unique row values means de facto non-unique column values as well,
                     * we don't need here to verify uniqueness separately for each of them
                     */
                    String notUniqueColumnValues = "Sudoku matrix rows/columns should contain only unique values";
                    return ValidationResult.createInvalid(filePath, notUniqueColumnValues);
                }
            }
        }

        return ValidationResult.createValid(filePath);
    }

    private ValidationResult validateSubSquares() {
        Map<MatrixPart, String[][]> matrix = this.sudokuMatrix.getMatrix()
                .entrySet().stream()
                .filter(entry -> entry.getKey() instanceof SudokuMatrix.MatrixSubSquarePart)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<MatrixPart, String[][]> entry : matrix.entrySet()) {
            List<String> values = Arrays.stream(entry.getValue()).sequential()
                    .flatMap(Stream::of)
                    .filter(val -> !val.equals(SUDOKU_MATRIX_EMPTY_VALUE))
                    .collect(Collectors.toList());
            Set<String> distinctValues = new HashSet<>(values);
            if (distinctValues.size() != values.size()) {
                String notUniqueSubSquareValues = "Sudoku matrix sub-squares should contain only unique values";
                return ValidationResult.createInvalid(filePath, notUniqueSubSquareValues);
            }
        }

        return ValidationResult.createValid(filePath);
    }
}
