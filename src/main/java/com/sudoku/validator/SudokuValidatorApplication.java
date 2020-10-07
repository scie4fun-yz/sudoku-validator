package com.sudoku.validator;

import com.sudoku.validator.domain.ValidationResult;
import com.sudoku.validator.utils.CsvSudokuValidator;
import com.sudoku.validator.utils.SudokuValidator;

public class SudokuValidatorApplication {

    @SuppressWarnings({"squid:S106"})
    public static void main(String[] args) {
        SudokuValidator validator = new CsvSudokuValidator();
        for (String arg : args) {
            ValidationResult result = validator.validateMatrix(arg);
            String validationMessage = ValidationResult.getOutputMessage(result);
            System.out.println(validationMessage);
        }
    }
}
