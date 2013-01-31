/*
 * Created on 10/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */
public class ReadExecutionPeriod extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static InfoExecutionPeriod run(Integer executionPeriodId) throws FenixServiceException {
		ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
		if (executionSemester == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}

		return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
	}

}