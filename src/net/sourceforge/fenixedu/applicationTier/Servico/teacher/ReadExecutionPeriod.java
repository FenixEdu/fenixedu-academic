/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class ReadExecutionPeriod implements IService {

	public InfoExecutionPeriod run(Integer executionCourseCode) throws FenixServiceException, ExcepcaoPersistencia {

		IPersistentExecutionCourse persistentExecutionCourse = null;
		ExecutionPeriod executionPeriod = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		persistentExecutionCourse = sp.getIPersistentExecutionCourse();
		ExecutionCourse executionCourse = (ExecutionCourse) persistentExecutionCourse.readByOID(
				ExecutionCourse.class, executionCourseCode);

		executionPeriod = executionCourse.getExecutionPeriod();

		return InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
	}
}