package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author mrsp and jdnf
 * 
 */
public class ReadProfessorshipByTeacherNumberAndExecutionCourseID extends Service {

    public InfoProfessorship run(String username, Integer executionCourseID)
            throws FenixServiceException, ExcepcaoPersistencia {

        Teacher teacher = Teacher.readTeacherByUsername(username);
        if (teacher == null) {
            throw new FenixServiceException();
        }
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(
                executionCourseID);
        
        Professorship professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);

        return infoProfessorship;
    }
}
