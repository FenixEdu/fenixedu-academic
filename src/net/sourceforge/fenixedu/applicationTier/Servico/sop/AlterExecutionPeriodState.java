package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
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

		final IExecutionPeriod executionPeriod = executionPeriodDAO.readBySemesterAndExecutionYear(
				infoExecutionPeriod.getSemester(), executionYearDAO.readExecutionYearByName(
						infoExecutionPeriod.getInfoExecutionYear().getYear()).getYear());

		if (executionPeriod == null) {
			throw new InvalidArgumentsServiceException();
		}

		if (periodState.getStateCode().equals(PeriodState.CURRENT)) {
			// Deactivate the current
			executionPeriodDAO.readActualExecutionPeriod().setState(new PeriodState(PeriodState.OPEN));
		}

		executionPeriod.setState(periodState);
	}

}
