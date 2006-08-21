/**
 * Aug 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDislocatedStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDislocatedStudentByStudentID extends Service {

    public InfoDislocatedStudent run(Integer studentID) throws ExcepcaoPersistencia {
        Registration registration = rootDomainObject.readRegistrationByOID(studentID);
        return InfoDislocatedStudent.newInfoFromDomain(registration.getDislocatedStudent());
    }

}
