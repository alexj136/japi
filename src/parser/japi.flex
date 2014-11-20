package parser;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%

%public
%class Lexer
%cup
%line
%column
%eofval{ return symbol(Symbols.EOF); %eofval}

%{
    StringBuffer string = new StringBuffer();

    public Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }

    public Symbol symbol(int type, String text) {
        return new Symbol(type, yyline, yycolumn, text);
    }
%}

Ident   = [a-z_]+
NewLine = \r|\n|\r\n;
Space   = {NewLine} | [ \t\f]
Comment =

%%

<YYINITIAL>{

"<"     { return symbol(LANGLE);          }
">"     { return symbol(RANGLE);          }
"("     { return symbol(LPAREN);          }
")"     { return symbol(RPAREN);          }
"["     { return symbol(LSQUAR);          }
"]"     { return symbol(RSQUAR);          }
"{"     { return symbol(LCURLY);          }
"}"     { return symbol(RCURLY);          }
"."     { return symbol(DOT);             }
","     { return symbol(COMMA);           }
"+"     { return symbol(SUM);             }
"|"     { return symbol(BAR);             }
"new"   { return symbol(NEW);             }
"in"    { return symbol(IN);              }
"!"     { return symbol(BANG);            }
"0"     { return symbol(ZERO);            }
"\\"    { return symbol(BSLASH);          }
"->"    { return symbol(ARROW);           }
{Ident} { return symbol(IDENT, yytext()); }

{Space} { /* ignore */ }

.|\n    { throw new Error("Illegal character <"+ yytext()+">"); }
