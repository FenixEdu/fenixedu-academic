package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni.AlumniNotificationService;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;

public class ValidateAlumniIdentity extends AlumniNotificationService {

    protected void run(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {
        identityRequest.validate(approval, operator);
        sendIdentityCheckEmail(identityRequest, approval);
    }

    protected void run(AlumniIdentityCheckRequest identityRequest, Person alumniPerson) {
        alumniPerson.setSocialSecurityNumber(identityRequest.getSocialSecurityNumber());
    }

    // Service Invokers migrated from Berserk

    private static final ValidateAlumniIdentity serviceInstance = new ValidateAlumniIdentity();

    @Atomic
    public static void runValidateAlumniIdentity(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {
        serviceInstance.run(identityRequest, approval, operator);
    }

    @Atomic
    public static void runValidateAlumniIdentity(AlumniIdentityCheckRequest identityRequest, Person alumniPerson) {
        serviceInstance.run(identityRequest, alumniPerson);
    }

}