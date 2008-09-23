/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class WriteStudentPersonalDataAuthorizationAnswer extends FenixService {

    public void run(Integer studentID, String answer) {
	final Registration registration = rootDomainObject.readRegistrationByOID(studentID);
	registration.getStudent().setPersonalDataAuthorizationForCurrentExecutionYear(
		StudentPersonalDataAuthorizationChoice.valueOf(answer));
    }
}
