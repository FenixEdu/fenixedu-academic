/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByExecutionPeriod implements IService {

	/**
	 * Executes the service. Returns the current collection of
	 * infoExecutionCourses.
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public List run(Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
		List allExecutionCoursesFromExecutionPeriod = null;
		List allExecutionCourses = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

		ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentExecutionPeriod.readByOID(
				ExecutionPeriod.class, executionPeriodId);

		if (executionPeriod == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}
		allExecutionCoursesFromExecutionPeriod = executionPeriod.getAssociatedExecutionCourses();

		if (allExecutionCoursesFromExecutionPeriod == null
				|| allExecutionCoursesFromExecutionPeriod.isEmpty()) {
			return allExecutionCoursesFromExecutionPeriod;
		}
		InfoExecutionCourse infoExecutionCourse = null;
		allExecutionCourses = new ArrayList(allExecutionCoursesFromExecutionPeriod.size());
		Iterator iter = allExecutionCoursesFromExecutionPeriod.iterator();
		while (iter.hasNext()) {
			ExecutionCourse executionCourse = (ExecutionCourse) iter.next();
			Boolean hasSite;
			if (executionCourse.getSite() != null)
				hasSite = true;
			else
				hasSite = false;

			infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
					.newInfoFromDomain(executionCourse);
			infoExecutionCourse.setHasSite(hasSite);
			allExecutionCourses.add(infoExecutionCourse);
		}

		return allExecutionCourses;
	}
}