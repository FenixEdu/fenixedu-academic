package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.services.Service;

public class CancelSubmitThesis extends FenixService {

    @Service
    public static void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis) {
        thesis.cancelSubmit();
    }

}