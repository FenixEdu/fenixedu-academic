package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

public class AlterExecutionPeriodState extends Service {

    public void run(final String year, final Integer semester, final PeriodState periodState)
            throws FenixServiceException {

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        final ExecutionPeriod executionPeriod = executionYear.readExecutionPeriodForSemester(semester);
        if (executionPeriod == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
            if (currentExecutionPeriod != null) {
            	currentExecutionPeriod.setState(PeriodState.OPEN);
            	currentExecutionPeriod.getExecutionYear().setState(PeriodState.OPEN);
            }
        }

        executionPeriod.setState(periodState);
        executionPeriod.getExecutionYear().setState(periodState);
    }

    public class InvalidExecutionPeriod extends FenixServiceException {

        InvalidExecutionPeriod() {
            super();
        }

    }

}