package net.sourceforge.fenixedu.applicationTier.Servico.operator;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.alumni.AlumniNotificationService;
import net.sourceforge.fenixedu.domain.AlumniIdentityCheckRequest;
import net.sourceforge.fenixedu.domain.Person;

public class ValidateAlumniIdentity extends AlumniNotificationService {

    public void run(AlumniIdentityCheckRequest identityRequest, Boolean approval, Person operator) {
        identityRequest.validate(approval, operator);
        sendIdentityCheckEmail(identityRequest, approval);
    }

    public void run(AlumniIdentityCheckRequest identityRequest, Person alumniPerson) {
        alumniPerson.setSocialSecurityNumber(identityRequest.getSocialSecurityNumber());
    }

}