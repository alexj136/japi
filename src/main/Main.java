package main;

import parser.*;
import syntax.PiTerm;
import utils.Triple;
import interpreter.Interpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;

public class Main {

    private static final String help =
            "Usage: japi [OPTIONS] [FILENAME]";

    /**
     * Main method parses and pretty-prints the contents of the japi source file
     * whose name is provided as the sole command line argument. It then
     * translates it into a representation with integer names, and prints that.
     * @param args The command line arguments - should be a single file name
     */
    public static void main(String[] args) {

        // Check arguments were supplied correctly
        if(args.length < 1) {
            System.out.println(Main.help);
            return;
        }

        HashSet<String> options = new HashSet<String>(Arrays.asList(
                Arrays.copyOfRange(args, 0, args.length - 1)));

        File file = new File(args[args.length - 1]);

        // Check the supplied file exists
        if(!file.exists()) {
            System.out.println("File: \'" + file.getAbsolutePath() +
                    "\' does not exist.");
            return;
        }

        // Check the supplied file isn't a directory
        if(file.isDirectory()) {
            System.out.println("File: \'" + file.getAbsolutePath() +
                    "\' is a directory.");
            return;
        }

        // Try to open the file
        FileInputStream input;
        try {
            input = new FileInputStream(file);
        }
        catch(FileNotFoundException e) {
            System.out.println("File: \'" + file.getAbsolutePath() +
                    "\' could not be opened.");
            return;
        }

        // Print either the prettyPrint of the AST or the error message supplied
        // with the ParseException if parsing fails.
        Triple<PiTerm, HashMap<String, Integer>, Integer> res;
        try {
            res = Parser.parseStream(input);
            input.close();
        }
        catch(Exception e) {
            System.out.println("File: \'" + file.getAbsolutePath() +
                    "\' could not be parsed:\n" + e.getMessage());
            return;
        }

        Interpreter interpreter = Interpreter.fromTranslation(res);
        boolean reductionOccurred = true;

        // If we're only doing one reduction, do it and stop
        if(options.contains("-o") || options.contains("--once")) {
            interpreter.doReduction();
            System.out.println(interpreter);
        }

        // Otherwise, reduce until we can't reduce any more
        else {
            while(reductionOccurred) {
                System.out.println(interpreter);
                reductionOccurred = interpreter.doReduction();
            }
        }
    }
}
