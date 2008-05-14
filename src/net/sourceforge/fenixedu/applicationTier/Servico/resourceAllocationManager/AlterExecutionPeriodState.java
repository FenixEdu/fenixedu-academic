package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

public class AlterExecutionPeriodState extends Service {

    public void run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionSemester executionSemester = ExecutionYear.readExecutionYearByName(
                infoExecutionPeriod.getInfoExecutionYear().getYear()).readExecutionPeriodForSemester(
                infoExecutionPeriod.getSemester());
        if (executionSemester == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (periodState.getStateCode().equals(PeriodState.CURRENT)) {
            // Deactivate the current
            ExecutionSemester.readActualExecutionPeriod().setState(new PeriodState(PeriodState.OPEN));
        }
        executionSemester.setState(periodState);
    }

}
