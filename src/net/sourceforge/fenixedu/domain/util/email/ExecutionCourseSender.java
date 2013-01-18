package net.sourceforge.fenixedu.domain.util.email;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public static Comparator<ExecutionCourseSender> COMPARATOR_BY_EXECUTION_COURSE_SENDER = new Comparator<ExecutionCourseSender>() {

	@Override
	public int compare(final ExecutionCourseSender executionCourseSender1, final ExecutionCourseSender executionCourseSender2) {
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
	setFromName(getFromName(executionCourse));
	setFromAddress(Sender.getNoreplyMail());
	addReplyTos(new ExecutionCourseReplyTo());
	addReplyTos(new CurrentUserReplyTo());
	setMembers(new ExecutionCourseTeachersGroup(executionCourse));
	final String labelECTeachers = BundleUtil.getStringFromResourceBundle("resources.SiteResources",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
		new String[] { executionCourse.getNome() });
	final String labelECStudents = BundleUtil.getStringFromResourceBundle("resources.SiteResources",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroupWithName",
		new String[] { executionCourse.getNome() });
	final String labelECResponsibleTeachers = BundleUtil.getStringFromResourceBundle("resources.SiteResources",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
		new String[] { executionCourse.getNome() });
	// fixed recipients
	addRecipients(new Recipient(labelECTeachers, new ExecutionCourseTeachersGroup(executionCourse)));
	addRecipients(new Recipient(labelECStudents, new ExecutionCourseStudentsGroup(executionCourse)));
	addRecipients(new Recipient(labelECResponsibleTeachers, new ExecutionCourseResponsibleTeachersGroup(executionCourse)));
    }

    private String getFromName(ExecutionCourse executionCourse) {
	return String.format("%s %s %s",
		executionCourse.getNome(),
		executionCourse.getDegreePresentationString().replaceAll(", ", " "),
		executionCourse.getExecutionPeriod().getQualifiedName().replace('/', ' '));
    }

    @Service
    public static ExecutionCourseSender newInstance(ExecutionCourse ec) {
	ExecutionCourseSender sender = ec.getSender();
	return sender == null ? new ExecutionCourseSender(ec) : sender;
    }

}
