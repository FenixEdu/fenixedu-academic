package net.sourceforge.fenixedu.applicationTier.Servico.registration;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class TransitToBolonha extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(final Person person, final Registration sourceRegistrationForTransition, final DateTime dateTime) {
	sourceRegistrationForTransition.transitToBolonha(person, dateTime);
    }

}