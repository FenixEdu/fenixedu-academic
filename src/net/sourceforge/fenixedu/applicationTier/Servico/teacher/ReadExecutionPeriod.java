package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriod extends Service {

    public InfoExecutionPeriod run(Integer executionCourseCode) throws FenixServiceException {
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
	ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
	return InfoExecutionPeriod.newInfoFromDomain(executionSemester);
    }

}
