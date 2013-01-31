/*
 * Created on 27/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */
public class CreateSiteInExecutionCourse extends FenixService {

	@Checked("RolePredicates.MANAGER_PREDICATE")
	@Service
	public static void run(Integer executionCourseId) throws FenixServiceException {
		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
		if (executionCourse == null) {
			throw new NonExistingServiceException("message.non.existing.execution.course", null);
		}
		executionCourse.createSite();
	}
}