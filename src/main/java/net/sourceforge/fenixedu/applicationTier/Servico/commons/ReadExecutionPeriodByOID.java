package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriodByOID extends FenixService {

    @Service
    public static InfoExecutionPeriod run(final Integer executionPeriodID) {
        final ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

        return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}