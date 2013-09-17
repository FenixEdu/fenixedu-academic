package net.sourceforge.fenixedu.applicationTier.Servico.registration;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class TransitToBolonha {

    @Atomic
    public static void run(final Person person, final Registration sourceRegistrationForTransition, final DateTime dateTime) {
        check(RolePredicates.MANAGER_PREDICATE);
        sourceRegistrationForTransition.transitToBolonha(person, dateTime);
    }

}