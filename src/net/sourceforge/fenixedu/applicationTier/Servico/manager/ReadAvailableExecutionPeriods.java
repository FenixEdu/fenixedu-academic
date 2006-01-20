/*
 * Created on 11/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author lmac1
 */
public class ReadAvailableExecutionPeriods extends Service {

	public List run(List unavailableExecutionPeriodsIds) throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionPeriods = null;
		ExecutionPeriod executionPeriod = null;

		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();
		Collection executionPeriods = persistentExecutionPeriod.readAll(ExecutionPeriod.class);

		Iterator iter = unavailableExecutionPeriodsIds.iterator();
		while (iter.hasNext()) {

			executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
					ExecutionPeriod.class, (Integer) iter.next());
			executionPeriods.remove(executionPeriod);
		}

		infoExecutionPeriods = (List) CollectionUtils.collect(executionPeriods,
				TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD);

		return infoExecutionPeriods;
	}

	private Transformer TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD = new Transformer() {
		public Object transform(Object executionPeriod) {
			return InfoExecutionPeriodWithInfoExecutionYear
					.newInfoFromDomain((ExecutionPeriod) executionPeriod);
		}
	};

}