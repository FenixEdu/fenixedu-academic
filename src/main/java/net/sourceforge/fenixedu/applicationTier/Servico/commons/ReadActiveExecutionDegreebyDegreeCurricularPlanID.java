package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID {

    @Service
    public static InfoExecutionDegree run(final String degreeCurricularPlanID) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}