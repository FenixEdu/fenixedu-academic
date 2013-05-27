package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadPreviousExecutionPeriod {

    @Service
    public static InfoExecutionPeriod run(final Integer oid) {
        final ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(oid);
        return (executionSemester != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}