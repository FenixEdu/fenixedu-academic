/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 * 
 */
public class ReadActualPersonalDataAuthorizationAnswer extends Service {

    public StudentPersonalDataAuthorizationChoice run(Integer studentID) throws ExcepcaoPersistencia {
        final Registration registration = rootDomainObject.readRegistrationByOID(studentID);

        for (final StudentPersonalDataAuthorization studentPersonalDataAuthorization :
                registration.getStudentPersonalDataAuthorizations()) {
            if (studentPersonalDataAuthorization.getExecutionYear().getState().equals(PeriodState.CURRENT)) {
                return studentPersonalDataAuthorization.getAnswer();
            }
        }

        return null;
    }

}
