package net.sourceforge.fenixedu.applicationTier.Servico.student;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentByUsername extends Service {

    public Object run(String username) throws ExcepcaoPersistencia {
        final Student student = Student.readByUsername(username);

        if (student != null) {
            return InfoStudent.newInfoFromDomain(student);
        }

        return null;
    }
}