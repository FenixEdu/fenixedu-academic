/**
 * Nov 22, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditSupportLesson implements IService {

    public void run(SupportLessonDTO supportLessonDTO) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ISupportLesson supportLesson = (ISupportLesson) persistentSupport.getIPersistentSupportLesson()
                .readByOID(SupportLesson.class, supportLessonDTO.getIdInternal());

        if (supportLesson == null) {
            IProfessorship professorship = (IProfessorship) persistentSupport
                    .getIPersistentProfessorship().readByOID(Professorship.class,
                            supportLessonDTO.getProfessorshipID());
            supportLesson = DomainFactory.makeSupportLesson();
            supportLesson.setProfessorship(professorship);
        }

        IExecutionPeriod executionPeriod = supportLesson.getProfessorship().getExecutionCourse()
                .getExecutionPeriod();
        ITeacher teacher = supportLesson.getProfessorship().getTeacher();
        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);

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
