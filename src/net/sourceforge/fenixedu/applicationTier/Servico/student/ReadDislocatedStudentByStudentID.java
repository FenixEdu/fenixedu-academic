/**
 * Aug 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoDislocatedStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDislocatedStudentByStudentID extends Service {

    public InfoDislocatedStudent run(Integer studentID) throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        Student student = (Student) persistentStudent.readByOID(Student.class, studentID);
        return InfoDislocatedStudent.newInfoFromDomain(student.getDislocatedStudent());
    }
}
