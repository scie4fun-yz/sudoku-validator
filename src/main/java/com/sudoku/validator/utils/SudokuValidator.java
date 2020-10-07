package com.sudoku.validator.utils;

import com.sudoku.validator.domain.ValidationResult;

public interface SudokuValidator {

    ValidationResult validateMatrix(String filePath);
}
