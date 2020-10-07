package com.sudoku.validator.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import static com.sudoku.validator.utils.CsvSudokuValidator.SUDOKU_MATRIX_SIDE_SIZE;

public class CsvSudokuMatrix implements SudokuMatrix {

    public static final String CSV_SPLITERATOR = ",";

    private final Map<MatrixPart, String[][]> matrix;

    public Map<MatrixPart, String[][]> getMatrix() {
        return matrix;
    }

    @SuppressWarnings({"squid:S1640"})
    public CsvSudokuMatrix(List<String> csvSudokuMatrixLines) {
        matrix = new LinkedHashMap<>();

        String[][] rows = new String[SUDOKU_MATRIX_SIDE_SIZE][SUDOKU_MATRIX_SIDE_SIZE];
        for (int row = 0; row < SUDOKU_MATRIX_SIDE_SIZE; ++row) {
            rows[row] = csvSudokuMatrixLines.get(row).split(CSV_SPLITERATOR);
        }
        matrix.put(MatrixDimensionPart.ROWS, rows);

        String[][] columns = new String[SUDOKU_MATRIX_SIDE_SIZE][SUDOKU_MATRIX_SIDE_SIZE];
        for (int column = 0; column < SUDOKU_MATRIX_SIDE_SIZE; ++column) {
            for (int row = 0; row < SUDOKU_MATRIX_SIDE_SIZE; ++row) {
                columns[column][row] = rows[row][column];
            }
        }
        matrix.put(MatrixDimensionPart.COLUMNS, columns);

        for (int i = 0; i < MatrixSubSquarePart.values().length; i += 3) {
            MatrixSubSquarePart subSquarePart = MatrixSubSquarePart.values()[i];
            final int SUDOKU_SQUARE_SIDE = SUDOKU_MATRIX_SIDE_SIZE / 3;
            for (int squareRowIndex = 0; squareRowIndex < SUDOKU_SQUARE_SIDE; ++squareRowIndex) {
                String[][] rowFirstSquare = new String[SUDOKU_SQUARE_SIDE][SUDOKU_SQUARE_SIDE];
                String[][] rowSecondSquare = new String[SUDOKU_SQUARE_SIDE][SUDOKU_SQUARE_SIDE];
                String[][] rowThirdSquare = new String[SUDOKU_SQUARE_SIDE][SUDOKU_SQUARE_SIDE];

                for (int squareColumnIndex = 0; squareColumnIndex < SUDOKU_SQUARE_SIDE; ++squareColumnIndex) {
                    for (int row = 0; row < SUDOKU_SQUARE_SIDE; ++row) {
                        final int SECOND_SQUARE_COLUMN_START = squareColumnIndex + SUDOKU_SQUARE_SIDE;
                        final int THIRD_SQUARE_COLUMN_START = SECOND_SQUARE_COLUMN_START + SUDOKU_SQUARE_SIDE;

                        rowFirstSquare[row][squareColumnIndex] = rows[row][squareColumnIndex];
                        rowSecondSquare[row][squareColumnIndex] = rows[row][SECOND_SQUARE_COLUMN_START];
                        rowThirdSquare[row][squareColumnIndex] = rows[row][THIRD_SQUARE_COLUMN_START];
                    }
                }

                IntFunction<MatrixSubSquarePart> getSubSquarePart = ordinal -> MatrixSubSquarePart.values()[ordinal];
                matrix.put(getSubSquarePart.apply(subSquarePart.ordinal()), rowFirstSquare);
                matrix.put(getSubSquarePart.apply(subSquarePart.ordinal() + 1), rowSecondSquare);
                matrix.put(getSubSquarePart.apply(subSquarePart.ordinal() + 2), rowThirdSquare);
            }
        }
    }
}
