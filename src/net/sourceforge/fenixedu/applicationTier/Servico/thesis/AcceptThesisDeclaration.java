package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

public class AcceptThesisDeclaration extends Service {

    public void run(Thesis thesis, ThesisVisibilityType visibility) {
        thesis.acceptDeclaration(visibility);
    }
}
