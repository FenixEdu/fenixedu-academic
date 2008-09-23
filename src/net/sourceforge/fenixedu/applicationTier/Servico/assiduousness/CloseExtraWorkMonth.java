package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;

public class CloseExtraWorkMonth extends FenixService {

    public void run(ClosedMonth closedMonth) {
	closedMonth.setClosedForExtraWork(true);
	return;
    }
}