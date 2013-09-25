package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * Creates a new {@link ExpresssionGroupAvailability} based on the expression
 * given.
 * 
 * @author cfgi
 */
public class CreateGroupAvailability {

    @Atomic
    public static ExpressionGroupAvailability run(Content functionality, String expression) {
        check(RolePredicates.MANAGER_PREDICATE);
        if (!isEmpty(expression)) {
            return new ExpressionGroupAvailability(functionality, expression);
        } else {
            functionality.setAvailabilityPolicy(null);
            return null;
        }
    }

    private static boolean isEmpty(String expression) {
        return expression == null || expression.trim().length() == 0;
    }

}