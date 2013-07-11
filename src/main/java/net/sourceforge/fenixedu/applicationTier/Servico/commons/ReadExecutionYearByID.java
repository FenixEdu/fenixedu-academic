package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionYearByID {

    @Service
    public static InfoExecutionYear run(final String executionYearId) {
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearId);
        return (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}