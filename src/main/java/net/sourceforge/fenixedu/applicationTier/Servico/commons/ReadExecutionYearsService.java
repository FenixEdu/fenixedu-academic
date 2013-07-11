package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionYearsService {

    @Atomic
    public static List run() {
        final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
        for (final ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
            infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }
        return infoExecutionYears;
    }

    @Atomic
    public static ExecutionYear run(String executionYearID) {
        return FenixFramework.getDomainObject(executionYearID);
    }
}