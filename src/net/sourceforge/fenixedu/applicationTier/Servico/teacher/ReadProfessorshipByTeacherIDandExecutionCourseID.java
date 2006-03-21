/*
 * Created on 16:31:33,18/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsAbstractService;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 * 16:31:33,18/Out/2004
 */
public class ReadProfessorshipByTeacherIDandExecutionCourseID extends
        ReadDetailedTeacherProfessorshipsAbstractService {

    public ReadProfessorshipByTeacherIDandExecutionCourseID() {
    }

    public InfoProfessorship run(Integer teacherID, Integer executionCourseID)
            throws FenixServiceException, ExcepcaoPersistencia {

        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherID);
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(
                executionCourseID);

        Professorship professorship = null;
        if (teacher != null) {
            professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
        }

        InfoProfessorship infoProfessorship = InfoProfessorship.newInfoFromDomain(professorship);

        return infoProfessorship;
    }
}