package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AlterExecutionPeriodState implements IService {

    public void run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        final IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

        final ExecutionPeriod executionPeriod = executionPeriodDAO.readBySemesterAndExecutionYear(
                infoExecutionPeriod.getSemester(), executionYearDAO.readExecutionYearByName(
                        infoExecutionPeriod.getInfoExecutionYear().getYear()).getYear());

        if (executionPeriod == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            ExecutionPeriod currentExecutionPeriod = executionPeriodDAO.readActualExecutionPeriod();
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