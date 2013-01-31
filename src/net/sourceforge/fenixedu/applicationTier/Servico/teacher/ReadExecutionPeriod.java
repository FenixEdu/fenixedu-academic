package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class ReadExecutionPeriod extends FenixService {

	public InfoExecutionPeriod run(Integer executionCourseCode) throws FenixServiceException {
		ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
		ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
		return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
	}

}
