package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID extends Service {

    public InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws FenixServiceException,
            ExcepcaoPersistencia {

    	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}
