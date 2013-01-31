package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCourseByOID extends FenixService {

	@Service
	public static InfoExecutionCourse run(Integer oid) {
		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
		return (executionCourse != null) ? InfoExecutionCourse.newInfoFromDomain(executionCourse) : null;
	}

}