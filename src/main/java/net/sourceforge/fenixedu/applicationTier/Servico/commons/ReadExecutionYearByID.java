package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionYearByID {

    @Atomic
    public static InfoExecutionYear run(final String executionYearId) {
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearId);
        return (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}