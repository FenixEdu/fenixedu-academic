package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class InquiryCoordinatorAnswer extends InquiryCoordinatorAnswer_Base {

    public InquiryCoordinatorAnswer(Coordinator coordinator, ExecutionSemester executionSemester) {
	super();
	setCoordinator(coordinator);
	setExecutionSemester(executionSemester);
    }

}
