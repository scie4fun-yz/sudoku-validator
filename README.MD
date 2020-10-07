## Sudoku Validator Command Line Tool (Tool)


**Sudoku Validator Command Line Tool environment requirements**:

- Java 8+;
- Gradle 6.3/6.5.

**Using**:

- download ZIP-file from [GitHub repository](https://github.com/scie4fun-yz/sudoku-validator) and extract it into a directory you want on a local machine;
- open terminal/command line and navigate it to directory you've selected at the previous step;
- use gradle to build a project and be able to run the Tool;
- run Tool validate.bat file, using next command.

**Building a project**.

Run next command in terminal/command line:
```
gradle clean build
```
Once its building being complete you'll be ready to run the Tool itself

**Running a program:**

To run a program you need a text file with Sudoku Matrix inside, which matches CSV format.

Let's suppose your file name is "puzzleName.txt", so you need to run next command to validate it:

```
validate.bat <full-path to "puzzleName.txt">
```
You can validate several files with one command just adding their full-path one by one with space as a separator:

```
validate.bat <full-path to "puzzleName.txt"> <full-path to "puzzleName1"> <full-path to "puzzleName2.csv">
```

**Test report opening:**

This time instead of running validate.bat, launch **report.bat** - it will open test report HTML-page, generated when project was build with gradle:
```
report.bat
```