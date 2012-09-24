/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.util.BundleUtil;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteSupportLesson extends FenixService {

    public void run(Integer supportLessonID, RoleType roleType) {
	SupportLesson supportLesson = rootDomainObject.readSupportLessonByOID(supportLessonID);
	log(supportLesson);
	supportLesson.delete(roleType);
    }

    private void log(final SupportLesson supportLesson) {
	final StringBuilder log = new StringBuilder();
	log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.teacher.schedule.supportLessons.delete"));
	log.append(supportLesson.getWeekDay().toString());
	log.append(" ");
	log.append(supportLesson.getStartTime().getHours());
	log.append(":");
	log.append(supportLesson.getStartTime().getMinutes());
	log.append(" - ");
	log.append(supportLesson.getEndTime().getHours());
	log.append(":");
	log.append(supportLesson.getEndTime().getMinutes());
	log.append(" - ");
	log.append(supportLesson.getPlace());
	final TeacherService teacherService = getTeacherService(supportLesson);
	new TeacherServiceLog(teacherService, log.toString());
    }

    private TeacherService getTeacherService(final SupportLesson supportLesson) {
	final Professorship professorship = supportLesson.getProfessorship();
	final ExecutionCourse executionCourse = professorship.getExecutionCourse();
	final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
	final Teacher teacher = professorship.getTeacher();
	return teacher.getTeacherServiceByExecutionPeriod(executionSemester);
    }

}
