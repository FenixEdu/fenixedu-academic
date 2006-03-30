package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriod extends Service {

	public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionCourseList = new ArrayList();

		List executionCourseList = new ArrayList();
		DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.readByNameAndDegreeSigla(infoExecutionDegree.getInfoDegreeCurricularPlan().getName(), infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
		if(degreeCurricularPlan != null) {
			ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
			List temp = null;
			for (int i = 1; i < 6; i++) {
				temp = degreeCurricularPlan.getExecutionCoursesByExecutionPeriodAndSemesterAndYear(executionPeriod, i, infoExecutionPeriod.getSemester());
				executionCourseList.addAll(temp);
			}
		}
		
		for (int i = 0; i < executionCourseList.size(); i++) {
			ExecutionCourse aux = (ExecutionCourse) executionCourseList.get(i);
			InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(aux);
			infoExecutionCourseList.add(infoExecutionCourse);
		}

		return infoExecutionCourseList;

	}

}