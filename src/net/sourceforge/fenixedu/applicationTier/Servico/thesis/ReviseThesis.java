package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;

public class ReviseThesis extends FenixService {

    public void run(Thesis thesis) {
	thesis.allowRevision();
    }

}
