/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionYearsService extends Service {

	public List run() throws ExcepcaoPersistencia {
		final IPersistentExecutionYear executionYearDAO = persistentSupport
				.getIPersistentExecutionYear();

		final Collection executionYears = executionYearDAO.readAll(ExecutionYear.class);
		return (List) CollectionUtils.collect(executionYears,
				new Transformer() {
					public Object transform(Object arg0) {
						final ExecutionYear executionYear = (ExecutionYear) arg0;
						return InfoExecutionYear
								.newInfoFromDomain(executionYear);
					}
				});

	}
	
	public ExecutionYear run(Integer executionYearID) throws ExcepcaoPersistencia {
		final IPersistentExecutionYear executionYearDAO = persistentSupport
				.getIPersistentExecutionYear();

		ExecutionYear executionYear = (ExecutionYear)executionYearDAO.readByOID(ExecutionYear.class,executionYearID);
		return executionYear;

	}

}