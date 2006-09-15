/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class WriteStudentPersonalDataAuthorizationAnswer extends Service {

    public void run(Integer studentID, String answer) throws ExcepcaoPersistencia {
	final Registration registration = rootDomainObject.readRegistrationByOID(studentID);
	registration.getStudent().setPersonalDataAuthorizationForCurrentExecutionYear(
		StudentPersonalDataAuthorizationChoice.valueOf(answer));
    }
}
