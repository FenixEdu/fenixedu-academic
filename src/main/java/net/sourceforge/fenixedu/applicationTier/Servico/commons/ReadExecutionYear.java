package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionYear {
    @Service
    public static InfoExecutionYear run(String year) {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readExecutionYearByName(year));
    }
}