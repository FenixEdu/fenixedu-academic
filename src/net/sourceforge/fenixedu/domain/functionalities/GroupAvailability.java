package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;

/**
 * This class represents an availability policy base on groups.
 * 
 * @author cfgi
 */
public class GroupAvailability extends GroupAvailability_Base {

    /** cached group */
    private ExpressionGroup group;

    protected GroupAvailability() {
        super();
    }

    /**
     * Creates a <code>GroupAvailability</code> to the given functionality and
     * with the given expression. The expression is converted into an
     * {@link ExpressionGroup} so if the expression is invalid an exception will
     * be thrown.
     * 
     * @param functionality
     *            the target functionality
     * @param expression
     *            the group expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    public GroupAvailability(Functionality functionality, String expression) {
        super();

        setFunctionality(functionality);
        setExpression(expression);
    }

    @Override
    public void setFunctionality(Functionality functionality) {
        // The field is required but since the deletion sets this to null and we
        // want to delete this objects the verification is not done.
        //
        // if (functionality == null) {
        // throw new FieldIsRequiredException("functionality",
        // "functionalities.availability.required.functionality");
        // }

        super.setFunctionality(functionality);
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
        this.group = new ExpressionGroup(expression);
    }

    /**
     * Obtains a group from the current expression obtained by
     * {@link #getExpression()}.
     * 
     * @return a group from the current expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    public ExpressionGroup getGroup() {
        if (this.group == null) {
            this.group = new ExpressionGroup(getExpression());
        }

        return this.group;
    }

    /**
     * Delegates the availability to the group obtained with {@link #getGroup()}.
     * The functionality is available if the given person is a member of the
     * group.
     * 
     * @return <code>getGroup().isMember(person)</code>
     * 
     * @see Group#isMember(Person)
     * 
     * @exception GroupDynamicExpressionException
     *                when the evaluation of the expression group fails
     */
    @Override
    public boolean isAvailable(FunctionalityContext context, Person person) {
        try {
            return getGroup().isMember(new GroupContextFromFunctionality(context), person);
        } catch (GroupDynamicExpressionException e) {
            throw e;
        } catch (Exception e) {
            throw new GroupDynamicExpressionException(e,
                    "accessControl.group.expression.evaluation.error");
        }
    }

}
