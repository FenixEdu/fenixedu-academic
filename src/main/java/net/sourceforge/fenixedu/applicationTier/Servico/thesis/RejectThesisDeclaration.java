package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class RejectThesisDeclaration extends FenixService {

    public void run(Thesis thesis) {
        thesis.rejectDeclaration();
    }

}
