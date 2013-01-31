/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseHasProposals extends FenixService {

	public Boolean run(Integer executionCourseCode) throws FenixServiceException {
		boolean result = false;
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);

		result = executionCourse.hasProposals();

		return result;

	}
}
