package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/*
 * 
 * @author Fernanda Quitério 29/Dez/2003
 */
public class AssociateCurricularCoursesToExecutionCourse extends Service {

	public void run(Integer executionCourseId, List curricularCourseIds) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
				.getIPersistentExecutionCourse();
		IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
				.getIPersistentCurricularCourse();

		if (executionCourseId == null) {
			throw new FenixServiceException("nullExecutionCourseId");
		}

		if (curricularCourseIds != null) {
			ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
					ExecutionCourse.class, executionCourseId);

			if (executionCourse == null) {
				throw new NonExistingServiceException("noExecutionCourse");
			}

			Iterator iter = curricularCourseIds.iterator();
			while (iter.hasNext()) {
				Integer curricularCourseId = (Integer) iter.next();

				CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse
						.readByOID(CurricularCourse.class, curricularCourseId);
				if (curricularCourse == null) {
					throw new NonExistingServiceException("noCurricularCourse");
				}
				if (!curricularCourse.hasAssociatedExecutionCourses(executionCourse)) {
					curricularCourse.addAssociatedExecutionCourses(executionCourse);
				}
			}
		}
	}
}
