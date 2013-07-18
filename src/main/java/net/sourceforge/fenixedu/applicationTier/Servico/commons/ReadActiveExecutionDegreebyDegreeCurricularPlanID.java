package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID {

    @Service
    public static InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}