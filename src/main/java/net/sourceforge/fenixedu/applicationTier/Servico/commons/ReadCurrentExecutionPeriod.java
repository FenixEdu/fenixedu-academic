package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurrentExecutionPeriod {
    @Service
    public static InfoExecutionPeriod run() {
        return InfoExecutionPeriod.newInfoFromDomain(ExecutionSemester.readActualExecutionSemester());
    }
}