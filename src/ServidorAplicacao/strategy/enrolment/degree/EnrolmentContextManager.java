/*
 * Created on 10/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
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

	public static EnrolmentContext initialEnrolmentContext(IStudent student, Integer semester) throws ExcepcaoPersistencia {

		EnrolmentContext enrolmentContext = new EnrolmentContext();

		ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

		IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSupport.getIStudentCurricularPlanPersistente();

		IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();

		IStudentCurricularPlan studentActiveCurricularPlan = persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

		List degreeCurricularPlanCurricularCourses = studentActiveCurricularPlan.getDegreeCurricularPlan().getCurricularCourses();

		final List studentEnrolments = persistentEnrolment.readAllByStudentCurricularPlan(studentActiveCurricularPlan);

		final List studentEnrolmentsWithStateApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getState().equals(new EnrolmentState(EnrolmentState.APROVED));
			}
		});

		final List studentDoneCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		List enrolmentsWithStateNotApproved = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				ICurricularCourse curricularCourse = enrolment.getCurricularCourse();
				return !studentDoneCurricularCourses.contains(curricularCourse)
					&& enrolment.getState().equals(new EnrolmentState(EnrolmentState.NOT_APROVED));
			}
		});

		List curricularCoursesEnrolled = (List) CollectionUtils.collect(enrolmentsWithStateNotApproved, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return (enrolment.getCurricularCourse().getCode() + enrolment.getCurricularCourse().getName());
			}
		});

//		List studentCurricularPlanCurricularCourses = (List) CollectionUtils.select(computeScopesOfCurricularCourses(degreeCurricularPlanCurricularCourses), new Predicate() {
//			public boolean evaluate(Object obj) {
//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
//				return (curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch())) || (curricularCourseScope.getBranch().getName().equals(""));
//			}
//		});

		List studentCurricularPlanCurricularCourses = computeStudentCurricularPlanCurricularCourses(degreeCurricularPlanCurricularCourses, studentActiveCurricularPlan, semester);

		enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(studentCurricularPlanCurricularCourses);
		enrolmentContext.setStudent(student);
		enrolmentContext.setSemester(semester);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(computeCurricularCoursesScopesNotYetDoneByStudent(degreeCurricularPlanCurricularCourses, studentDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());

		return enrolmentContext;
	}


	public static List computeStudentCurricularPlanCurricularCourses(List degreeCurricularPlanCurricularCourses, IStudentCurricularPlan studentActiveCurricularPlan, Integer semester) {

		List scopesOfCurricularCourses = computeScopesOfCurricularCourses(degreeCurricularPlanCurricularCourses);
		Iterator iteratorScopesOfCurricularCourses = scopesOfCurricularCourses.iterator();
		List aux = new ArrayList();
		while (iteratorScopesOfCurricularCourses.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopesOfCurricularCourses.next();
			if( (curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch())) || (curricularCourseScope.getBranch().getName().equals("")) ) {
				if(!aux.contains(curricularCourseScope.getCurricularCourse())) {
					aux.add(curricularCourseScope.getCurricularCourse());
				}
			}
		}
		return aux;
	}








	private static List computeCurricularCoursesScopesNotYetDoneByStudent(
		List curricularCoursesFromStudentDegreeCurricularPlan,
		List aprovedCurricularCoursesFromStudent)
		throws ExcepcaoPersistencia {

		List scopesNotDone = new ArrayList();

		List coursesNotDone = (List) CollectionUtils.subtract(curricularCoursesFromStudentDegreeCurricularPlan, aprovedCurricularCoursesFromStudent);

		return computeScopesOfCurricularCourses(coursesNotDone);
	}


	private static List computeScopesOfCurricularCourses(List curricularCourses) {

		List scopes = new ArrayList();

		Iterator iteratorCourses = curricularCourses.iterator();
		while (iteratorCourses.hasNext()) {
			ICurricularCourse curricularCourse = (ICurricularCourse) iteratorCourses.next();
			List scopesTemp = curricularCourse.getScopes();
			Iterator iteratorScopes = scopesTemp.iterator();
			while (iteratorScopes.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopes.next();
				scopes.add(curricularCourseScope);
			}
		}

		return scopes;
	}

	public static EnrolmentContext getEnrolmentContext(InfoEnrolmentContext infoEnrolmentContext) {

		EnrolmentContext enrolmentContext = new EnrolmentContext();

		IStudent student = Cloner.copyInfoStudent2IStudent(infoEnrolmentContext.getInfoStudent());
		
		IStudentCurricularPlan studentActiveCurricularPlan =
			Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());

		List curricularCourseScopeList = new ArrayList();
		List infoCurricularCourseScopeList = null;

		infoCurricularCourseScopeList = infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (infoCurricularCourseScopeList != null && !infoCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope =
					Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList.add(curricularCourseScope);
			}
		}

		List curricularCourseScopeList2 = new ArrayList();
		List infoCurricularCourseScopeList2 = infoEnrolmentContext.getActualEnrolment();
		if (infoCurricularCourseScopeList2 != null && !infoCurricularCourseScopeList2.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList2.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope =
					Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList2.add(curricularCourseScope);
			}
		}

		try {
			BeanUtils.copyProperties(enrolmentContext, infoEnrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCourseScopeList);
		enrolmentContext.setStudent(student);
		enrolmentContext.setActualEnrolment(curricularCourseScopeList2);

		return enrolmentContext;
	}
	
	public static InfoEnrolmentContext getInfoEnrolmentContext(EnrolmentContext enrolmentContext) {

		InfoEnrolmentContext infoEnrolmentContext = new InfoEnrolmentContext();

		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(enrolmentContext.getStudent());
		
		InfoStudentCurricularPlan infoStudentActiveCurricularPlan =
			Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());

		List infoCurricularCourseScopeList = new ArrayList();
		List curricularCourseScopeList = null;

		curricularCourseScopeList = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (curricularCourseScopeList != null && !curricularCourseScopeList.isEmpty()) {
			Iterator iterator = curricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoCurricularCourseScopeList.add(infoCurricularCourseScope);
			}
		}

		List infoCurricularCourseScopeList2 = new ArrayList();
		List curricularCourseScopeList2 = enrolmentContext.getActualEnrolment();
		if (curricularCourseScopeList2 != null && !curricularCourseScopeList2.isEmpty()) {
			Iterator iterator = curricularCourseScopeList2.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoCurricularCourseScopeList2.add(infoCurricularCourseScope);
			}
		}

		try {
			BeanUtils.copyProperties(infoEnrolmentContext, enrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoEnrolmentContext.setInfoStudentActiveCurricularPlan(infoStudentActiveCurricularPlan);
		infoEnrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(infoCurricularCourseScopeList);
		infoEnrolmentContext.setInfoStudent(infoStudent);
		infoEnrolmentContext.setActualEnrolment(infoCurricularCourseScopeList2);

		return infoEnrolmentContext;
	}
}
