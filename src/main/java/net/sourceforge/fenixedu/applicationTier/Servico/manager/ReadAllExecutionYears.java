package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixframework.Atomic;

public class ReadAllExecutionYears {

    @Atomic
    public static List<InfoExecutionYear> run() throws FenixServiceException {
        List<InfoExecutionYear> result = new ArrayList<InfoExecutionYear>();

        for (ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
            result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }

        return result;
    }

}