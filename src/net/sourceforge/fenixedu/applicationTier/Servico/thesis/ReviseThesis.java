package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ReviseThesis extends Service {

    public void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis) {
        thesis.allowRevision();
    }
    
}
