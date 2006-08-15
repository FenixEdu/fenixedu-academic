package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão Create on 13/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriod extends Service {

	public List<InfoExecutionDegree> run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException, ExcepcaoPersistencia {
		if (degreeId == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Execution OccupationPeriod
		ExecutionPeriod executionPeriod;
		if (executionPeriodId == null) {
			executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
		} else {
			executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		}

		if (executionPeriod == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		ExecutionYear executionYear = executionPeriod.getExecutionYear();
		if (executionYear == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Degree
		Degree degree = rootDomainObject.readDegreeByOID(degreeId);
		if (degree == null) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

		// Execution degrees
		List<ExecutionDegree> executionDegreeList = ExecutionDegree.getAllByDegreeAndExecutionYear(degree, executionYear.getYear());
		if (executionDegreeList == null || executionDegreeList.size() <= 0) {
			throw new FenixServiceException("error.impossibleDegreeSite");
		}

        List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();
		ListIterator listIterator = executionDegreeList.listIterator();
		while (listIterator.hasNext()) {
			ExecutionDegree executionDegree = (ExecutionDegree) listIterator.next();

			InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
					.newInfoFromDomain(executionDegree);

			result.add(infoExecutionDegree);
		}

		return result;
	}
}