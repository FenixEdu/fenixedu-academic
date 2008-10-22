package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import pt.ist.fenixWebFramework.services.Service;

public class CancelResidenceEvent extends FenixService {

    @Service
    public static void run(final ResidenceEvent residenceEvent, Employee employee) {
	residenceEvent.cancel(employee);
    }

}