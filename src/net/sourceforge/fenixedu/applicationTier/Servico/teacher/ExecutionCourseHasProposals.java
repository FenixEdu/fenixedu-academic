/*
 * Created on 09/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class ExecutionCourseHasProposals implements IService {

	public boolean run(Integer executionCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		boolean result = false;
		IPersistentExecutionCourse persistentExecutionCourse = null;

		ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

		persistentExecutionCourse = ps.getIPersistentExecutionCourse();

		ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
				ExecutionCourse.class, executionCourseCode);

		result = executionCourse.hasProposals();

		return result;

	}
}
