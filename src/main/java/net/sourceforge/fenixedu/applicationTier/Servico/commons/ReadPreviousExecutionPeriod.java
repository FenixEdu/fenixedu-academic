package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadPreviousExecutionPeriod {

    @Atomic
    public static InfoExecutionPeriod run(final String oid) {
        final ExecutionSemester executionSemester = FenixFramework.getDomainObject(oid);
        return (executionSemester != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}