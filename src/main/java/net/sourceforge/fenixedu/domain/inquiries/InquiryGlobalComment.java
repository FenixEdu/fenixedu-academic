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

    public void delete() {
        for (; !getInquiryResultCommentsSet().isEmpty(); getInquiryResultComments().get(0).delete()) {
            ;
        }
        setExecutionCourse(null);
        setExecutionDegree(null);
        setExecutionSemester(null);
        setTeacher(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment> getInquiryResultComments() {
        return getInquiryResultCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResultComments() {
        return !getInquiryResultCommentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasCommentOnTeacher() {
        return getCommentOnTeacher() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

}
