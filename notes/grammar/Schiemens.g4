grammar Schiemens;

///////////////////////////////////////////////////
// root of the program
///////////////////////////////////////////////////

compilationunit: topLevelUnitList EOF ;
topLevelUnitList: topLevelUnit topLevelUnitList
    | /* EPSILON */ ;
topLevelUnit: program ;
program: classdec
	| func
	| label
	| global ;

///////////////////////////////////////////////////
// global variables and labels
///////////////////////////////////////////////////

label: LABEL ID smtblock ;
global: GLOBAL typedesc '=' globalTail SEMI ;
globalTail: constant
    | constlist ;

///////////////////////////////////////////////////
// class description
///////////////////////////////////////////////////

classdec: CLASS ID classpoly classbody ;
classpoly: EXTENDS ID
    | /* EPSILON */ ;
classbody: '{' classinsidelist '}' ;
classinside: method
	| member
	| classconstr ;
classinsidelist: classvisibility classinside classinsidelist
	| /* EPSILON */ ;
classvisibility: PUBLIC
    | PROTECTED
    | PRIVATE ;
method: METH rtype ID fparam smtblock ;
member: MEMBER typemodifier typedesc SEMI ;
classconstr: CONSTR fparam smtblock ;
typemodifier: STATIC
    | FINAL
    | /* EPSILON */ ;

///////////////////////////////////////////////////
// general function description
///////////////////////////////////////////////////

func: FUNC rtype ID fparam smtblock ;
fparam: '(' arglist ')' ;
arglist: typedesc typedesctail
    | /* EPSILON */ ;

///////////////////////////////////////////////////
// datatype descriptions
///////////////////////////////////////////////////

typedesctail: ',' typedesc typedesctail
    | /* EPSILON */ ;
typedesc: type farraytail ID ;
type: 'int'
	| 'double'
	| 'char'
	| 'string'
	| 'boolean'
	| ID ;
rtype: type emptyarraytail
	| VOID ;
emptyarraytail: emptyarray emptyarraytail
	| /* EPSILON */ ;
emptyarray: '.' '[' ']' ;
farraytail: farray farraytail
	| /* EPSILON */ ;
farray: '[' INT_LIT ']'
	| '.' '[' ']' ;

///////////////////////////////////////////////////
// statements
///////////////////////////////////////////////////

smtlist: smt smtlist
	| /* EPSILON */ ;
smt: assignsmt SEMI
	| ifsmt
	| whilesmt
	| dowhilesmt SEMI
	| forsmt
	| switchsmt
	| fcall SEMI
	| methcall SEMI
	| supercall SEMI
	| vardec SEMI
	| jumpstmt SEMI ;
ifsmt: IF check smtblock elsepart ;
elsepart: ELSE smtblock
	| /* EPSILON */ ;
whilesmt: WHILE check smtblock ;
dowhilesmt: DO smtblock WHILE check SEMI;
forsmt: FOR '(' forstart SEMI formiddle SEMI forend ')' smtblock ;
forstart: vardec
	| assignsmt
	| /* EPSILON */ ;
formiddle: expr
    | /* EPSILON */ ;
forend: assignsmt SEMI
    | /* EPSILON */ ;
switchsmt: SWITCH check '{' caseinside caseinsidelist '}' ;
caseinside: CASE constant ':' smtblock
	| DEFAULT ':' smtblock ;
caseinsidelist: caseinside caseinsidelist
	| /* EPSILON */ ;
jumpstmt: CONTINUE
	| BREAK
	| HOME
	| RETURN expr
	| GOTO ID ;
check: '(' expr ')' ;
smtblock: '{' smtlist '}' ;

///////////////////////////////////////////////////
// variable access and assignments
///////////////////////////////////////////////////

assignsmt: variable assignsmtTail ;
assignsmtTail: assignop expr
    | postop ;
vardec: VAR vardecComma vardecCommaTail ;
vardecComma: typemodifier typedesc vardecTail ;
vardecCommaTail: vardecComma vardecCommaTailRest
    | /* EPSILON */ ;
vardecCommaTailRest: ',' vardecComma vardecCommaTailRest
    | /* EPSILON */ ;
vardecTail: '=' vardecTailP
    | /* EPSILON */ ;
vardecTailP: expr
	| list ;
variable: variableTail
    | THIS index idnestTail ;
variableTail: ID index idnestTail ;
idnest: '.' ID index ;
idnestTail: idnest idnestTail
	| /* EPSILON */ ;
index: '[' expr ']'
	| /* EPSILON */ ;

///////////////////////////////////////////////////
// function, method, super and new - class
///////////////////////////////////////////////////

supercall: SUPER fcallheader ;
fargs: expr fargstail
	| /* EPSILON */ ;
fargstail: ',' expr fargstail
	| /* EPSILON */ ;
fcall: FC ID fcallheader fcallTail ;
methcall: MC ID index methcallend ;
methcallTail: idnest
	| ':' ID fcallheader ;
