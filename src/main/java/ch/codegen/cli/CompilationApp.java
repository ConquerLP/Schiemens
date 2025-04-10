package ch.codegen.cli;

import java.util.Arrays;

public class CompilationApp {

    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);
    }

}