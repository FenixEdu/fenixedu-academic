package net.sourceforge.fenixedu.domain.functionalities;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupDynamicExpressionException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;
import net.sourceforge.fenixedu.domain.contents.Content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an availability policy base on groups.
 * 
 * @author cfgi
 */
public class GroupAvailability extends GroupAvailability_Base {

    private static final Logger logger = LoggerFactory.getLogger(GroupAvailability.class);

    protected GroupAvailability() {
        super();
    }

    /**
     * Creates a <code>GroupAvailability</code> to the given accessible item
     * that relies on the given group to determine the availability
     * 
     * @param item
     *            the target item
     * @param expression
     *            the group expression
     * 
     * @exception GroupExpressionException
     *                when the expression is not correct
     */
    public GroupAvailability(Content content, Group group) {
        super();

        setContent(content);
        setTargetGroup(group);
    }

    /**
     * Delegates the availability to the group obtained with {@link #getGroup()} . The functionality is available if the group
     * allows the <tt>UserView</tt> specified in the context.
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
            return getTargetGroup().allows(context.getUserView());
        } catch (GroupDynamicExpressionException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GroupDynamicExpressionException(e, "accessControl.group.expression.evaluation.error");
        }
    }

    public String getExpression() {
        return getTargetGroup().getExpression();
    }

    @Deprecated
    public boolean hasTargetGroup() {
        return getTargetGroup() != null;
    }

}
