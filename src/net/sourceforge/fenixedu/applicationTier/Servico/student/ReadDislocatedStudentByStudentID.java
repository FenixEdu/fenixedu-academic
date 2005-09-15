/**
 * Aug 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.student.InfoDislocatedStudent;
import net.sourceforge.fenixedu.domain.DislocatedStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentDislocatedStudent;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDislocatedStudentByStudentID implements IService {

    public InfoDislocatedStudent run(Integer studentID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IPersistentDislocatedStudent persistentDislocatedStudent = sp.getIPersistentDislocatedStudent();

        IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
        List<DislocatedStudent> dislocatedStudents = (List<DislocatedStudent>) persistentDislocatedStudent
                .readAll(DislocatedStudent.class);

        if (dislocatedStudents != null) {
            for (DislocatedStudent dislocatedStudent : dislocatedStudents) {
                if(dislocatedStudent.getStudent().equals(student)){
                    return InfoDislocatedStudent.newInfoFromDomain(dislocatedStudent);
                }
            }
        }
        
        return null;
    }
}
