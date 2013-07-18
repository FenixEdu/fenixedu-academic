package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriodByOID {

    @Service
    public static InfoExecutionPeriod run(final Integer executionPeriodID) {
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);

        return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}