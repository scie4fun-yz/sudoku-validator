package com.sudoku.validator.domain;

import java.util.Map;

public interface SudokuMatrix {

    Map<MatrixPart, String[][]> getMatrix();

    enum MatrixDimensionPart implements MatrixPart {
        ROWS, COLUMNS
    }

    enum MatrixSubSquarePart implements MatrixPart {
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH,
        SIXTH,
        SEVENTH,
        EIGHTH,
        NINTH
    }
}
