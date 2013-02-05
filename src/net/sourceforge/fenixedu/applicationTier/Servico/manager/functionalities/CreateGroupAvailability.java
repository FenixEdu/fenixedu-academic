package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * Creates a new {@link ExpresssionGroupAvailability} based on the expression
 * given.
 * 
 * @author cfgi
 */
public class CreateGroupAvailability extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static ExpressionGroupAvailability run(Content functionality, String expression) {
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