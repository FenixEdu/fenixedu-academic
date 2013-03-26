package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadNextExecutionPeriod extends FenixService {

    @Service
    public static InfoExecutionPeriod run(final Integer oid) {
        final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(oid);
        return (executionSemester != null && executionSemester.getNextExecutionPeriod() != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester) : null;
    }

}