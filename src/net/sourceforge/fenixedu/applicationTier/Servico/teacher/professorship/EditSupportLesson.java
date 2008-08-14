package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditSupportLesson extends Service {

    public void run(SupportLessonDTO supportLessonDTO, RoleType roleType) {

	Professorship professorship = rootDomainObject.readProfessorshipByOID(supportLessonDTO.getProfessorshipID());
	ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
	Teacher teacher = professorship.getTeacher();
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);

	if (teacherService == null) {
	    new TeacherService(teacher, executionSemester);
	}

	SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonDTO.getIdInternal());
	if (supportLesson == null) {
	    supportLesson = new SupportLesson(supportLessonDTO, professorship, roleType);
	} else {
	    supportLesson.update(supportLessonDTO, roleType);
	}
    }
}
