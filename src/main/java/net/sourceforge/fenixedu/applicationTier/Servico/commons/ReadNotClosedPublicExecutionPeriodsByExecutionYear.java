package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear {

    @Service
    public static List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        final ExecutionYear executionYear;
        if (infoExecutionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = AbstractDomainObject.fromExternalId(infoExecutionYear.getExternalId());
        }

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : executionYear.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }
}