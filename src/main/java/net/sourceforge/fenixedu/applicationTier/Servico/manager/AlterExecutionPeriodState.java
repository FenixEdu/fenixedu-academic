package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class AlterExecutionPeriodState {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(final String year, final Integer semester, final PeriodState periodState) throws FenixServiceException {

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        final ExecutionSemester executionSemester = executionYear.getExecutionSemesterFor(semester);
        if (executionSemester == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
            final ExecutionYear currentExecutionYear = currentExecutionPeriod.getExecutionYear();
            if (currentExecutionPeriod != null) {
                currentExecutionPeriod.setState(PeriodState.OPEN);
                currentExecutionYear.setState(PeriodState.OPEN);
            }
            executionSemester.setState(periodState);
            executionSemester.getExecutionYear().setState(periodState);
        } else {
            executionSemester.setState(periodState);
            PeriodState currentPeriodState = periodState;
            for (final ExecutionSemester otherExecutionPeriod : executionYear.getExecutionPeriodsSet()) {
                if (currentPeriodState != null
                        && !otherExecutionPeriod.getState().getStateCode().equals(currentPeriodState.getStateCode())) {
                    currentPeriodState = null;
                }
            }
            if (currentPeriodState != null) {
                executionYear.setState(currentPeriodState);
            }
        }
    }

    public static class InvalidExecutionPeriod extends FenixServiceException {
        InvalidExecutionPeriod() {
            super();
        }
    }

}