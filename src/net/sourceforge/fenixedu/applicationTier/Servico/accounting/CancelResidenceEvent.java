package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;

public class CancelResidenceEvent extends Service {

    public void run(final ResidenceEvent residenceEvent, Employee employee) {
	residenceEvent.cancel(employee);
    }

}
