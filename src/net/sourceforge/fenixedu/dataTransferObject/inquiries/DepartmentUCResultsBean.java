package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.beanutils.BeanComparator;

public class DepartmentUCResultsBean extends CoordinatorResultsBean {

	private static final long serialVersionUID = 1L;

	public DepartmentUCResultsBean(ExecutionCourse executionCourse, ExecutionDegree executionDegree, Person president,
			boolean backToResume) {
		super(executionCourse, executionDegree, president, backToResume);
	}

	public List<InquiryResultComment> getAllCourseComments() {
		List<InquiryResultComment> commentsMade = new ArrayList<InquiryResultComment>();
		for (InquiryGlobalComment globalComment : getExecutionCourse().getInquiryGlobalComments()) {
			commentsMade.addAll(globalComment.getInquiryResultCommentsSet());
		}
		Collections.sort(commentsMade, new BeanComparator("person.name"));
		return commentsMade;
	}

	@Override
	protected ResultPersonCategory getPersonCategory() {
		return ResultPersonCategory.DEPARTMENT_PRESIDENT;
	}
}
