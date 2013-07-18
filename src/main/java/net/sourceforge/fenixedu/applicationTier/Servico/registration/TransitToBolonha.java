package net.sourceforge.fenixedu.applicationTier.Servico.registration;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class TransitToBolonha {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(final Person person, final Registration sourceRegistrationForTransition, final DateTime dateTime) {
        sourceRegistrationForTransition.transitToBolonha(person, dateTime);
    }

}