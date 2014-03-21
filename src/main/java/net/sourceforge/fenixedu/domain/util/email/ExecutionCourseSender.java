package net.sourceforge.fenixedu.domain.util.email;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixframework.Atomic;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public static Comparator<ExecutionCourseSender> COMPARATOR_BY_EXECUTION_COURSE_SENDER =
            new Comparator<ExecutionCourseSender>() {

                @Override
                public int compare(final ExecutionCourseSender executionCourseSender1,
                        final ExecutionCourseSender executionCourseSender2) {
                    final ExecutionCourse executionCourse1 = executionCourseSender1.getCourse();
                    final ExecutionCourse executionCourse2 = executionCourseSender2.getCourse();
                    final ExecutionSemester executionSemester1 = executionCourse1.getExecutionPeriod();
                    final ExecutionSemester executionSemester2 = executionCourse2.getExecutionPeriod();
                    final int p = executionSemester1.compareTo(executionSemester2);
                    if (p == 0) {
                        final int n = executionCourse1.getName().compareTo(executionCourse2.getName());
                        return n == 0 ? executionCourseSender1.hashCode() - executionCourseSender2.hashCode() : n;
                    }
                    return p;
                }
            };

    public ExecutionCourseSender(ExecutionCourse executionCourse) {
        super();
        setCourse(executionCourse);
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new ExecutionCourseReplyTo());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(new ExecutionCourseTeachersGroup(executionCourse));
        final String labelECTeachers =
                BundleUtil.getStringFromResourceBundle("resources.SiteResources",
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
                        new String[] { executionCourse.getNome() });
        final String labelECStudents =
                BundleUtil.getStringFromResourceBundle("resources.SiteResources",
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroupWithName",
                        new String[] { executionCourse.getNome() });
        final String labelECResponsibleTeachers =
                BundleUtil.getStringFromResourceBundle("resources.SiteResources",
                        "label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
                        new String[] { executionCourse.getNome() });
        // fixed recipients
        addRecipients(new Recipient(labelECTeachers, new ExecutionCourseTeachersGroup(executionCourse)));
        addRecipients(new Recipient(labelECStudents, new ExecutionCourseStudentsGroup(executionCourse)));
        addRecipients(new Recipient(labelECResponsibleTeachers, new ExecutionCourseResponsibleTeachersGroup(executionCourse)));
        setFromName(createFromName());
    }

    public String createFromName() {
        if (getCourse() != null && getCourse().getExecutionPeriod() != null
                && getCourse().getExecutionPeriod().getQualifiedName() != null) {
            String degreeName = getCourse().getDegreePresentationString();
            String courseName = getCourse().getNome();
            String period = getCourse().getExecutionPeriod().getQualifiedName().replace('/', '-');
            return String.format("%s (%s: %s, %s)", Unit.getInstitutionAcronym(), degreeName, courseName, period);
        } else {
            return getFromName();
        }

    }

    @Override
    public void delete() {
        setCourse(null);
        super.delete();
    }

    @Atomic
    public static ExecutionCourseSender newInstance(ExecutionCourse ec) {
        ExecutionCourseSender sender = ec.getSender();
        return sender == null ? new ExecutionCourseSender(ec) : sender;
    }

    @Deprecated
    public boolean hasCourse() {
        return getCourse() != null;
    }

}
