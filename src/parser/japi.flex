package parser;

import utils.Pair;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.io.InputStreamReader;
import java.util.HashMap;

%%

%public
%class Lexer
%cup
%line
%column

%eofval{
    return symbol(sym.EOF);
%eofval}

%{
    // For symbol generation
    private ComplexSymbolFactory csf;

    // Used in converting identifier strings to integers
    private HashMap<String, Integer> nameMap;
    private int nextAvailableName;

    /**
     * Construct a new Lexer.
     * @param input the InputStreamReader to take input from
     * @param csf ComplexSymbolFactory for symbol generation
     */
    public Lexer(InputStreamReader input, ComplexSymbolFactory csf) {
        this(input);
        this.csf = csf;
        this.nameMap = new HashMap<String, Integer>();
        this.nextAvailableName = 0;
    }

    /**
     * Access the nameMap and nextAvailableName for subsequent usage.
     * @return the nameMap and nextAvailableName as a pair
     */
    public Pair<HashMap<String, Integer>, Integer> getNameInfo() {
        return Pair.make(this.nameMap, this.nextAvailableName);
    }

    /**
     * Construct a new Symbol containing the symbol type, not caring about the
     * text.
     * @param type the token type
     * @return a new Symbol with type information
     */
    public Symbol symbol(int type) {
        Pair<Location, Location> loc = this.getLocation();
        return csf.newSymbol(sym.terminalNames[type], type, loc.frst, loc.scnd);
    }

    /**
     * Construct a new Symbol containing the symbol type and scanned text.
     * @param type the token type
     * @param name the token text
     * @return a new Symbol with type and text information. The text information
     * is an int that is a key in the nameMap, and can be used to look up the
     * actual String value.
     */
    public Symbol symbol(int type, String name) {
        Pair<Location, Location> loc = this.getLocation();
        return csf.newSymbol(sym.terminalNames[type], type, loc.frst, loc.scnd,
                this.lease(name));
    }

    /**
     * Obtain the start and end locations of the current token.
     * @return the start and end locations of the current token
     */
    public Pair<Location, Location> getLocation() {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right = new Location(yyline + 1, yycolumn + yylength(),
                yychar + yylength());
        return Pair.make(left, right);
    }

    /**
     * Convert an identifier string into an integer. If we haven't seen the
     * string before, associate the string with an unused integer, returning
     * that integer. If we have seen the string before, return the integer
     * already associated with it.
     * @param strName the String to use to obtain an intname
     * @return an int that corresponds to the given String name
     */
    public int lease(String strName) {
        if(this.nameMap.containsKey(strName)) {
            return this.nameMap.get(strName);
        }
        else {
            int intName = nextAvailableName;
            nextAvailableName++;
            this.nameMap.put(strName, intName);
            return intName;
        }
    }
%}

Ident   = [a-z_]+
NewLine = \r|\n|\r\n;
Space   = {NewLine} | [ \t\f]
Comment = "//" [^\r\n]* {NewLine}?

%%

<YYINITIAL>{

    "<"       { return symbol(sym.LANGLE); }
    ">"       { return symbol(sym.RANGLE); }
    "("       { return symbol(sym.LPAREN); }
    ")"       { return symbol(sym.RPAREN); }
    "["       { return symbol(sym.LSQUAR); }
    "]"       { return symbol(sym.RSQUAR); }
    "{"       { return symbol(sym.LCURLY); }
    "}"       { return symbol(sym.RCURLY); }
    "."       { return symbol(sym.DOT);    }
    ","       { return symbol(sym.COMMA);  }
    "+"       { return symbol(sym.SUM);    }
    "|"       { return symbol(sym.BAR);    }
    "new"     { return symbol(sym.NEW);    }
    "in"      { return symbol(sym.IN);     }
    "!"       { return symbol(sym.BANG);   }
    "0"       { return symbol(sym.ZERO);   }
    "\\"      { return symbol(sym.BSLASH); }
    "->"      { return symbol(sym.ARROW);  }

    {Ident}   { return symbol(sym.IDENT, yytext()); }

    {Space}   { /* ignore */ }

    {Comment} { /* ignore */ }

    [^]|\n    { throw new Error("Illegal character <" + yytext() + ">"); }
}
