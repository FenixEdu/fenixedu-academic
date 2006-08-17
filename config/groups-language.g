header {
	package net.sourceforge.fenixedu.domain.accessControl.groups.language;
	
	import java.util.List;
	import java.util.ArrayList;
	
	import net.sourceforge.fenixedu.domain.accessControl.Group;
	import net.sourceforge.fenixedu.domain.accessControl.GroupIntersection;
	import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
	import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;

	import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.*;
	import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
	import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionParserException;
	import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;
	
	import net.sourceforge.fenixedu.domain.accessControl.groups.language.parser.*;
}

//
// Lexer
//

class GroupExpressionLexer extends Lexer;

options {
	k = 2; // to distinguish "$" from the operators "$I", "$P", etc
}

LPAREN : '(' ;
RPAREN : ')' ;
COMMA  : ',' ;
DOLLAR : '$' ;
DOT    : '.' ;

INTERSECTION : "&&" ;
UNION        : "||" ;
DIFFERENCE   : "-" ;

I_OP : "$I" ;
P_OP : "$P" ;
E_OP : "$E" ;
N_OP : "$N" ;
C_OP : "$C" ;

NAME : LETTER ( DOLLAR | LETTER | DIGIT )* ;

protected LETTER : ( 'A'..'Z' | 'a'..'z' | '_' ) ;
protected DIGIT  : '0'..'9' ;

// String

STRING 
	: (STRING1 | STRING2) { 
		// strip " or ' from the extremities
		
		String value = #getText();
		value = value.substring(1, value.length() - 1);
		$setText(value);
	} 
	;

protected STRING1 : '\'' (~'\'')* '\'' ;
protected STRING2 : '"' (ESCAPE | ~('\\'|'"'))* '"' ;
protected ESCAPE
	: '\\' (	  'n' { $setText("\n"); }
			| 'r' { $setText("\r"); }
			| 't' { $setText("\t"); }
			| '"' { $setText("\""); }
           )
    ;

// Spaces

SPACE_TFF 
	: (   ' '
		| '\t'
		| '\f'
		| ( '\r' '\n'  | '\r' | '\n'  ) { newline(); }
	  )
	  { 
	  	// we don't care about spaces (being a delimiter is enough)
	  	$setType(Token.SKIP); 
  	  }
	;

//
// Parser
//

class GroupExpressionParser extends Parser;

options {
	buildAST = true;
	defaultErrorHandler = false; // makes exceptions be thrown for all errors
	
	importVocab = GroupExpressionLexer;
	exportVocab = GroupExpression;
}

tokens {
	GROUP;
	OP;
}

{
	private void expand(AST ast, AST ... children) {
		for (AST child : children) {
			if (child == null) { // errors may give us null nodes
				continue;
			}
			
			((CustomAST) ast).expandMarker(((CustomAST) child).marker);
		}
	}
}

start
{
	// change the AST node type for the entire tree
    setASTNodeClass(CustomAST.class.getName());
}
	: difference EOF
	;

difference 
	: union (DIFFERENCE^ union)*
	;

union
	: intersection (UNION^ intersection)*
	;
	
intersection
	: parExpression (INTERSECTION^ parExpression)*
	;
	
