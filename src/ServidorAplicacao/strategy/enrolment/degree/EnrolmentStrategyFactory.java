package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLERCI;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyFactory {

	public static final int LERCI = 1;

	private static IEnrolmentStrategy strategyInstance = null;

	//	private EnrolmentContext enrolmentContext = null;

	private static SuportePersistenteOJB persistentSupport = null;
	private static IStudentCurricularPlanPersistente persistentStudentCurricularPlan = null;
	private static IPersistentCurricularCourse persistentCurricularCourse = null;
	private static IPersistentEnrolment persistentEnrolment = null;

	public static synchronized IEnrolmentStrategy getEnrolmentStrategyInstance(int degree, EnrolmentContext enrolmentContext)
		throws ExcepcaoPersistencia {

		if (strategyInstance == null) {
			switch (degree) {
				case LERCI :
					strategyInstance = new EnrolmentStrategyLERCI();
					strategyInstance.setEnrolmentContext(prepareEnrolmentContext(enrolmentContext));
					break;
				default :
					break;
			}
		}
		return strategyInstance;
	}

	public static synchronized void resetInstance() {
		if (strategyInstance != null) {
			strategyInstance = null;
		}
	}

	private static EnrolmentContext prepareEnrolmentContext(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
		persistentSupport = SuportePersistenteOJB.getInstance();
		persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
		persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		if ((enrolmentContext.getStudent() == null) || (enrolmentContext.getSemester() == null) || (enrolmentContext.getDegree() == null)) {
			throw new ExcepcaoPersistencia();
		}

		List list1 = computeCurricularCoursesFromStudentDegreeCurricularPlan(enrolmentContext.getStudent());
		List list2 = computeAprovedCurricularCoursesFromStudent(enrolmentContext.getStudent());
		enrolmentContext.setFinalCurricularCoursesSpanToBeEnrolled(computeCurricularCoursesNotYetDoneByStudent(list1, list2));
		enrolmentContext.setCurricularCoursesDoneByStudent(list2);

		//		enrolmentContext.setStudent(student);
		//		enrolmentContext.setSemester(semester);
		//		enrolmentContext.setDegree(degree);
		return enrolmentContext;
	}

	private static List computeCurricularCoursesFromStudentDegreeCurricularPlan(IStudent student) throws ExcepcaoPersistencia {

		IStudentCurricularPlan studentCurricularPlan = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		List curricularCoursesList = null;

		studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
		degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
		curricularCoursesList = persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);

		return curricularCoursesList;
	}

	private static List computeAprovedCurricularCoursesFromStudent(IStudent student) throws ExcepcaoPersistencia {

		IStudentCurricularPlan studentCurricularPlan = null;
		List enrolmentsList = null;
		List curricularCoursesList = null;

		studentCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());
		enrolmentsList =
			persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
				studentCurricularPlan,
				new EnrolmentState(EnrolmentState.APROVED));

		Iterator iterator = enrolmentsList.iterator();
		curricularCoursesList = new ArrayList();
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			curricularCoursesList.add(enrolment.getCurricularCourse());
		}

		return curricularCoursesList;
	}

	private static List computeCurricularCoursesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent) {

		return (List) CollectionUtils.subtract(curricularCoursesFromStudentDegreeCurricularPlan, aprovedCurricularCoursesFromStudent);
			
		/*Iterator iteratorAproved = aprovedCurricularCoursesFromStudent.iterator();
		while (iteratorAproved.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iteratorAproved.next();
			if (curricularCoursesFromStudentDegreeCurricularPlan.contains(curricularCourse)) {
				curricularCoursesFromStudentDegreeCurricularPlan.remove(curricularCourse);
			}
		}
		return curricularCoursesFromStudentDegreeCurricularPlan;*/
	}

}