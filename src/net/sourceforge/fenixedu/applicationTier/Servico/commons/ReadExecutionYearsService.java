/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionYearsService implements IService {

	public List run() throws ExcepcaoPersistencia {

		final ISuportePersistente sp = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		final IPersistentExecutionYear executionYearDAO = sp
				.getIPersistentExecutionYear();

		final List executionYears = executionYearDAO.readAllExecutionYear();
		return (List) CollectionUtils.collect(executionYears,
				new Transformer() {
					public Object transform(Object arg0) {
						final IExecutionYear executionYear = (IExecutionYear) arg0;
						return InfoExecutionYear
								.newInfoFromDomain(executionYear);
					}
				});

	}

}