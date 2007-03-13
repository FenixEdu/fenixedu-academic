package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CoordinationPredicates {

    public static final AccessControlPredicate<Coordinator> responsible = new AccessControlPredicate<Coordinator>() {

        public boolean evaluate(Coordinator c) {
            Person person = AccessControl.getPerson();
            
            for (Coordinator coordinator : c.getExecutionDegree().getCoordinatorsList()) {
                if (coordinator.getPerson() == person && coordinator.isResponsible()) {
                    return true;
                }
            }
            
            return false;
        }
        
    };
}
