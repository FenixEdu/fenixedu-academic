package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;

public class RestoreClocking extends FenixService {

    public void run(Clocking clocking, Employee employee) {
	clocking.restore(employee);
    }

}
