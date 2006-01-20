package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.applicationTier.Service;

public class AlterExecutionPeriodState extends Service {

	public void run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState)
			throws FenixServiceException, ExcepcaoPersistencia {

		final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
		final IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();

		final ExecutionPeriod executionPeriod = executionPeriodDAO.readBySemesterAndExecutionYear(
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
