/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.student.IStudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.domain.student.StudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.student.IPersistentStudentPersonalDataAuthorization;
import net.sourceforge.fenixedu.util.StudentPersonalDataAuthorizationChoice;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadActualPersonalDataAuthorizationAnswer implements IService {

    public StudentPersonalDataAuthorizationChoice run(Integer studentID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        IPersistentStudentPersonalDataAuthorization persistentStudentPersonalDataAuthorization = sp
                .getIPersistentStudentPersonalDataAuthorization();

        IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
        IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
        List allStudentPersonalDataAuthorizationAnswers = (List) persistentStudentPersonalDataAuthorization
                .readAll(StudentPersonalDataAuthorization.class);

        if (allStudentPersonalDataAuthorizationAnswers != null) {
            for (Iterator iter = allStudentPersonalDataAuthorizationAnswers.iterator(); iter.hasNext();) {
                IStudentPersonalDataAuthorization spda = (StudentPersonalDataAuthorization) iter.next();
                if (spda.getStudent().equals(student) && spda.getExecutionYear().equals(executionYear)) {
                    return spda.getAnswer();
                }
            }
        }
        return null;
    }

}
