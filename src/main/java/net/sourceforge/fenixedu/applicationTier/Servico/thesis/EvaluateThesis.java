package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.ScientificComissionMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.ist.fenixWebFramework.services.Service;

public class EvaluateThesis extends FenixService {

    protected void run(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis, Integer mark) {
        thesis.confirm(mark);
    }

    // Service Invokers migrated from Berserk

    private static final EvaluateThesis serviceInstance = new EvaluateThesis();

    @Service
    public static void runEvaluateThesis(DegreeCurricularPlan degreeCurricularPlan, Thesis thesis, Integer mark) throws NotAuthorizedException {
        ScientificComissionMemberAuthorizationFilter.instance.execute(degreeCurricularPlan, thesis, mark);
        serviceInstance.run(degreeCurricularPlan, thesis, mark);
    }

}