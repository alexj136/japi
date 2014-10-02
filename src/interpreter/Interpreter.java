package interpreter;

import syntax.*;

/**
 * The Interpreter class contains methods used for interpreting pi calculus
 * ASTs.
 */
public class Interpreter {

    private Term term;

    public Interpreter(Term term) {
        this.term = term;
    }

}
