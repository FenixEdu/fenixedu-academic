package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryGlobalComment extends InquiryGlobalComment_Base {

    public InquiryGlobalComment(ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setExecutionCourse(executionCourse);
	setExecutionDegree(executionDegree);
	setCommentOnTeacher(false);
    }

    public InquiryGlobalComment(Person teacher, ExecutionSemester executionSemester) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setTeacher(teacher);
	setExecutionSemester(executionSemester);
	setCommentOnTeacher(true);
    }
}
