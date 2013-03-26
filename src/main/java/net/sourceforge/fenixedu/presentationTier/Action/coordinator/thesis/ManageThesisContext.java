package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;

public class ManageThesisContext implements Serializable {

    private ExecutionDegree executionDegree = null;

    public ManageThesisContext(final ExecutionDegree executionDegree) {
        setExecutionDegree(executionDegree);
    }

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    public ExecutionDegree getPreviousExecutionDegree() {
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            final ExecutionYear executionYear = executionDegree.getExecutionYear();
            final ExecutionYear previousExecutionYear = executionYear.getPreviousExecutionYear();
            if (previousExecutionYear != null) {
                final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
                for (final ExecutionDegree otherExecutionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
                    if (otherExecutionDegree.getExecutionYear() == previousExecutionYear) {
                        return otherExecutionDegree;
                    }
                }
            }
        }
        return null;
    }

    public SortedSet<ExecutionDegree> getAvailableExecutionDegrees() {
        final SortedSet<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.REVERSE_EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
        final ExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            executionDegrees.addAll(degreeCurricularPlan.getExecutionDegreesSet());
        }
        return executionDegrees;
    }

    public Scheduleing getScheduleing() {
        final ExecutionDegree executionDegree = getPreviousExecutionDegree();
        return executionDegree == null ? null : executionDegree.getScheduling();
    }

}
