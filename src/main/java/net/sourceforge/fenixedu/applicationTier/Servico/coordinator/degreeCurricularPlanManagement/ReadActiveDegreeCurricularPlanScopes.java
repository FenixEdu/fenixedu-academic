package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quitério 5/Nov/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 *           23/11/2004
 * 
 */

public class ReadActiveDegreeCurricularPlanScopes extends ReadDegreeCurricularPlanBaseService {

    protected List run(final String degreeCurricularPlanId) {
        return super.readActiveCurricularCourseScopes(degreeCurricularPlanId);
    }

    // Service Invokers migrated from Berserk

    private static final ReadActiveDegreeCurricularPlanScopes serviceInstance = new ReadActiveDegreeCurricularPlanScopes();

    @Atomic
    public static List runReadActiveDegreeCurricularPlanScopes(String degreeCurricularPlanId) throws NotAuthorizedException {
        CoordinatorAuthorizationFilter.instance.execute();
        return serviceInstance.run(degreeCurricularPlanId);
    }

}