package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.io.StringReader;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupContextRequiredException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.TokenStreamRecognitionException;

/**
 * An expression group represents a group of persons that is dynamically
 * created from an expression. The group continues to be immutable but
 * it can contain parts, or subgroups, that change with each evaluation.
 * This happens because an expression group requires a context to be
 * evaluated and some subgroups use information from the context to 
 * compute it's members. If the context changes then the group may change. 
 * 
 * <p>
 * Note that because the group may require a context to be evaluated it cannot
 * be easily combined with other groups.
 * 
 * @author cfgi
 */
public class ExpressionGroup extends Group implements GroupContextProvider {

    private static final long serialVersionUID = 1L;
    
    private String expression;
    
    private transient GroupContext context;
    private transient Group group;
    
    /**
     * The given expression is parsed and a group is created from the
     * expression. Since the expression is parsed you may have to deal with
     * errors in the expression.
     * 
     * @param expression
     *            the group expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     * 
     * @see #getGroup()
     */
    public ExpressionGroup(String expression) {
        super();

        this.expression = expression;
        this.group = parseExpression(this.expression);
    }

    /**
     * @return the original expression passed in the construction of the group
     */
    public String getExpression() {
        return this.expression;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    /**
     * Makes the given context the current context.
     * 
     * @param context the context to be used as the current context
     */
    protected void setContext(GroupContext context) {
        this.context = context;
    }
    
    /**
     * @return the current group context or <code>null</code> if no context is
     *         defined
     * @exception GroupContextRequiredException
     *                if a group context is not defined
     */
    public GroupContext getContext() {
        if (this.context == null) {
            throw new GroupContextRequiredException(this);
        }

        return this.context;
    }
    
    /**
     * Obtains the group created from parsing the expression.
     * 
     * @return the group compiled from the expression
     */
    public Group getGroup() {
        if (this.group == null) {
            this.group = parseExpression(getExpression());
        }
        
        return this.group;
    }
    
    /**
     * Parses the given expression and creates the appropriate group.
     * 
     * @param expression the expression to parse
     * @return the resulting group
     * 
     * @exception GroupExpressionException when the expression is not correct
     */
    protected Group parseExpression(String expression) {
        GroupExpressionLexer lexer = new GroupExpressionLexer(new StringReader(expression));
        GroupExpressionParser parser = new GroupExpressionParser(lexer);
        
        try {
            parser.start();
        } catch (RecognitionException e) {
            throw new GroupExpressionException(e);
        } catch (TokenStreamRecognitionException e) {
            throw new GroupExpressionException(e.recog);
        } catch (TokenStreamException e) {
            throw new GroupExpressionException(e);
        }
        
        GroupExpressionTreeParser treeParser = new GroupExpressionTreeParser();
        treeParser.setContextProvider(this);
        
        try {
            return treeParser.start(parser.getAST());
        } catch (RecognitionException e) {
            throw new GroupExpressionException(e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isMember(Person person) {
        return getGroup().isMember(person);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Set<Person> getElements() {
        return getGroup().getElements();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean allows(IUserView userView) {
        return getGroup().allows(userView);
    }
    
    /**
     * This method behaves like {@link #isMember(Person)} but setups the
     * expression group's context so that is's available during the entire
     * group's evaluation.
     */
    public boolean isMember(GroupContext context, Person person) {
        setContext(context);
        
        try {
            return isMember(person);
        }
        finally {
            setContext(null);
        }
    }
    
    /**
     * This method behaves like {@link #allows(IUserView)} but seup the
     * expression's group context so that it's available during the entire
     * group's evaluation.
     */
    public boolean allows(GroupContext context, IUserView userView) {
        setContext(context);
        
        try {
            return allows(userView);
        }
        finally {
            setContext(null);
        }
    }
    
    /**
     * This method behaves like {@link #getElements()} but setups the expression
     * group's context so that it's available during the entire group's
     * evaluation.
     */
    public Set<Person> getElements(GroupContext context) {
        setContext(context);
        
        try {
            return getElements();
        }
        finally {
            setContext(null);
        }
    }

}
