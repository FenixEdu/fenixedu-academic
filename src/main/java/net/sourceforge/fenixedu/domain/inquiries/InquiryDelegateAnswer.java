package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

public class InquiryDelegateAnswer extends InquiryDelegateAnswer_Base {

    public InquiryDelegateAnswer(YearDelegate yearDelegate, ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
        super();
        setDelegate(yearDelegate);
        setExecutionCourse(executionCourse);
        setExecutionDegree(executionDegree);
    }
    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasAllowAcademicPublicizing() {
        return getAllowAcademicPublicizing() != null;
    }

    @Deprecated
    public boolean hasDelegate() {
        return getDelegate() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

}
