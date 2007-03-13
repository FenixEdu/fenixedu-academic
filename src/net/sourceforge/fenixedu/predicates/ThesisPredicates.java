package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class ThesisPredicates {

    public static final AccessControlPredicate<Thesis> waitingConfirmation = new AccessControlPredicate<Thesis>() {

        public boolean evaluate(Thesis thesis) {
            return thesis.isWaitingConfirmation();
        }
        
    };
    
}
