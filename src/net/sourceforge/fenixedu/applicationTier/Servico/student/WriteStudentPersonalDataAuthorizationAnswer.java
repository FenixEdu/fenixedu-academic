/**
* Aug 29, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;

/**
 * @author Ricardo Rodrigues
 *
 */

public class WriteStudentPersonalDataAuthorizationAnswer extends Service {

    public void run(Integer studentID, String answer) throws ExcepcaoPersistencia{
        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        
        Student student = (Student) persistentObject.readByOID(Student.class,studentID);
        ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
        
        StudentPersonalDataAuthorization studentPersonalDataAuthorization = DomainFactory.makeStudentPersonalDataAuthorization();
        studentPersonalDataAuthorization.setStudent(student);
        studentPersonalDataAuthorization.setExecutionYear(executionYear);
        studentPersonalDataAuthorization.setAnswer(StudentPersonalDataAuthorizationChoice.valueOf(answer));
    }
}


