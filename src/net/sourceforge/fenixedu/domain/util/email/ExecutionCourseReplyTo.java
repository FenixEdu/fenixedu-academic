package net.sourceforge.fenixedu.domain.util.email;

public class ExecutionCourseReplyTo extends ExecutionCourseReplyTo_Base {

    public ExecutionCourseReplyTo() {
	super();
    }

    @Override
    public String getReplyToAddress() {
	ExecutionCourseSender executionCourseSender = (ExecutionCourseSender) getSender();
	return executionCourseSender.getCourse().getSite().getMail();
    }

}
