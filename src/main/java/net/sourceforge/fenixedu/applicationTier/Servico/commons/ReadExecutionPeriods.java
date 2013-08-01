package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionPeriods {

    @Atomic
    public static List run() {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : Bennu.getInstance().getExecutionPeriodsSet()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }

}