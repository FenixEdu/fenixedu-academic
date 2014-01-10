package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;

public class ReadExecutionYear {
    @Atomic
    public static InfoExecutionYear run(String year) {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readExecutionYearByName(year));
    }
}