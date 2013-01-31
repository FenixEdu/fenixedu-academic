package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import pt.ist.fenixWebFramework.services.Service;

public class ReadActiveExecutionDegreebyDegreeCurricularPlanID extends FenixService {

	@Service
	public static InfoExecutionDegree run(final Integer degreeCurricularPlanID) throws FenixServiceException {

		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
		if (degreeCurricularPlan == null) {
			throw new FenixServiceException("error.impossibleEditDegreeInfo");
		}

		final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();
		return InfoExecutionDegree.newInfoFromDomain(executionDegree);
	}

}