package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteExecutionDegreesOfDegreeCurricularPlan extends Service {

	public List run(List<Integer> executionDegreesIds) throws FenixServiceException{
		List<String> undeletedExecutionDegreesYears = new ArrayList<String>();

		for (final Integer executionDegreeId : executionDegreesIds) {
            final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);

            if (executionDegree != null) {
				if (executionDegree.canBeDeleted()) {
					executionDegree.delete();
				} else {
                    undeletedExecutionDegreesYears.add(executionDegree.getExecutionYear().getYear());    
                }
			}
		}

		return undeletedExecutionDegreesYears;
	}
    
}
