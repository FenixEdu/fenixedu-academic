package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability;

/**
 * Creates a new {@link ExpresssionGroupAvailability} based on the expression
 * given.
 * 
 * @author cfgi
 */
public class CreateGroupAvailability extends Service {

    public ExpressionGroupAvailability run(Content functionality, String expression) {
	if (!isEmpty(expression)) {
	    return new ExpressionGroupAvailability(functionality, expression);
	} else {
	    functionality.setAvailabilityPolicy(null);
	    return null;
	}
    }

    private boolean isEmpty(String expression) {
	return expression == null || expression.trim().length() == 0;
    }

}
