package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class RejectThesisDeclaration extends Service {

    public void run(Thesis thesis) {
        thesis.rejectDeclaration();
    }
    
}
