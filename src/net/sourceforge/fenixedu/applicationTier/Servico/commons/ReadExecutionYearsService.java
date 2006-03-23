package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadExecutionYearsService extends Service {

	public List run() throws ExcepcaoPersistencia {
		return (List) CollectionUtils.collect(rootDomainObject.getExecutionYears(),
				new Transformer() {
					public Object transform(Object arg0) {
						final ExecutionYear executionYear = (ExecutionYear) arg0;
						return InfoExecutionYear.newInfoFromDomain(executionYear);
					}
				});
	}
	
	public ExecutionYear run(Integer executionYearID) throws ExcepcaoPersistencia {
		return rootDomainObject.readExecutionYearByOID(executionYearID);
	}
}