package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCourse extends Service {

    public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException {
	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(idInternal);

	if (executionCourse == null) {
	    throw new NonExistingServiceException();
	}

	return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

}