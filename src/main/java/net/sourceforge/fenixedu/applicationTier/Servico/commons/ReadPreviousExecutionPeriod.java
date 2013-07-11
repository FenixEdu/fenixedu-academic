package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ReadPreviousExecutionPeriod {

    @Service
    public static InfoExecutionPeriod run(final String oid) {
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(oid);
        return (executionSemester != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}