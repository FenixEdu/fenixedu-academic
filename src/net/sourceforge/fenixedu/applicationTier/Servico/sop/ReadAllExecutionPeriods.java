package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAllExecutionPeriods implements IService {

	public List run() throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

		final Collection<IExecutionPeriod> executionPeriods = persistentExecutionPeriod.readAll(ExecutionPeriod.class);
		final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>(executionPeriods.size());
		for (final IExecutionPeriod executionPeriod : executionPeriods) {
			infoExecutionPeriods.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
		}

		return infoExecutionPeriods;
	}

}