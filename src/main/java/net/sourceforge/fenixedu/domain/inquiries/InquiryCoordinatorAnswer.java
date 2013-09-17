package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class InquiryCoordinatorAnswer extends InquiryCoordinatorAnswer_Base {

    public InquiryCoordinatorAnswer(ExecutionDegree executionDegree, ExecutionSemester executionSemester) {
        super();
        setExecutionDegree(executionDegree);
        setExecutionSemester(executionSemester);
    }

    @Deprecated
    public boolean hasLastUpdatedBy() {
        return getLastUpdatedBy() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasExecutionSemester() {
        return getExecutionSemester() != null;
    }

    @Deprecated
    public boolean hasCoordinator() {
        return getCoordinator() != null;
    }

}
