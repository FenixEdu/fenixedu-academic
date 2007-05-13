package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;

public class OpenExtraWorkClosedMonth extends Service {

    public void run(ClosedMonth closedMonth) {
	closedMonth.setClosedForExtraWork(false);
    }
}