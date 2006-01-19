/**
* Aug 29, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteStudentPersonalDataAuthorizationAnswer implements IService {

    public void run(Integer studentID, String answer) throws ExcepcaoPersistencia{
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        
        Student student = (Student) persistentStudent.readByOID(Student.class,studentID);
        ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
        
        StudentPersonalDataAuthorization studentPersonalDataAuthorization = DomainFactory.makeStudentPersonalDataAuthorization();
        studentPersonalDataAuthorization.setStudent(student);
        studentPersonalDataAuthorization.setExecutionYear(executionYear);
        studentPersonalDataAuthorization.setAnswer(StudentPersonalDataAuthorizationChoice.valueOf(answer));
    }
}


