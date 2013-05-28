package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadPreviousExecutionPeriod {

    @Service
    public static InfoExecutionPeriod run(final Integer oid) {
        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(oid);
        return (executionSemester != null) ? InfoExecutionPeriod
                .newInfoFromDomain(executionSemester.getPreviousExecutionPeriod()) : null;
    }

}