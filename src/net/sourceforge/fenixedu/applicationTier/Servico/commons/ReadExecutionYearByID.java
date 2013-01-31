package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionYearByID extends FenixService {

	@Service
	public static InfoExecutionYear run(final Integer executionYearId) {
		final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
		return (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
	}

}