package syntax;

import java.util.HashSet;

/**
 * LambdaTerms are terms in the lambda calculus.
 */
public abstract class LambdaTerm<T> extends Term<T> {

    /**
     * Copy a LambdaTerm. Contained name objects need not be deeply copied.
     * @return a copy of this LambdaTerm.
     */
    public abstract LambdaTerm<T> copy();

    /**
     * Enumerate the free variables in this LambdaTerm.
     * @return a HashSet of all the free variable names in this LambdaTerm
     */
    public abstract HashSet<T> freeVars();

    /**
     * Enumerate the variable names that have their binders within the given
     * LambdaTerm.
     * @return a HashSet of all the variable names that have their binders
     * within the given LambdaTerm
     */
    public abstract HashSet<T> binders();

    /**
     * Rename all free occurences of the name 'from' to the name 'to'.
     * @param from names of this value are renamed
     * @param to renamed names are renamed to this value
     */
    public abstract void renameFree(T from, T to);

    /**
     * Rename all occurences of the name 'from' to the name 'to', in and after
     * the first abstraction that binds this name.
     * @param from names of this value are renamed
     * @param to renamed names are renamed to this value
     */
    public abstract void renameNonFree(T from, T to);
}
