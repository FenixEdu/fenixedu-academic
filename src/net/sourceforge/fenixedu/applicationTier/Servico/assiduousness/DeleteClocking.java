package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Anulation;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;

public class DeleteClocking extends Service {

    public void run(Clocking clocking, Employee employee) {
	if (clocking.getAnulation() != null) {
	    clocking.getAnulation().setState(AnulationState.VALID);
	    clocking.getAnulation().setLastModifiedDate(new DateTime());
	    clocking.getAnulation().setModifiedBy(employee);
	} else {
	    new Anulation(clocking, employee);
	}
    }

}
