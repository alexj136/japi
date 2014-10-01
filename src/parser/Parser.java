package parser;

import genparser.GenParser;
import genparser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Parser {
    public static void main(String[] args)
    throws ParseException, FileNotFoundException {
        if(!(args.length == 1)) {
            System.out.println("Please provide exactly one filename.");
            return;
        }
        GenParser p = new GenParser(new FileInputStream(args[0]));
        p.Expression();
    }
}
