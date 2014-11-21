package parser;

import utils.Pair;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java.io.InputStreamReader;

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
    private ComplexSymbolFactory csf;

    public Lexer(InputStreamReader input, ComplexSymbolFactory csf) {
        this(input);
        this.csf = csf;
    }

    public Symbol symbol(int type) {
        Pair<Location, Location> loc = this.getLocation();
        return csf.newSymbol(sym.terminalNames[type], type, loc.frst, loc.scnd);
    }

    public Symbol symbol(int type, String text) {
        Pair<Location, Location> loc = this.getLocation();
        return csf.newSymbol(sym.terminalNames[type], type, loc.frst, loc.scnd,
                text);
    }

    public Pair<Location, Location> getLocation() {
        Location left = new Location(yyline + 1, yycolumn + 1, yychar);
        Location right = new Location(yyline + 1, yycolumn + yylength(),
                yychar + yylength());
        return Pair.make(left, right);
    }
%}

Ident   = [a-z_]+
NewLine = \r|\n|\r\n;
Space   = {NewLine} | [ \t\f]
Comment = "//" [^\r\n]* {NewLine}?

%%

<YYINITIAL>{

    {Comment} { /* ignore */ }

    "<"       { return symbol(sym.LANGLE);          }
    ">"       { return symbol(sym.RANGLE);          }
    "("       { return symbol(sym.LPAREN);          }
    ")"       { return symbol(sym.RPAREN);          }
    "["       { return symbol(sym.LSQUAR);          }
    "]"       { return symbol(sym.RSQUAR);          }
    "{"       { return symbol(sym.LCURLY);          }
    "}"       { return symbol(sym.RCURLY);          }
    "."       { return symbol(sym.DOT);             }
    ","       { return symbol(sym.COMMA);           }
    "+"       { return symbol(sym.SUM);             }
    "|"       { return symbol(sym.BAR);             }
    "new"     { return symbol(sym.NEW);             }
    "in"      { return symbol(sym.IN);              }
    "!"       { return symbol(sym.BANG);            }
    "0"       { return symbol(sym.ZERO);            }
    "\\"      { return symbol(sym.BSLASH);          }
    "->"      { return symbol(sym.ARROW);           }
    {Ident}   { return symbol(sym.IDENT, yytext()); }

    {Space}   { /* ignore */ }

    [^]|\n    { throw new Error("Illegal character <"+ yytext()+">"); }
}
