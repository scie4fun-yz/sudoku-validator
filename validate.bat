@echo off

if exist "build/libs/sudoku-validator-1.0-SNAPSHOT.jar" (
    java -jar build/libs/sudoku-validator-1.0-SNAPSHOT.jar %*
) else (
    echo "Build project first. There is no *.jar file to launch yet"
)
