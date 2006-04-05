package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditSupportLesson extends Service {

    public void run(SupportLessonDTO supportLessonDTO) throws ExcepcaoPersistencia {
        SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonDTO.getIdInternal());

        if (supportLesson == null) {
            Professorship professorship = rootDomainObject.readProfessorshipByOID(supportLessonDTO.getProfessorshipID());
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
