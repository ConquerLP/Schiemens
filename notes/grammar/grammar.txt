grammar ReFugg;

//bodys
program: (func | classDec | globalVar | label)* main EOF ;
main: MAIN functionBlock ;

//function
func: FUNC fHeader fParam functionBlock ;
fHeader: returntype arrayGroup* identifier ;
fParam: '(' argList? ')' ;
varDescription: type constArray* identifier ;
argList: varDescription (',' varDescription)* ;

//class
classDec: CLASS identifier poly?
	'{' classInsideGroup* '}' ;
classInsideGroup: visibilty classInside+ ;
classInside: classConstructor | classField | method ;
poly: ISA identifier ;
visibilty: PUBLIC | PRIVATE | PROTECTED ;
classConstructor: CONST identifier fParam functionBlock ;
method: METH fHeader fParam functionBlock ;
classField: FIELD typemodifier? varDescription constInit? SEMI ;

//programflow & statements
functionBlock: '{' functionBlockStmt* '}' ;
functionBlockStmt: stmt | (functionJumpStmt SEMI) ;
loopBlock: '{' loopBlockStmt*  '}' ;
loopBlockStmt: stmt | (loopJumpStmt SEMI) ;
stmt: ifStmt
	| whileStmt
	| doWhileStmt SEMI
	| forStmt
	| switchCase
	| label
	| functionBlock
	| varDec SEMI
	| expression SEMI
	;
ifStmt: IF check functionBlock (ELSE functionBlock)? ;
whileStmt: WHILE check loopBlock ;
doWhileStmt: DO loopBlock WHILE check ;
forStmt: FOR '(' forStart SEMI forCheck SEMI forAction ')' loopBlock ;
forStart: (varDec | orExpression)? ;
forCheck: orExpression? ;
forAction: orExpression? ;
functionJumpStmt: GOTO identifier | RETURN expression? ;
loopJumpStmt: BREAK
	| CONTINUE
	| GOTO identifier
	| RETURN expression?
	;
label: LABEL identifier functionBlock? ;
switchCase: SWITCH check '{' caseBlock+ '}' ;
caseBlock: CASE constExpr ':' functionBlock
	| DEFAULT ':' functionBlock
	;
check: '(' orExpression ')' ;

//declaration & assignment
varDec: typemodifier? varDescription ('=' (orExpression | list))? ;

//static declarations
globalVar: GLOBAL typemodifier? varDescription constInit SEMI ;

constArray: '[' constExpr? ']' ;
constInit: '=' constExpr | constList ;

constList: '{' constExprMany '}'
	| '{' constSubList (',' constSubList)+ '}'
	;
constSubList: '{' constExprMany '}' ;
constExprMany: constExpr (',' constExpr)* ;
constVar: identifier ;
constArrayAccess: identifier ('[' constExpr ']')+ ;

constExpr: constExpr orOP constJoin | constJoin ;
constJoin: constJoin andOP constEQ | constEQ ;
constEQ: constEQ eqOP constRel | constRel ;
constRel: constRel relOP constLogic | constLogic ;
constLogic: constLogic addOP constTerm | constTerm ;
constTerm: constTerm multOP constExpo | constExpo ;
constExpo: constExpo expOP constUnary | constUnary ;
constUnary: preOP constFactor | constFactor ;
constFactor:  constant
	| '(' constExpr ')'
	| constVar
	| constArrayAccess
	;

//expression
arrayAccess: '[' expression ']' ;
methodCall: '.' identifier fArgs arrayAccess* exprTail* ;
newObject: NEW identifier fArgs arrayAccess* exprTail* ;
fCall: identifier fArgs arrayAccess* exprTail* ;

thisAcces: THIS exprTail* ;
varAcces: identifier arrayAccess* exprTail* ;
exprTail: (('.' identifier) | methodCall) arrayAccess* ;
lh_expression: thisAcces | varAcces ;

expression: lh_expression assignOP (expression | list) | orExpression ;
orExpression: orExpression orOP andExpression | andExpression ;
andExpression: andExpression andOP equalityExpression | equalityExpression ;
equalityExpression: equalityExpression eqOP relationalExpression | relationalExpression ;
relationalExpression: relationalExpression relOP additiveExpression | additiveExpression ;
additiveExpression: additiveExpression addOP multiplicativeExpression | multiplicativeExpression ;
multiplicativeExpression: multiplicativeExpression multOP exponentiationExpression | exponentiationExpression ;
exponentiationExpression: exponentiationExpression expOP unaryExpression | unaryExpression ;
unaryExpression: preOP unaryExpression | postExpression ;
postExpression: primary postOP | primary ;
primary: '(' orExpression ')'
    | newObject
    | fCall
    | varAcces
    | thisAcces
    | constant
    ;

list: '{' expressionMany '}'
    | '{' subList (',' subList)+ '}' ;
subList: '{' expressionMany '}' ;
expressionMany: orExpression (',' orExpression)* ;
fArgs: '(' expressionMany? ')' ;

//const & type
returntype: VOID | type ;

constant: doubleRule | intRule | stringRule | charRule | booleanRule | refRule ;
type: 'double' | 'int' | 'string' | 'char' | 'boolean' | identifier	;
identifier: ID ;
typemodifier: FINAL | STATIC ;

doubleRule: DOUBLE_LIT ;
intRule: INT_LIT ;
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
MAIN: 'main:' ;
FUNC: 'func:' ;
CLASS: 'class:' ;
VOID: 'void' ;
FIELD: 'field:' ;
VAR: 'var:' ;
GLOBAL: 'global:' ;
METH: 'method:' ;
CONST: 'constructor:' ;
ISA: 'isa:' ;

PRIVATE: 'private:' ;
PUBLIC: 'public:' ;
PROTECTED: 'protected:' ;
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
LABEL: 'label:' ;
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
DOUBLE_LIT: INT_LIT '.' INT_LIT ;
CHAR_LIT: '\'' . '\'' ;
STRING_LIT: '"' .*? '"' ;
SEMI: ';' ;

ID: [a-zA-Z_][a-zA-Z_0-9]* ;
COMMENT: '//' ~[\r\n]* -> skip ;
BIG_COMMENT: '/*' .*? '*/' -> skip ;
WS: [ \t\n\r\f]+ -> skip ;