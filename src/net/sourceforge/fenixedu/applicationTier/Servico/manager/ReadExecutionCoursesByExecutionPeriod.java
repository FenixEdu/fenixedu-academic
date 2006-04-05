package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionCoursesByExecutionPeriod extends Service {

	public List run(Integer executionPeriodId) throws FenixServiceException, ExcepcaoPersistencia {
		List allExecutionCoursesFromExecutionPeriod = null;
		List<InfoExecutionCourse> allExecutionCourses = null;

		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);

		if (executionPeriod == null) {
			throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
		}
		allExecutionCoursesFromExecutionPeriod = executionPeriod.getAssociatedExecutionCourses();

		if (allExecutionCoursesFromExecutionPeriod == null
				|| allExecutionCoursesFromExecutionPeriod.isEmpty()) {
			return allExecutionCoursesFromExecutionPeriod;
		}
		InfoExecutionCourse infoExecutionCourse = null;
		allExecutionCourses = new ArrayList<InfoExecutionCourse>(allExecutionCoursesFromExecutionPeriod.size());
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