package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.AccessibleItem;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;

/**
 * This class represents an availability policy base on groups created from
 * expression group language.
 * 
 * @author cfgi
 */
public class ExpressionGroupAvailability extends ExpressionGroupAvailability_Base {

    /** cached not persisted group   */
    private ExpressionGroup group;
    
    protected ExpressionGroupAvailability() {
        super();
    }

    /**
     * Creates a <code>GroupAvailability</code> to the given functionality and
     * with the given expression. The expression is converted into an
     * {@link ExpressionGroup} so if the expression is invalid an exception will
     * be thrown.
     * 
     * @param item
     *            the target item
     * @param expression
     *            the group expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    public ExpressionGroupAvailability(AccessibleItem item, String expression) {
        super();

        setAccessibleItem(item);
        setExpression(expression);
    }

    /**
     * Changes the current expression to the given one. An
     * {@link ExpressionGroup} is created with the given expression.
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    @Override
    public void setExpression(String expression) {
        super.setExpression(expression);

        // we build the group immediatly to detect problems with the expression
        // as soon as possible. Nevertheless the group has a lazy construction
        // built in getGroup(). This is used after obtaining the group for the
        // persistent storage.
        setTargetGroup(new ExpressionGroup(expression));
    }

    protected ExpressionGroup getGroup() {
        if (this.group == null) {
            this.group = new ExpressionGroup(getExpression());
        }

        return this.group;
    }

    protected void setGroup(ExpressionGroup group) {
        this.group = group;
    }

    /**
     * Obtains a group from the current expression obtained by
     * {@link #getExpression()}.
     * 
     * @return an expression group from the current expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    @Override
    public ExpressionGroup getTargetGroup() {
        return getGroup();
    }

    /**
     * Delegates the availability to the group obtained with {@link #getGroup()}.
     * The functionality is available if the group allows the <tt>UserView</tt>
     * specified in the context.
     * 
     * @return <code>getGroup().allows(context.getUserView())</code>
     * 
     * @see Group#isMember(Person)
     * 
     * @exception GroupDynamicExpressionException
     *                when the evaluation of the expression group fails
     */
    @Override
    public boolean isAvailable(FunctionalityContext context) {
        try {
            return getTargetGroup().allows(new GroupContextFromFunctionality(context), context.getUserView());
        } catch (GroupDynamicExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new GroupDynamicExpressionException(e,
                    "accessControl.group.expression.evaluation.error");
        }
    }

}
