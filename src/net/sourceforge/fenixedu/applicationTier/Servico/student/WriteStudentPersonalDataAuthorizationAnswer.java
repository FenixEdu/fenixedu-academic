/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class WriteStudentPersonalDataAuthorizationAnswer extends Service {

	public void run(Integer studentID, String answer) throws ExcepcaoPersistencia {
		final Student student = rootDomainObject.readStudentByOID(studentID);
		new StudentPersonalDataAuthorization(student, ExecutionYear.readCurrentExecutionYear(),
				StudentPersonalDataAuthorizationChoice.valueOf(answer));
	}
}
