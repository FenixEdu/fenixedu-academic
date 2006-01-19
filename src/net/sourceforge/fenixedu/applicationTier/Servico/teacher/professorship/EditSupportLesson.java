/**
 * Nov 22, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditSupportLesson extends Service {

    public void run(SupportLessonDTO supportLessonDTO) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        SupportLesson supportLesson = (SupportLesson) persistentSupport.getIPersistentSupportLesson()
                .readByOID(SupportLesson.class, supportLessonDTO.getIdInternal());

        if (supportLesson == null) {
            Professorship professorship = (Professorship) persistentSupport
                    .getIPersistentProfessorship().readByOID(Professorship.class,
                            supportLessonDTO.getProfessorshipID());
            supportLesson = DomainFactory.makeSupportLesson();
            supportLesson.setProfessorship(professorship);
        }

        ExecutionPeriod executionPeriod = supportLesson.getProfessorship().getExecutionCourse()
                .getExecutionPeriod();
        Teacher teacher = supportLesson.getProfessorship().getTeacher();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);

        if (teacherService == null) {
            DomainFactory.makeTeacherService(teacher, executionPeriod);
        }
        supportLesson.setEndTime(supportLessonDTO.getEndTime());
        supportLesson.setStartTime(supportLessonDTO.getStartTime());
        supportLesson.setPlace(supportLessonDTO.getPlace());
        supportLesson.setWeekDay(supportLessonDTO.getWeekDay());

        supportLesson.verifyOverlappings();

    }
}
