package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.SupportLessonDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.WeekDay;

public class EditSupportLesson extends FenixService {

	public void run(SupportLessonDTO supportLessonDTO, RoleType roleType) {

		Professorship professorship = rootDomainObject.readProfessorshipByOID(supportLessonDTO.getProfessorshipID());
		ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
		Teacher teacher = professorship.getTeacher();
		TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);

		if (teacherService == null) {
			new TeacherService(teacher, executionSemester);
		}

		final StringBuilder log = new StringBuilder();

		SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonDTO.getIdInternal());
		if (supportLesson == null) {
			supportLesson = new SupportLesson(supportLessonDTO, professorship, roleType);
			log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
					"label.teacher.schedule.supportLessons.create"));
		} else {
			supportLesson.update(supportLessonDTO, roleType);
			log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
					"label.teacher.schedule.supportLessons.change"));
		}

		log.append(WeekDay.getWeekDay(supportLessonDTO.getWeekDay()).getLabel());
		log.append(" ");
		log.append(supportLessonDTO.getStartTime().getHours());
		log.append(":");
		log.append(supportLessonDTO.getStartTime().getMinutes());
		log.append(" - ");
		log.append(supportLessonDTO.getEndTime().getHours());
		log.append(":");
		log.append(supportLessonDTO.getEndTime().getMinutes());
		log.append(" - ");
		log.append(supportLessonDTO.getPlace());

		new TeacherServiceLog(teacherService, log.toString());
	}
}
