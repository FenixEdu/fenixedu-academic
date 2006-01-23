package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCourseByID extends Service {

	public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {

		ExecutionCourse executionCourse = null;
		InfoExecutionCourseWithNonAffiliatedTeachers infoExecutionCourse = new InfoExecutionCourseWithNonAffiliatedTeachers();

		executionCourse = (ExecutionCourse) persistentObject.readByOID(
				ExecutionCourse.class, idInternal);

		if (executionCourse == null) {
			throw new NonExistingServiceException();
		}

		infoExecutionCourse.copyFromDomain(executionCourse);

		return infoExecutionCourse;
	}

}
