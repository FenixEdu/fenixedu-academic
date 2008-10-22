/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class WriteStudentPersonalDataAuthorizationAnswer extends FenixService {

    @Service
    public static void run(Integer studentID, String answer) {
	final Registration registration = rootDomainObject.readRegistrationByOID(studentID);
	registration.getStudent().setPersonalDataAuthorizationForCurrentExecutionYear(
		StudentPersonalDataAuthorizationChoice.valueOf(answer));
    }
}