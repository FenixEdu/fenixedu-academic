package net.sourceforge.fenixedu.applicationTier.Servico.registration;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class TransitToBolonha {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(final Person person, final Registration sourceRegistrationForTransition, final DateTime dateTime) {
        sourceRegistrationForTransition.transitToBolonha(person, dateTime);
    }

}