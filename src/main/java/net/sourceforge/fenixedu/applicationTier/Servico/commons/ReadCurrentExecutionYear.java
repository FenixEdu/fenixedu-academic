package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurrentExecutionYear {

    @Service
    public static InfoExecutionYear run() {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }

}