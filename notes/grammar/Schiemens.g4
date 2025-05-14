grammar Schiemens;

compilationunit: PUBLIC1 programList programroot EOF ;
programroot: PUBLIC1 programList
    | namespaceList ;

program: classdec
	| func
	| label
	| enums
	| global ;
programList: program programList
	| /* EPSILON */ ;
namespace: NAMESPACE ID '{' programList '}' ;
namespaceList: namespace namespaceList
	| /* EPSILON */ ;
label: LABEL ID smtblock ;
enums: ENUM ID '{' enuminsideList '}' SEMI ;
enuminsideList: enuminside enuminsideListTail
    | /* EPSILON */ ;
enuminsideListTail: ',' enuminside enuminsideListTail
    | /* EPSILON */ ;
enuminside: ID enuminsideTail ;
enuminsideTail: '=' constant
    | /* EPSILON */ ;

global: GLOBAL typedesc '=' globalTail SEMI ;
globalTail: constant
    | constlist ;

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

func: FUNC rtype ID fparam smtblock ;
fparam: '(' arglist ')' ;
arglist: typedesc typedesctail
	| /* EPSILON */ ;
typedesc: namespaceAcces type farraytail ID ;
typedesctail: ',' typedesc
	| /* EPSILON */ ;
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
	| vardec SEMI
	| jumpstmt SEMI
	| enums ;
ifsmt: IF check smtblock elsepart ;
elsepart: ELSE smtblock
	| /* EPSILON */ ;
whilesmt: WHILE check smtblock ;
dowhilesmt: DO smtblock WHILE check ;
forsmt: FOR '(' forstart SEMI formiddle SEMI forend SEMI ')' smtblock ;
forstart: vardec
	| assignsmt
	| /* EPSILON */ ;
formiddle: expr
    | /* EPSILON */ ;
forend: assignsmt
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

assignsmt: variable assignop expr ;
vardec: VAR vardecComma vardecCommaTail ;
vardecComma: typemodifier typedesc vardecTail ;
vardecCommaTail: ',' vardecComma vardecCommaTail
    | /* EPSILON */ ;
vardecTail: '=' vardecTailP
    | /* EPSILON */ ;
vardecTailP: expr
	| list ;
variable: namespaceAcces variableTail
    | THIS index idnestTail ;
variableTail: ID index idnestTail
    | ENUM1 ':' ID '.' ID ;
namespaceAcces: NAMESPACE1 '::' ID
	| /* EPSILON */ ;

//A -> Aa | b
// <=>
//A -> bA'
//A' -> aA' | e
// E -> E + T | T
// <=>
// E -> TE'
// E' -> +TE'

idnest: '.' ID index ;
idnestTail: idnest idnestTail
	| /* EPSILON */ ;

index: '[' expr ']'
	| /* EPSILON */ ;

fargs: expr fargstail
	| /* EPSILON */ ;
fargstail: ',' expr fargstail
	| /* EPSILON */ ;
fcall: FC namespaceAcces ID fcallheader fcallTail ;
methcall: MC ID index mathcallend ;
methcallTail: idnest
	| ':' ID fcallheader ;
mathcallend: methcallTail mathcallend
	| /* EPSILON */ ;

fcallheader: '(' fargs ')' ;
newobj: NEW namespaceAcces ID fcallheader fcallTail ;
fcallTail: index mathcallend ;

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
expoexpr: unaryexpr expoexprP ;
expoexprP: expoop unaryexpr expoexprP
	| /* EPSILON */ ;
unaryexpr: preop cast
	| cast ;
primary: variable
	| fcall
	| methcall
	| newobj
	| '(' expr ')'
	| constant ;
cast: CAST '(' type ')' primary
    | primary ;

constant: INT_LIT
	| HEX_LIT
	| BINARY_LIT
	| OCT_LIT
	| DOUBLE_LIT
	| STRING_LIT
	| CHAR_LIT
	| BOOLEAN_LIT
	| REF_LIT ;

list: '{' exprMany '}'
	| '.' '{' list ',' list listTail '}' ;
listTail: ',' list listTail
	| /* EPSILON */ ;
exprMany: expr exprManyTail ;
exprManyTail: ',' expr exprManyTail
	| /* EPSILON */ ;

constlist: '{' constexprMany '}'
    | '.' '{' constlist ',' constlist constlistTail '}' ;
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
FC: 'f->' ;
MC: 'm->' ;
LABEL: 'label' ;
GOTO: 'goto' ;
CAST: 'cast' ;
ENUM: 'enum' ;
ENUM1: 'ENUM' ;
PUBLIC: 'public' ;
PROTECTED: 'protected' ;
PRIVATE: 'private' ;
GLOBAL: 'global' ;
STATIC: 'static' ;
FINAL: 'final' ;
NAMESPACE: 'namespace' ;
NAMESPACE1: '@' ;
PUBLIC1: 'public:' ;

ID: [a-zA-Z_][a-zA-Z_0-9]*;
COMMENT: '//' ~[\r\n]* -> skip;
BIG_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\n\r\f]+ -> skip;

