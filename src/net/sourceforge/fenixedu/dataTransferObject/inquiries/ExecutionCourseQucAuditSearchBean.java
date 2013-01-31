package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.executionCourse.ExecutionCourseSearchBean;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultType;

public class ExecutionCourseQucAuditSearchBean extends ExecutionCourseSearchBean {

	private static final long serialVersionUID = 1L;
	private ExecutionCourse selectedExecutionCourse;

	@Override
	public Collection<ExecutionCourse> search(final Collection<ExecutionCourse> result) {
		final ExecutionDegree executionDegree = getExecutionDegree();
		final ExecutionSemester executionSemester = getExecutionPeriod();

		if (executionDegree == null && executionSemester != null) {
			for (InquiryResult inquiryResult : executionSemester.getInquiryResults()) {
				if (InquiryResultType.AUDIT.equals(inquiryResult.getResultType())) {
					result.add(inquiryResult.getExecutionCourse());
				}
			}
			return result;
		} else {
			return super.search(result);
		}
	}

	public void setSelectedExecutionCourse(ExecutionCourse selectedExecutionCourse) {
		this.selectedExecutionCourse = selectedExecutionCourse;
	}

	public ExecutionCourse getSelectedExecutionCourse() {
		return selectedExecutionCourse;
	}
}
