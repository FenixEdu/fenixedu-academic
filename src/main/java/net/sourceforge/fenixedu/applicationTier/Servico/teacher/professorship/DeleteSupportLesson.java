/**
 * Nov 23, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceLog;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.WeekDay;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteSupportLesson {

    protected void run(String supportLessonID, RoleType roleType) {
        SupportLesson supportLesson = FenixFramework.getDomainObject(supportLessonID);
        log(supportLesson);
        supportLesson.delete(roleType);
    }

    private void log(final SupportLesson supportLesson) {
        final StringBuilder log = new StringBuilder();
        log.append(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                "label.teacher.schedule.supportLessons.delete"));
        log.append(WeekDay.getWeekDay(supportLesson.getWeekDay()).getLabel());
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

    // Service Invokers migrated from Berserk

    private static final DeleteSupportLesson serviceInstance = new DeleteSupportLesson();

    @Atomic
    public static void runDeleteSupportLesson(String supportLessonID, RoleType roleType) throws NotAuthorizedException {
        try {
            ScientificCouncilAuthorizationFilter.instance.execute();
            serviceInstance.run(supportLessonID, roleType);
        } catch (NotAuthorizedException ex1) {
            try {
                DepartmentMemberAuthorizationFilter.instance.execute();
                serviceInstance.run(supportLessonID, roleType);
            } catch (NotAuthorizedException ex2) {
                try {
                    DepartmentAdministrativeOfficeAuthorizationFilter.instance.execute();
                    serviceInstance.run(supportLessonID, roleType);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}