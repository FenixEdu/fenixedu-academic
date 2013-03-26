package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Person;

public class ExecutionCourseReplyTo extends ExecutionCourseReplyTo_Base {

    public ExecutionCourseReplyTo() {
        super();
    }

    @Override
    public String getReplyToAddress(final Person person) {
        ExecutionCourseSender executionCourseSender = (ExecutionCourseSender) getSender();
        return executionCourseSender.getCourse().getSite().getMail();
    }

    @Override
    public String getReplyToAddress() {
        ExecutionCourseSender executionCourseSender = (ExecutionCourseSender) getSender();
        return executionCourseSender.getCourse().getSite().getMail();
    }

}
