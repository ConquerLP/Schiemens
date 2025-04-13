grammar Schiemens;

//bodys
compilationUnit: (func | classDec | globalVar | label) EOF ;

//function
func: FUNC fHeader fParam block ;
fHeader: returntype arrayGroup* identifier ;
fParam: '(' argList? ')' ;
varDescription: type constArray* identifier ;
argList: varDescription (',' varDescription)* ;

//class
classDec: CLASS identifier
	'{' classInsideGroup* '}' ;
classInsideGroup: classInside+ ;
classInside: classConstructor | classField | method ;
classConstructor: identifier fParam block ;
method: fHeader fParam block ;
classField: typemodifier? varDescription constInit? SEMI ;

//programflow & statements
block: '{' stmt* '}' ;
stmt: ifStmt
    | whileStmt
    | doWhileStmt
    | forStmt
    | switchCase
    | label
    | block
    | varDec SEMI
    | expression SEMI
    | jumpStmt SEMI
    | incDecStmt SEMI
    ;
ifStmt: IF check stmt (ELSE stmt)? ;
whileStmt: WHILE check block ;
doWhileStmt: DO block WHILE check ;
forStmt: FOR '(' forStart SEMI forCheck SEMI forAction ')' block ;
forStart: (varDec | orExpression)? ;
forCheck: orExpression? ;
forAction: orExpression? ;
jumpStmt: BREAK
	| CONTINUE
	| GOTO identifier
	| RETURN orExpression?
	;
label: LABEL identifier block ;
switchCase: SWITCH check '{' caseBlock+ '}' ;
caseBlock: CASE constant ':' block
	| DEFAULT ':' block
	;
check: '(' orExpression ')' ;

//declaration & assignment
varDec: typemodifier? varDescription ('=' (orExpression | list))? ;

//static declarations
globalVar: GLOBAL typemodifier? varDescription constInit SEMI ;

//expressions
lh_expression: base_lh postfix_lh* ;
base_lh: identifier | THIS ;
postfix_lh: '.' identifier
    | arrayAccess
    ;
expression: lh_expression assignOP orExpression | list ;
orExpression: andExpression orTail ;
orTail: (orOP andExpression)* ;
andExpression: equalityExpression andTail ;
andTail: (andOP equalityExpression)* ;
equalityExpression: relationalExpression equalityTail ;
equalityTail: (eqOP relationalExpression)* ;
relationalExpression: additiveExpression relationTail ;
relationTail: (relOP additiveExpression)* ;
additiveExpression: multiplicativeExpression additiveTail ;
additiveTail: (addOP multiplicativeExpression)* ;
multiplicativeExpression: exponentiationExpression multiplicativeTail ;
multiplicativeTail: (multOP exponentiationExpression)* ;
exponentiationExpression: unaryExpression exponentiationTail ;
exponentiationTail: (expOP unaryExpression)* ;

unaryExpression: preOP postExpression | postExpression ;
postExpression: primary ;
validPostfix_expression: identifier
    | THIS
    | identifier postfix_lh+
    | THIS postfix_lh+
    ;

primary: base_primary postfix_expression* ;
base_primary: '(' orExpression ')'
    | NEW identifier fArgs
    | identifier
    | THIS
    | constant
    ;
postfix_expression: '.' identifier fArgs
    | '.' identifier
    | arrayAccess
    ;
incDecStmt: validPostfix_expression postOP ;
list: '{' expressionMany '}'
    | '{' subList (',' subList)+ '}' ;
subList: '{' expressionMany '}' ;
expressionMany: orExpression (',' orExpression)* ;
fArgs: '(' expressionMany? ')' ;
arrayAccess: '[' orExpression ']' ;

//const & type
returntype: VOID | type ;

constList: '{' constantMany '}'
    | '{' constSubList (',' constSubList)+ '}' ;
constSubList: '{' constantMany '}' ;
constInit: constList | constant ;
constantMany: constant (',' constant)* ;
constArray: '[' constant ']';

constant: doubleRule | intRule | stringRule | charRule | booleanRule | refRule | octRule | hexRule | binaryRule ;
type: 'double' | 'int' | 'string' | 'char' | 'boolean' | identifier	;
identifier: ID ;
typemodifier: FINAL | STATIC ;

doubleRule: DOUBLE_LIT ;
intRule: INT_LIT ;
octRule: OCT_LIT ;
hexRule: HEX_LIT ;
binaryRule: BOOL_LIT ;
stringRule: STRING_LIT ;
charRule: CHAR_LIT ;
booleanRule: TRUE | FALSE ;
refRule: NULL ;

assignOP: '=' | '+=' | '-=' | '*=' | '/=' | '%=' | '^=' | '**=' ;
orOP: '||' | 'or' ;
andOP: '&&' | 'and' ;
eqOP: '==' | '!=' ;
relOP: '<' | '<=' | '>' | '>=' ;
addOP: '+' | '-' ;
multOP: '*' | '/' | '%' ;
expOP: '^' | '**' ;
preOP: '!' | 'not' | '-' | '+' ;
postOP: '++' | '--' ;
arrayGroup: ('[' ']') ;

//Tökens
CLASS: 'class' ;
VOID: 'void' ;
FUNC: 'func' ;
GLOBAL: 'global' ;

FINAL: 'final' ;
STATIC: 'static' ;

IF: 'if' ;
ELSE: 'else' ;
WHILE: 'while';
DO: 'do' ;
FOR: 'for' ;
CONTINUE: 'continue' ;
BREAK: 'break' ;
GOTO: 'goto' ;
RETURN: 'return' ;
LABEL: 'label' ;
SWITCH: 'switch' ;
CASE: 'case' ;
DEFAULT: 'default' ;

TRUE: 'true' ;
FALSE: 'false' ;
THIS: 'this' ;
NULL: 'null' ;
NEW: 'new' ;

//MISC
INT_LIT: [0-9]+ ;
DOUBLE_LIT: INT_LIT '.' INT_LIT* ;
HEX_LIT: '0x' [0-9a-fA-F]+ ;
BOOL_LIT: '0b' [01]+ ;
OCT_LIT: '0' [0-7]+ ;
CHAR_LIT: '\'' . '\'' ;
STRING_LIT: '"' .*? '"' ;
SEMI: ';' ;

ID: [a-zA-Z_][a-zA-Z_0-9]* ;
COMMENT: '//' ~[\r\n]* -> skip ;
BIG_COMMENT: '/*' .*? '*/' -> skip ;
WS: [ \t\n\r\f]+ -> skip ;