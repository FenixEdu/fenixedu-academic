package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;

public class ReadCurrentExecutionYear {

    @Atomic
    public static InfoExecutionYear run() {
        return InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
    }

}