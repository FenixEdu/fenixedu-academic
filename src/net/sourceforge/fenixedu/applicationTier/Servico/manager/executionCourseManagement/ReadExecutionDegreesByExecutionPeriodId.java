package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 *  
 */
public class ReadExecutionDegreesByExecutionPeriodId extends Service {

    public List run(Integer executionPeriodId) throws FenixServiceException {
	if (executionPeriodId == null) {
	    throw new FenixServiceException("nullId");
	}
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	List executionDegrees = ExecutionDegree.getAllByExecutionYear(executionSemester.getExecutionYear().getYear());

	Iterator iterator = executionDegrees.iterator();
	List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
	while (iterator.hasNext()) {
	    final ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
	    final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
	    infoExecutionDegreeList.add(infoExecutionDegree);
	}

	return infoExecutionDegreeList;
    }
}