package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

public class DepartmentUCResultsBean extends CoordinatorResultsBean {

    private static final long serialVersionUID = 1L;

    public DepartmentUCResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person president,
	    InquiryGlobalComment globalComment, boolean backToResume) {
	super(executionCourse, executionDegree, president, globalComment, backToResume);
    }

    @Override
    protected ResultPersonCategory getPersonCategory() {
	return ResultPersonCategory.DEPARTMENT_PRESIDENT;
    }
}
