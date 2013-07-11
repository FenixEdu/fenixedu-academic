package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;

public class ReadNotClosedPublicExecutionPeriods {

    @Atomic
    public static List run() {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionSemester executionSemester : ExecutionSemester.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }
}