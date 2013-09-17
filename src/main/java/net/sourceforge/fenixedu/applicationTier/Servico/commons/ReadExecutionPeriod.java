package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionPeriod {

    @Atomic
    public static InfoExecutionPeriod run(final String name, final InfoExecutionYear infoExecutionYear) {

        final ExecutionSemester executionSemester =
                ExecutionSemester.readByNameAndExecutionYear(name, infoExecutionYear.getYear());
        return (executionSemester != null) ? InfoExecutionPeriod.newInfoFromDomain(executionSemester) : null;
    }

}