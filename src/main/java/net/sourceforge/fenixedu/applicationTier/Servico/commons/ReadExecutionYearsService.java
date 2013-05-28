package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionYearsService {

    @Service
    public static List run() {
        final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
        for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
            infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }
        return infoExecutionYears;
    }

    @Service
    public static ExecutionYear run(Integer executionYearID) {
        return AbstractDomainObject.fromExternalId(executionYearID);
    }
}