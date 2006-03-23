package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ReadCurrentExecutionPeriod extends Service {
    public InfoExecutionPeriod run() {
        return InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(ExecutionPeriod.readActualExecutionPeriod());
    }
}