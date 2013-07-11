package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear {

    @Atomic
    public static List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        final ExecutionYear executionYear;
        if (infoExecutionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = FenixFramework.getDomainObject(infoExecutionYear.getExternalId());
        }

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : executionYear.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }
}