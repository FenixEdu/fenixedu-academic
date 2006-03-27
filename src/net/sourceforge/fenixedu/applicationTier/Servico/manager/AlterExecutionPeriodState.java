package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

public class AlterExecutionPeriodState extends Service {

    public void run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod = ExecutionYear.readExecutionYearByName(
                infoExecutionPeriod.getInfoExecutionYear().getYear()).readExecutionPeriodForSemester(infoExecutionPeriod.getSemester()); 

        if (executionPeriod == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
            currentExecutionPeriod.setState(new PeriodState(PeriodState.OPEN));
        }

        executionPeriod.setState(periodState);
    }

    public class InvalidExecutionPeriod extends FenixServiceException {

        InvalidExecutionPeriod() {
            super();
        }

    }

}