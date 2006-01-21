package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;

/**
 * @author João Mota
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriod extends Service {

	public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionCourseList = new ArrayList();

		IPersistentExecutionCourse executionCourseDAO = persistentSupport.getIPersistentExecutionCourse();

		List executionCourseList = new ArrayList();
		List temp = null;
		for (int i = 1; i < 6; i++) {
			temp = executionCourseDAO.readByCurricularYearAndExecutionPeriodAndExecutionDegree(
					new Integer(i), infoExecutionPeriod.getSemester(), infoExecutionDegree
							.getInfoDegreeCurricularPlan().getName(), infoExecutionDegree
							.getInfoDegreeCurricularPlan().getInfoDegree().getSigla(),
					infoExecutionPeriod.getIdInternal());
			executionCourseList.addAll(temp);
		}

		for (int i = 0; i < executionCourseList.size(); i++) {
			ExecutionCourse aux = (ExecutionCourse) executionCourseList.get(i);
			InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(aux);
			infoExecutionCourseList.add(infoExecutionCourse);
		}

		return infoExecutionCourseList;

	}

}