package syntax;

import java.util.HashSet;

/**
 * LambdaTerms are terms in the lambda calculus.
 */
public abstract class LambdaTerm extends Term {

    public static final String LAM = "\\";
    public static final String DOT = ".";

    /**
     * Copy a LambdaTerm. Contained name objects need not be deeply copied.
     * @return a copy of this LambdaTerm.
     */
    public abstract LambdaTerm copy();
}
