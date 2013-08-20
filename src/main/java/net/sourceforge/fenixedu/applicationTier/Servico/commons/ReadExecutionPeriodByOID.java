package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionPeriodByOID {

    @Service
    public static InfoExecutionPeriod run(final String executionPeriodID) {
        final ExecutionSemester executionSemester = AbstractDomainObject.fromExternalId(executionPeriodID);

        return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}