package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionDegreesByExecutionYearId extends FenixService {

	@Service
	public static List run(Integer executionYearId) {

		List<InfoExecutionDegree> infoExecutionDegreeList = null;

		ExecutionYear executionYear = null;
		if (executionYearId == null) {
			executionYear = ExecutionYear.readCurrentExecutionYear();
		} else {
			executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
		}

		List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());

		if (executionDegrees != null && executionDegrees.size() > 0) {
			Iterator iterator = executionDegrees.iterator();
			infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();

			while (iterator.hasNext()) {
				ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
				InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
				infoExecutionDegreeList.add(infoExecutionDegree);
			}
		}

		return infoExecutionDegreeList;
	}

}