package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

public class DeleteThesis extends FenixService {

    public void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis) {
	thesis.delete();
    }

}