parExpression
	: lp:LPAREN! difference rp:RPAREN! {
		expand(#parExpression, #lp, #rp);
	}
	| group
	;

group
	:! n:NAME^ (LPAREN! args:arguments rp:RPAREN!)? {
		#group = #([GROUP, "GROUP"], #n, #args);
		
		expand(#group, #n, #rp);
	}
	;
	
arguments
	: argument (COMMA! argument)*
	| // empty
	;
	
argument
	: freeArgument
	| variableArgument
	| operatorArgument
	;
	
variableArgument
	: d:DOLLAR^ n:NAME (DOT! variableName) {
		expand(#variableArgument, #d);
	}
	;
	
freeArgument
	: NAME
	| STRING
	;
	
variableName
	: propertyName (DOT! variableName)? 
	;
	
propertyName
	:! n:NAME (LPAREN! a:arguments rp:RPAREN!)? {
		if (#a != null) {
			#propertyName = #([OP, "OP"], #n, #a);
		}
		else {
			#propertyName = #n;
		}
		
		expand(#propertyName, #n, #rp);
	}
	;
	
operatorArgument
	:! o:operator (DOT! v:variableName)? {
		if (#v != null) {
			#operatorArgument = #([DOLLAR, "$"], #o, #v);
		}
		else {
			#operatorArgument = #o;
		}
	}
	;
	
operator
	: P_OP^ LPAREN! argument prp:RPAREN! {
		expand(#operator, #prp);
	}
	| I_OP^ LPAREN! argument COMMA! argument irp:RPAREN! {
		expand(#operator, #irp);
	}
	| E_OP^ LPAREN! argument (COMMA! argument)? erp:RPAREN! {
		expand(#operator, #erp);
	}
	| N_OP^ LPAREN! argument (COMMA! argument)? nrp:RPAREN! {
		expand(#operator, #nrp);
	}
	| C_OP^ LPAREN! argument crp:RPAREN! {
		expand(#operator, #crp);
	}
	;

//
// Tree Parser
//

class GroupExpressionTreeParser extends TreeParser;

options {
	importVocab = GroupExpression;
	defaultErrorHandler = false;
}

// 
// Body
//

{
	private boolean dynamic;
	
	private GroupContextProvider provider;
	
	public void setContextProvider(GroupContextProvider provider) {
		this.provider = provider;
	}
	
	private GroupExpressionParserException wrap(AST node, GroupExpressionException e) {
		return new GroupExpressionParserException(((CustomAST) node).marker, e);
	}
	
	private Argument ensureDynamic(AST node, Argument argument) {
		if (! argument.isDynamic()) {
			try {
				return new StaticArgument(argument.getValue());
			}
			catch (GroupExpressionException e) {
				throw wrap(node, e);
			}
		}
		else {
			this.dynamic = true;
			return argument;
		}
	}
}

//
// Rules
//

start returns [Group group]
	: group=expression
	;
	
expression returns [Group group]
{
	Group a;
	Group b;
}
	: #(UNION a=expression b=expression) {
		group = new GroupUnion(a, b);
	}
	| #(INTERSECTION a=expression b=expression) {
		group = new GroupIntersection(a, b);
	}
	| #(DIFFERENCE a=expression b=expression) {
		group = new GroupDifference(a, b);
	}
	| group=group
	;
	
group returns [Group group] 
{
	ArgumentList arguments = new ArgumentList();
	Argument arg;
	
	this.dynamic = false; // reset flag
}
	: #(g:GROUP n:NAME (arg=argument { arguments.add(arg); })*) {
		String name = #n.getText();
		
		if (this.dynamic) {
			DynamicGroup dynaGroup = new DynamicGroup(this.provider, name);
			
			for (Argument argument : arguments) {
				dynaGroup.addArgument(argument);
			}
			
			group = dynaGroup;
		}
		else {
			GroupBuilder builder = GroupBuilderRegistry.getGroupBuilder(name);
			
			if (arguments.size() < builder.getMinArguments() || arguments.size() > builder.getMaxArguments()) {
				throw wrap(#g, new WrongNumberOfArgumentsException(arguments.size(),  builder.getMinArguments(), builder.getMaxArguments()));
			}
			
			try {
				group = builder.build(arguments.getArgumentValues());
			}
			catch (GroupExpressionException e) {
				throw wrap(#g, e);
			}
		}
	}
	;
	
argument returns [Argument argument]
{
	Object value;
}
	: value=simpleArgument { 
		argument = new StaticArgument(value);
	}
	| argument=so:specificOperator {
		argument = ensureDynamic(#so, argument);
	}
	| argument=ca:composedArgument { 
		argument = ensureDynamic(#ca, argument);
	}
	;

simpleArgument returns [Object value]
	: n:NAME {
		value = #n.getText();
	}
	| s:STRING {
		value = #s.getText();
	}
	;

composedArgument returns [DynamicArgument argument]
{
	List<NestedProperty> properties = new ArrayList<NestedProperty>();
	NestedProperty nested;
}
	: #(DOLLAR argument=originPart (nested=propertyPart { properties.add(nested); })*) {
		
		for (NestedProperty property : properties) {
			argument.addProperty(property);
		}
	}
	;

originPart returns [DynamicArgument argument]
	: n:NAME  {
		argument = new VariableArgument(#n.getText());
	}
	| argument=specificOperator
	;

propertyPart returns [NestedProperty property]
	: n:NAME {
		property = new SimpleProperty(#n.getText());
	}
	| property=genericOperator
	;

genericOperator returns [MethodProperty property]
{
	ArgumentList arguments = new ArgumentList();
	Argument arg;
}
	: #(OP n:NAME (arg=argument { arguments.add(arg); })*) {
		property = new MethodProperty(#n.getText());
		
		for (Argument argument : arguments) {
			property.addArgument(argument);
		}
	}
	;

specificOperator returns [OperatorArgument operator]
	: operator=idOperator
	| operator=parameterOperator
	| operator=enumOperator
	| operator=numberOperator
	| operator=classOperator
	;

idOperator returns [IdOperator operator] 
{
	Argument a;
	Argument b;
}
	: #(I_OP a=argument b=argument) {
		operator = new IdOperator(a, b);
	}
	;

parameterOperator returns [ParameterOperator operator]
{
	Argument arg;
}
	: #(P_OP arg=argument) {
		operator = new ParameterOperator(arg);
	}
	;
	
enumOperator returns [EnumOperator operator]
{
	Argument a;
	Argument b;
}
	: #(E_OP a=argument b=argument) {
		if (b == null) {
			operator = new EnumOperator(a, b);
		}
		else {
			operator = new EnumOperator(a);
		}
	}
	;

numberOperator returns [NumberOperator operator]
{
	Argument a;
	Argument b;
}
	: #(N_OP a=argument b=argument) {
		if (b == null) {
			operator = new NumberOperator(a);
		}
		else {
			operator = new NumberOperator(a, b);
		}
	}
	;
	
classOperator returns [ClassOperator operator]
{
	Argument arg;
}
	: #(C_OP arg=argument) {
		operator = new ClassOperator(arg);
	}
	;
	