methcallend: methcallTail methcallend
	| /* EPSILON */ ;

fcallheader: '(' fargs ')' ;
newobj: NEW ID fcallheader fcallTail ;
fcallTail: index methcallend ;

///////////////////////////////////////////////////
// expressions
///////////////////////////////////////////////////

//A -> Aa | b
// <=>
//A -> bA'
//A' -> aA' | e
// E -> E + T | T
// <=>
// E -> TE'
// E' -> +TE'

expr: andexpr exprP ;
exprP: orop andexpr exprP
	| /* EPSILON */ ;
andexpr: eqexpr andexprP ;
andexprP: andop eqexpr andexprP
	| /* EPSILON */ ;
eqexpr: relexpr eqexprP ;
eqexprP: eqop relexpr eqexprP
	| /* EPSILON */ ;
relexpr: addexpr relexprP ;
relexprP: relop addexpr relexprP
	| /* EPSILON */ ;
addexpr: multexpr addexprP ;
addexprP: addop multexpr addexprP
	| /* EPSILON */ ;
multexpr: expoexpr multexprP ;
multexprP: multop expoexpr multexprP
	| /* EPSILON */ ;
expoexpr: unaryexpr expoexprRest ;
expoexprRest: expoop expoexpr
    | /* EPSILON */ ;
unaryexpr: preop cast
	| cast ;
cast: CAST '(' type ')' primary
    | primary ;
primary: variable
	| fcall
	| methcall
	| supercall
	| newobj
	| '(' expr ')'
	| constant ;

constant: INT_LIT
	| HEX_LIT
	| BINARY_LIT
	| OCT_LIT
	| DOUBLE_LIT
	| STRING_LIT
	| CHAR_LIT
	| BOOLEAN_LIT
	| REF_LIT ;

///////////////////////////////////////////////////
// expressionlist
///////////////////////////////////////////////////


list: '{' exprMany '}'
	| '.' '{' list ',' list listTail '}' ;
listTail: ',' list listTail
	| /* EPSILON */ ;
exprMany: expr exprManyTail ;
exprManyTail: ',' expr exprManyTail
	| /* EPSILON */ ;

///////////////////////////////////////////////////
// constantlist
///////////////////////////////////////////////////

constlist: '{' constexprMany '}'
    | '.' '{' constlist constlistTail '}' ;
constlistTail: ',' constlist constlistTail
	| /* EPSILON */ ;
constexprMany: constant constexprManyTail ;
constexprManyTail: ',' constant constexprManyTail
	| /* EPSILON */ ;

///////////////////////////////////////////////////
// operators, keywords and symbols
///////////////////////////////////////////////////

INT_LIT: [0-9]+ ;
DOUBLE_LIT: INT_LIT '.' INT_LIT ;
HEX_LIT: '0x' [0-9a-fA-F]+ ;
BINARY_LIT: '0b' [01]+ ;
OCT_LIT: '0o' [0-7]+ ;
CHAR_LIT: '\'' . '\'' ;
STRING_LIT: '"' .*? '"' ;
BOOLEAN_LIT: 'true'
	| 'false' ;
REF_LIT: 'null' ;

assignop: '=' | '+=' | '-=' | '*=' | '/=' | '%=' | '^=' | '**=' ;
orop: '||' | 'or' ;
andop: '&&' | 'and' ;
eqop: '==' | '!=' ;
relop: '<' | '<=' | '>' | '>=' ;
addop: '+' | '-' ;
multop: '*' | '/' | '%' ;
expoop: '^' | '**' ;
preop: '!' | 'not' | '-' ;
postop: '++' | '--' ;

SEMI: ';' ;
FUNC: 'func' ;
CLASS: 'class' ;
EXTENDS: 'extends' ;
METH: 'meth' ;
CONSTR: 'constr' ;
MEMBER: 'member' ;
THIS: 'this' ;
NEW: 'new' ;
VOID: 'void' ;
VAR: 'var' ;
DO: 'do' ;
WHILE: 'while' ;
FOR: 'for' ;
SWITCH: 'switch' ;
DEFAULT: 'default' ;
CASE: 'case' ;
IF: 'if' ;
ELSE: 'else' ;
RETURN: 'return' ;
HOME: 'home' ;
BREAK: 'break' ;
CONTINUE: 'continue' ;
FC: 'fcall' ;
MC: 'mcall' ;
LABEL: 'label' ;
GOTO: 'goto' ;
CAST: 'cast' ;
PUBLIC: 'public' ;
PROTECTED: 'protected' ;
PRIVATE: 'private' ;
GLOBAL: 'global' ;
STATIC: 'static' ;
FINAL: 'final' ;
SUPER: 'super' ;

ID: [a-zA-Z_][a-zA-Z_0-9]*;
COMMENT: '//' ~[\r\n]* -> skip;
BIG_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\n\r\f]+ -> skip;

