package main;

import parser.*;
import syntax.PiTerm;
import interpreter.SyntaxTranslator;
import interpreter.SyntaxTranslationResult;
import interpreter.Interpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import syntax.LambdaTerm;
import syntax.Variable;
import syntax.Application;
import syntax.Abstraction;
import interpreter.LambdaReducer;
public class Main {

    /**
     * Main method parses and pretty-prints the contents of the japi source file
     * whose name is provided as the sole command line argument. It then
     * translates it into a representation with integer names, and prints that.
     * @param args The command line arguments - should be a single file name
     */
    public static void main(String[] args) {

        // Check arguments were supplied correctly
        if(!(args.length == 1)) {
            System.out.println("Please provide exactly one filename.");
            return;
        }

        File file = new File(args[0]);

        // Check the supplied file exists
        if(!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        // Check the supplied file isn't a directory
        if(file.isDirectory()) {
            System.out.println("File is a directory.");
            return;
        }

        // Try to open the file
        FileInputStream input;
        try {
            input = new FileInputStream(file);
        }
        catch(FileNotFoundException e) {
            System.out.println("File could not be opened.");
            return;
        }

        // Print either the prettyPrint of the AST or the error message supplied
        // with the ParseException if parsing fails.
        PiTerm<String> term;
        try {
            term = Parser.parseStream(input);
        }
        catch(ParseException e) {
            System.out.println("Could not parse the file:\n" + e.getMessage());
            return;
        }
        SyntaxTranslationResult res = SyntaxTranslator.translate(term);
        Interpreter interpreter = Interpreter.fromTranslation(res);
        System.out.println(interpreter);
        boolean reductionOccurred = true;
        while(reductionOccurred) {
            reductionOccurred = interpreter.doReduction();
            if(reductionOccurred) { System.out.println(interpreter); }
        }
    }
}
