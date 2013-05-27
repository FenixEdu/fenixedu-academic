package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionYearByID {

    @Service
    public static InfoExecutionYear run(final Integer executionYearId) {
        final ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearId);
        return (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}