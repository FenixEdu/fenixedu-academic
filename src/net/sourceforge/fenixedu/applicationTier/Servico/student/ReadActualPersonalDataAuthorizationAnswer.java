/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */
public class ReadActualPersonalDataAuthorizationAnswer extends FenixService {

    public StudentPersonalDataAuthorizationChoice run(Integer registrationID) {
	final Registration registration = rootDomainObject.readRegistrationByOID(registrationID);
	return registration.getStudent().getPersonalDataAuthorizationForCurrentExecutionYear();
    }

}
