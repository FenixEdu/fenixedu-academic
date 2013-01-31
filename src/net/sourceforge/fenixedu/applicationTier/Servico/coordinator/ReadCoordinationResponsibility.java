/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationResponsibility extends FenixService {

	public Boolean run(Integer executionDegreeId, IUserView userView) throws FenixServiceException {
		ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
		Coordinator coordinator = executionDegree.getCoordinatorByTeacher(userView.getPerson());

		if (coordinator == null || !coordinator.getResponsible().booleanValue()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}