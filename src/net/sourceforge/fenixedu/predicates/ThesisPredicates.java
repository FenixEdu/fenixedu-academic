package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.RoleTypeGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ThesisPredicates {

    public static final AccessControlPredicate<Thesis> waitingConfirmation = new AccessControlPredicate<Thesis>() {

        public boolean evaluate(Thesis thesis) {
            return thesis.isWaitingConfirmation();
        }
        
    };
    
    public static final AccessControlPredicate<Thesis> isScientificCouncil = new AccessControlPredicate<Thesis>() {

        public boolean evaluate(Thesis thesis) {
            return new RoleTypeGroup(RoleType.SCIENTIFIC_COUNCIL).isMember(AccessControl.getPerson());
        }
        
    };
}
