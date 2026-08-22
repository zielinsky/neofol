grammar Neofol;

@header {
package raw.antlr;
}

program
: expression EOF
;

expression
: INTEGER
;

INTEGER
: [0-9]+
;

WHITESPACE
: [ \t\r\n]+ -> skip
;
