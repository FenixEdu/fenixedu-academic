/*
 * Created on 10/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;

/**
 * @author jpvl
 */
public abstract class EnrolmentContextManager {
	public static EnrolmentContext initialEnrolmentContext(
		IStudent student,
		Integer semester) throws ExcepcaoPersistencia {
		EnrolmentContext enrolmentContext = new EnrolmentContext();

		ISuportePersistente persistentSupport =
			SuportePersistenteOJB.getInstance();

		IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
			persistentSupport.getIStudentCurricularPlanPersistente();

		IPersistentEnrolment persistentEnrolment =
			persistentSupport.getIPersistentEnrolment();

		IStudentCurricularPlan studentActiveCurricularPlan =
			persistentStudentCurricularPlan.readActiveStudentCurricularPlan(
				student.getNumber(),
				student.getDegreeType());

		List studentCurricularPlanCurricularCourses =
			studentActiveCurricularPlan
				.getDegreeCurricularPlan()
				.getCurricularCourses();

		final List studentEnrolments =
			persistentEnrolment.readAllByStudentCurricularPlan(
				studentActiveCurricularPlan);

		final List studentEnrolmentsWithStateApproved =
			(List) CollectionUtils.select(studentEnrolments, new Predicate() {

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getState().equals(
					new EnrolmentState(EnrolmentState.APROVED));
			}
		});

		final List studentDoneCurricularCourses =
			(
				List) CollectionUtils
					.collect(
						studentEnrolmentsWithStateApproved,
						new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		List enrolmentsWithStateNotApproved =
			(List) CollectionUtils.select(studentEnrolments, new Predicate() {

			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				ICurricularCourse curricularCourse =
					enrolment.getCurricularCourse();
				return !studentDoneCurricularCourses.contains(curricularCourse)
					&& enrolment.getState().equals(
						new EnrolmentState(EnrolmentState.NOT_APROVED));
			}

		});

		List curricularCoursesEnrolled =
			(
				List) CollectionUtils
					.collect(enrolmentsWithStateNotApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		enrolmentContext.setStudent(student);

		
		enrolmentContext.setSemester(semester);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(
			computeCurricularCoursesScopesNotYetDoneByStudent(
				studentCurricularPlanCurricularCourses,
				studentDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(
			studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(
			CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(
			studentActiveCurricularPlan);

		return enrolmentContext;
	}

	private static List computeCurricularCoursesScopesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent)
		throws ExcepcaoPersistencia {

		List scopesNotDone = new ArrayList();

		List coursesNotDone =
			(List) CollectionUtils.subtract(
				curricularCoursesFromStudentDegreeCurricularPlan,
				aprovedCurricularCoursesFromStudent);

		Iterator iteratorCourses = coursesNotDone.iterator();
		while (iteratorCourses.hasNext()) {
			ICurricularCourse curricularCourse =
				(ICurricularCourse) iteratorCourses.next();
			List scopes = curricularCourse.getScopes();
			Iterator iteratorScopes = scopes.iterator();
			while (iteratorScopes.hasNext()) {
				ICurricularCourseScope curricularCourseScope =
					(ICurricularCourseScope) iteratorScopes.next();
				scopesNotDone.add(curricularCourseScope);
			}
		}

		return scopesNotDone;
	}

}
