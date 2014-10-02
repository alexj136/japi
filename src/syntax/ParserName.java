package syntax;

/**
 * ParserNames are a representation of names that simply use the user-supplied
 * string representation of names from source code.
 */
public class ParserName extends Name<String> {

    private String nameString;
    
    /**
     * Construct a new ParserName.
     * @param name the name value parsed from the source file
     * @return a new ParserName object
     */
    public ParserName(String nameString) {
        this.nameString = nameString;
    }

    /**
     * Access this ParserName's underlying String
     * @return this ParserName's underlying String
     */
    public String get() {
        return this.nameString;
    }

    /**
     * Compare this ParserName to another name object.
     * @param other the other name object to compare with
     * @return true if the passed object is a ParserName with the same ID value,
     * false otherwise
     */
    public boolean matches(Name other) throws NameRepresentationException {
        if(!(other instanceof ParserName)) {
            throw new NameRepresentationException("Tried to compare a " +
                    "ParserName with a different representation of names.");
        }
        return other.get().equals(this.nameString);
    }

    /**
     * Change the value of this name to that of 'to', only if the initial value
     * was that of 'from'.
     * @param from change if this object has the same name value
     * @param to if changing, change to this value
     */
    @Override
    public void rename(Name from, Name to) throws NameRepresentationException {
        if(!((from instanceof ParserName) && (to instanceof ParserName))) {
            throw new NameRepresentationException("Tried to rename a " + 
                    "ParserName to another representation of names.");
        }
        if(this.matches(from)) {
            this.nameString = ((ParserName) to).get();
        }
    }

    /**
     * Get a pretty-printout for this ParserName
     * @param indentationLevel this parameter is ignored
     * @return a pretty-printout for this ParserName
     */
    @Override
    public String prettyPrint(int indentationLevel) {
        return this.nameString;
    }
}
