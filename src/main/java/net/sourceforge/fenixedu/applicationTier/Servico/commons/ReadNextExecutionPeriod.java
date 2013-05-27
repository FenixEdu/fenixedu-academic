package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadNextExecutionPeriod {

    @Service
    public static InfoExecutionPeriod run(final Integer oid) {
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(oid);
        return (executionSemester != null && executionSemester.getNextExecutionPeriod() != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester) : null;
    }

}