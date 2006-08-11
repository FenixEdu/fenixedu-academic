package net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.GroupAvailability;

/**
 * Creates a new {@link GroupAvailability} based on the expression given.
 * 
 * @author cfgi
 */
public class CreateGroupAvailability extends Service {

    public GroupAvailability run(Functionality functionality, String expression) {
        return new GroupAvailability(functionality, expression);
    }
    
}
