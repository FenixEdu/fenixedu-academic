package net.sourceforge.fenixedu.applicationTier.Servico.manager.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/*
 * 
 * @author Fernanda Quitério 22/Dez/2003
 */

public class ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear extends Service {

	public Object run(Integer executionDegreeId, Integer executionPeriodId, Integer curricularYear)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionCourseList = new ArrayList();

		List executionCourseList = null;
		IPersistentExecutionCourse executionCourseDAO = persistentSupport.getIPersistentExecutionCourse();

		if (executionPeriodId == null) {
			throw new FenixServiceException("nullExecutionPeriodId");
		}

		ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject.readByOID(
				ExecutionPeriod.class, executionPeriodId);

		if (executionDegreeId == null && curricularYear == null) {
			executionCourseList = executionCourseDAO
					.readByExecutionPeriodWithNoCurricularCourses(executionPeriod.getIdInternal());

		} else {
			ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
					ExecutionDegree.class, executionDegreeId);

			executionCourseList = executionCourseDAO
					.readByCurricularYearAndExecutionPeriodAndExecutionDegree(curricularYear,
							executionPeriod.getSemester(), executionDegree.getDegreeCurricularPlan()
									.getName(), executionDegree.getDegreeCurricularPlan().getDegree()
									.getSigla(), executionPeriod.getIdInternal());
		}

		CollectionUtils.collect(executionCourseList, new Transformer() {
			public Object transform(Object input) {
				ExecutionCourse executionCourse = (ExecutionCourse) input;
				return InfoExecutionCourse.newInfoFromDomain(executionCourse);
			}
		}, infoExecutionCourseList);

		return infoExecutionCourseList;
	}
}