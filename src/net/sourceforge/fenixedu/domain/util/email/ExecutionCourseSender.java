package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class ExecutionCourseSender extends ExecutionCourseSender_Base {

    public ExecutionCourseSender(ExecutionCourse executionCourse) {
	super();
	setCourse(executionCourse);
	setFromName(getFromName(executionCourse));
	setFromAddress("noreply@ist.utl.pt");
	addReplyTos(new ExecutionCourseReplyTo());
	addReplyTos(new CurrentUserReplyTo());
	setMembers(new ExecutionCourseTeachersGroup(executionCourse));
	final String labelECTeachers = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroupWithName",
		new Object[] { executionCourse.getNome() });
	final String labelECStudents = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseStudentsGroupWithName",
		new Object[] { executionCourse.getNome() });
	final String labelECResponsibleTeachers = RenderUtils.getResourceString("SITE_RESOURCES",
		"label.net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseResponsibleTeachersGroupWithName",
		new Object[] { executionCourse.getNome() });
	// fixed recipients
	addRecipients(new Recipient(labelECTeachers, new ExecutionCourseTeachersGroup(executionCourse)));
	addRecipients(new Recipient(labelECStudents, new ExecutionCourseStudentsGroup(executionCourse)));
	addRecipients(new Recipient(labelECResponsibleTeachers, new ExecutionCourseResponsibleTeachersGroup(executionCourse)));
    }

    private String getFromName(ExecutionCourse executionCourse) {
	return String.format("%s - %s - %s", executionCourse.getNome(), executionCourse.getDegreePresentationString(),
		executionCourse.getExecutionPeriod().getQualifiedName());
    }

    @Service
    public static ExecutionCourseSender newInstance(ExecutionCourse ec) {
	ExecutionCourseSender sender = ec.getSender();
	return sender == null ? new ExecutionCourseSender(ec) : sender;
    }

}
