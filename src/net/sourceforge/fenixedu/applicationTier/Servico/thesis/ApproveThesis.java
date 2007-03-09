package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class ApproveThesis extends Service {

    public void run(Integer degreeCurricularPlanId, Thesis thesis) {
        thesis.approve();
    }
    
}
