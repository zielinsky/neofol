grammar Neofol;

@header {
package raw.antlr;
}

program
: letExpression EOF
;

letExpression
: LET IDENTIFIER typedBinding* EQUAL body      # inferredLet
| LET typedBinding typedBinding* EQUAL body    # annotatedLet
;

body
: letExpression
| INTEGER
| STRING_LITERAL
| TRUE
| FALSE
| IDENTIFIER
;

typedBinding
: LEFT_PAREN IDENTIFIER COLON typeExpression RIGHT_PAREN
;

typeExpression
: TYPE_IDENTIFIER
;

LET
: 'let'
;

EQUAL
: '='
;

COLON
: ':'
;

LEFT_PAREN
: '('
;

RIGHT_PAREN
: ')'
;

TRUE
: 'true'
;

FALSE
: 'false'
;

INTEGER
: [0-9]+
;

STRING_LITERAL
: '"' ~["\r\n]* '"'
;

IDENTIFIER
: [a-z_] [a-zA-Z0-9_]*
;

TYPE_IDENTIFIER
: [A-Z] [a-zA-Z0-9_]*
;

WHITESPACE
: [ \t\r\n]+ -> skip
;
