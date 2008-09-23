package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCourseByOID extends FenixService {

    public InfoExecutionCourse run(Integer oid) {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(oid);
	return (executionCourse != null) ? InfoExecutionCourse.newInfoFromDomain(executionCourse) : null;
    }

}
