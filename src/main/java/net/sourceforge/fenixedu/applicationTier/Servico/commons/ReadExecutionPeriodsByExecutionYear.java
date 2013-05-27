package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriodsByExecutionYear {

    @Service
    public static List run(Integer executionYearId) {
        final ExecutionYear executionYear =
                executionYearId != null ? RootDomainObject.getInstance().readExecutionYearByOID(executionYearId) : ExecutionYear
                        .readCurrentExecutionYear();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return infoExecutionPeriods;
    }
}