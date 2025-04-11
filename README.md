# 🔧 Java Compiler Project

A fully custom-built compiler in Java, including a lexer, parser, AST, code generator, virtual machine, and command-line interface.

## 📦 Module Structure

```text
src/
├── cli/                → Command-line interface (CLI)
│   ├── CompilationApp.java
│   ├── ArgumentParser.java
│   └── CliLogger.java
├── compiler/           → Compiler logic
│   ├── CompilationEngine.java
│   ├── CompilerLogger.java
│   ├── Lexer.java, Parser.java, ASTNode.java, ...
├── util/               → Helper functions & logging
│   └── CompilationOptions.java
```

```
java CompilationApp -help

# Compile
java CompilationApp -i src/main.sc -o out/main.pain

# Print AST
java CompilationApp -i src/main.sc -ast

# Compile with timing and logging
java CompilationApp -i src/main.sc -o out/main.pain -time -log
```
---

## 🧪 Usage

```bash
# Show help
java CompilationApp -help

# Compile a complete project
java CompilationApp -i src/main.sc -o out/main.pain

# Print AST only
java CompilationApp -i src/main.sc -ast

# Verify syntax without generating output
java CompilationApp -i src/main.sc -verify

# Compile with time measurement and logging
java CompilationApp -i src/main.sc -o out/main.pain -time -log

# Compile separately
java CompilationApp -i src/utils.sc -o out/utils.pain -c
java CompilationApp -i src/main.sc -o out/main.pain -c

# Generate x86 assembly output
java CompilationApp -i src/main.sc -o out/main.pain -target asm
```

---

## ⚙️ Options (`CompilationOptions`)

| Option      | Parameter           | Description                                                                                          |
|-------------|---------------------|------------------------------------------------------------------------------------------------------|
| `-help`     | *(none)*            | Displays this help screen                                                                            |
| `-i`        | `<source-file(s)>`  | Input source files                                                                                   |
| `-o`        | `<output-file>`     | Output file                                                                                          |
| `-c`        | *(none)*            | Compile each file separately                                                                         |
| `-version`  | *(none)*            | Shows the current version                                                                            |
| `-time`     | *(none)*            | Displays compilation time                                                                            |
| `-ast`      | *(none)*            | Writes AST to file                                                                                   |
| `-log`      | *(none)*            | Keeps the log file after compilation                                                                 |
| `-verify`   | *(none)*            | Syntax check only, no code generation                                                                |
| `-target`   | `<target-name>`     | Defines the target system to compile to (default: `sm`)<br/> use `asm` to generate assembly x86 code |

---

## 📊 Architecture – Class Diagram

<details>
<summary><strong>Show Diagram</strong></summary>

