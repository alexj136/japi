package syntax;

/**
 * A IntName object represents a Pi-Calculus name as an integer.
 */
public class IntName extends SyntaxElement implements Name<Integer> {

    private Integer nameID;

    /**
     * Construct a new Name object.
     * @param nameID the 'name' to store. A name is an integer for the sake of
     * efficiency. User-supplied names may be Strings, but they must be
     * converted into integers.
     * @return a Name object storing the suppied ID/name/whatever you want to
     * call it.
     */
    public IntName(int nameID) {
        this.nameID = new Integer(nameID);
    }

    /**
     * Access the nameID
     * @return the name value.
     */
    public Integer get() {
        return this.nameID;
    }

    /**
     * Compare this Name to another object.
     * @param other the other name object to compare with
     * @return true if the passed object is an IntName with the same ID value,
     * false otherwise
     */
    public boolean matches(Name other) throws NameRepresentationException {
        if(!(other instanceof IntName)) {
            throw new NameRepresentationException("Tried to compare an " +
                    "IntName with a different representation of names.");
        }
        return ((IntName) other).get().intValue() == this.nameID.intValue();
    }

    /**
     * Change the value of this name to that of 'to', only if the initial value
     * was that of 'from'.
     * @param from change if this object has the same name value
     * @param to if changing, change to this value
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        if(!((from instanceof IntName) && (to instanceof IntName))) {
            throw new NameRepresentationException("Tried to rename an " +
                    "IntName to another representation of names.");
        }
        if(this.matches(from)) {
            this.nameID = new Integer(((IntName) to).get().intValue());
        }
    }

    @Override
    public String prettyPrint(int indentationLevel) {
        return this.nameID.toString();
    }
}
