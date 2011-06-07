package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

public class ExecutionDegree1stAnd2ndCycleProviderForExecutionCourseSearchBean extends
	ExecutionDegreeProviderForExecutionCourseSearchBean {

    public Object provide(Object source, Object currentValue) {
	List<ExecutionDegree> filteredExecutionDegrees = new ArrayList<ExecutionDegree>();
	final List<ExecutionDegree> executionDegrees = (List<ExecutionDegree>) super.provide(source, currentValue);
	for (ExecutionDegree executionDegree : executionDegrees) {
	    if (executionDegree.getDegree().isFirstCycle() || executionDegree.getDegree().isSecondCycle()) {
		filteredExecutionDegrees.add(executionDegree);
	    }
	}
	return filteredExecutionDegrees;
    }
}
