package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionPeriod extends FenixService {

    @Service
    public static InfoExecutionPeriod run(final String name, final InfoExecutionYear infoExecutionYear) {

        final ExecutionSemester executionSemester =
                ExecutionSemester.readByNameAndExecutionYear(name, infoExecutionYear.getYear());
        return (executionSemester != null) ? InfoExecutionPeriod.newInfoFromDomain(executionSemester) : null;
    }

}