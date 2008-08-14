package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;

public class ReviseThesis extends Service {

    public void run(Thesis thesis) {
	thesis.allowRevision();
    }

}
