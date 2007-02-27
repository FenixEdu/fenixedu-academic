package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;

public class RestoreClocking extends Service {

    public void run(Clocking clocking, Employee employee) {
	clocking.getAnulation().setState(AnulationState.INVALID);
	clocking.getAnulation().setLastModifiedDate(new DateTime());
	clocking.getAnulation().setModifiedBy(employee);
    }

}
