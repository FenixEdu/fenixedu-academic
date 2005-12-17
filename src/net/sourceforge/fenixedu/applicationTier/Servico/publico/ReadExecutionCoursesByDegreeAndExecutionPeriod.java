package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 */
public class ReadExecutionCoursesByDegreeAndExecutionPeriod implements IService {

	public Object run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoExecutionCourseList = new ArrayList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		IPersistentExecutionCourse executionCourseDAO = sp.getIPersistentExecutionCourse();

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
			IExecutionCourse aux = (IExecutionCourse) executionCourseList.get(i);
			InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(aux);
			infoExecutionCourseList.add(infoExecutionCourse);
		}

		return infoExecutionCourseList;

	}

}