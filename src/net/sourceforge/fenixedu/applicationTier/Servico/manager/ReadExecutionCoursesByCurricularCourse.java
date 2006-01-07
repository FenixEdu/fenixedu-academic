/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesByCurricularCourse implements IService {

	public List run(Integer curricularCourseId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp;
		List allExecutionCourses = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		CurricularCourse curricularCourse = (CurricularCourse) sp.getIPersistentCurricularCourse()
				.readByOID(CurricularCourse.class, curricularCourseId);

		if (curricularCourse == null) {

			throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
		}

		allExecutionCourses = curricularCourse.getAssociatedExecutionCourses();

		if (allExecutionCourses == null || allExecutionCourses.isEmpty()) {
			// return allExecutionCourses;
			return new ArrayList();
		}

		// build the result of this service
		Iterator iterator = allExecutionCourses.iterator();
		List result = new ArrayList(allExecutionCourses.size());

		Boolean hasSite;
		while (iterator.hasNext()) {

			InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
					.newInfoFromDomain((ExecutionCourse) iterator.next());

			ExecutionCourse executionCourse = (ExecutionCourse) sp.getIPersistentExecutionCourse()
					.readByOID(ExecutionCourse.class, infoExecutionCourse.getIdInternal());
			if (executionCourse.getSite() != null)
				hasSite = true;
			else
				hasSite = false;
			infoExecutionCourse.setHasSite(hasSite);
			result.add(infoExecutionCourse);
		}
		return result;
	}
}