```mermaid
classDiagram
    %% === Helper ===
    class PositionInFile {
        +int row
        +int col
        +PositionInFile(int row, int col)
        +String toString()
    }

    %% === Exceptions ===
    class CompilerException {
        +CompilerException(String message)
    }

    class LexicalException {
        +LexicalException(String message, PositionInFile pos)
    }

    class ParseException {
        +ParseException(String message, PositionInFile pos)
    }

    %% === CLI ===
    class CompilationApp {
        +main(String[] args) void
        -handleHelpFlag() void
        -handleVersionFlag() void
        -shouldRunCompilation() boolean
        -printError(String message) void
    }

    class ArgumentParser {
        +parse(String[] args) void
        -expectOption(String[] args, int index) String
        -isValidFlag(String flag) boolean
    }

    class CliLogger {
        +log(String message) void
        +error(String message) void
        +flushAndClose() void
        -Path logFile
        -BufferedWriter writer
    }

    %% === Compiler Core ===
    class CompilationEngine {
        <<static>>
        +run() void
        -prepareFiles() void
        -runCompilerPipeline() void
        -cleanupTemporaryFiles() void
        -mergeLogs(List~Path~ logFiles) void
        -checkAndAbortIfErrors(CompilerLogger logger, String phase) void
    }

    class Compiler {
        +compileUnit(Path source) void
        -Path runPreprocessor(Path source) throws CompilerException
        -List~Token~ runLexer(Path processedFile) throws LexicalException, CompilerException
        -ParseTree runParser(List~Token~ tokens) throws ParseException
        -generateOutput(ASTNode ast) void
        -CompilerLogger logger
    }

    class Preprocessor {
        +Path process(Path source) throws CompilerException
        -String resolveInclude(String path)
        -boolean isAlreadyIncluded(String path)
        -Set~String~ includedFiles
    }

    class CompilerLogger {
        +log(String message, Path file, PositionInFile pos) void
        +warn(String message, Path file, PositionInFile pos) void
        +error(String message, Path file, PositionInFile pos) void
        +flushAndClose() void
        +int getErrorCount() int
        +List~String~ getErrorMessages()
        +void printErrorsToConsole(int max)
        +Path getLogFile()
        -List~String~ errors
        -Path logFile
        -BufferedWriter writer
    }

    %% === Lexer/Parser ===
    class TokenType {
        <<enum>>
        +KEYWORD_IF
        +IDENTIFIER
        +NUMBER
        +PLUS
        +MINUS
        +EQUAL
        +LPAREN
        +RPAREN
        +EOF
        +...
    }

    class Token {
        +TokenType type
        +String value
        +PositionInFile position
        +Path sourceFile
    }

    class TokenStream {
        +Token peek()
        +Token advance()
        +boolean isAtEnd()
        -List~Token~ tokens
        -int position
    }

    class Lexer {
        <<static>>
        +List~Token~ tokenize(Path sourceFile) throws LexicalException, CompilerException
    }

    class Parser {
        +Parser(TokenStream stream, CompilerLogger logger)
        +ParseTree parseProgram()
        -Token current
        -CompilerLogger logger
        -TokenStream stream
    }

    class ParseTree {
        +String ruleName
        +List~ParseTree~ children
        +Token token
    }

    class ASTNode {
        <<abstract>>
        +PositionInFile position
    }

    %% === Util ===
    class CompilationOptions {
        <<static>>
        +List~Path~ inputFiles
        +Path outputFile
        +boolean compileSeparately
        +boolean printAST
        +boolean dumpLogs
        +boolean printHelp
        +boolean printVersion
        +boolean printTime
        +boolean verifyOnly
        +String target
        +Path logFile
    }

    %% === Relationships ===
    CompilationApp --> ArgumentParser : uses
    ArgumentParser --> CompilationOptions : sets
    CompilationApp --> CompilationEngine : calls run()
    CompilationApp --> CliLogger : uses

    CompilationEngine --> CompilationOptions : reads
    CompilationEngine --> Compiler : delegates to
    CompilationEngine --> CompilerLogger : merges from many
    CompilationEngine --> CliLogger : uses for summary

    Compiler --> Preprocessor : uses
    Compiler --> CompilerLogger : uses
    Compiler --> Parser : uses
    Compiler --> Lexer : uses
    Compiler --> TokenStream : builds
    Compiler --> ParseTree : receives
    Compiler --> ASTNode : converts to
    Compiler --> CompilationOptions : reads
    Compiler --> CompilerException : throws

    Preprocessor --> CompilerException : throws

    Lexer --> Token : creates
    Lexer --> LexicalException : throws
    Lexer --> CompilerException : throws
    Parser --> TokenStream : consumes
    Parser --> CompilerLogger : logs errors
    Parser --> ParseTree : returns
    Parser --> ParseException : throws
    Token --> TokenType
    Token --> PositionInFile
    Token --> CompilationOptions : uses file path
    TokenStream --> Token : manages
    ASTNode --> PositionInFile
    CompilerLogger --> CompilationOptions : uses logFile
    CompilerLogger --> PositionInFile : uses
    CliLogger --> CompilationOptions : uses logFile
    Preprocessor --> CompilationOptions : reads
```

</details>

---

## 🗂 Example Output (with `-log` and `-ast`)

```text
out/
├── main.pain           ← Compiled target (.pain) file
├── main.ast.txt      ← AST dump
├── main.log.txt      ← CLI log file
```

Temporary files like `lexer.tmp`, `parser.tmp` are automatically deleted unless debugging is enabled.

---

## 🧠 Notes

- Only ASTs and related trees are kept in memory.
- Source code, tokens, logs, etc. are written to disk.
- Temporary files go to `temp/`, logs and output to `out/`.

---

## 📍 License & Contributions

> This project is a learning-oriented compiler built in Java. Feel free to fork, test, improve!