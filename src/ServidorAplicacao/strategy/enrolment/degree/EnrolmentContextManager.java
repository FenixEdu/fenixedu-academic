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
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
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

		final IStudentCurricularPlan studentActiveCurricularPlan =
			persistentStudentCurricularPlan.readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

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

		final List studentEnrolmentsWithStateEnroled = (List) CollectionUtils.select(studentEnrolments, new Predicate() {
			public boolean evaluate(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getState().equals(new EnrolmentState(EnrolmentState.ENROLED));
			}
		});

		final List studentEnroledCurricularCourses = (List) CollectionUtils.collect(studentEnrolmentsWithStateEnroled, new Transformer() {
			public Object transform(Object obj) {
				IEnrolment enrolment = (IEnrolment) obj;
				return enrolment.getCurricularCourse();
			}
		});

		List studentEnroledAndDoneCurricularCourses = new ArrayList();
		studentEnroledAndDoneCurricularCourses.addAll(studentDoneCurricularCourses);
		studentEnroledAndDoneCurricularCourses.addAll(studentEnroledCurricularCourses);

		List studentEnroledCurricularCourseScopes = computeScopesOfCurricularCourses(studentEnroledCurricularCourses);

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

		List studentCurricularPlanCurricularCourses =
			computeStudentCurricularPlanCurricularCourses(degreeCurricularPlanCurricularCourses, studentActiveCurricularPlan);

		enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(studentCurricularPlanCurricularCourses);
		enrolmentContext.setStudent(student);
		enrolmentContext.setSemester(semester);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(
			computeCurricularCoursesScopesNotYetDoneByStudent(degreeCurricularPlanCurricularCourses, studentEnroledAndDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
		enrolmentContext.setCurricularCoursesScopesEnroledByStudent(studentEnroledCurricularCourseScopes);

		return enrolmentContext;
	}

	public static List computeStudentCurricularPlanCurricularCourses(
		List degreeCurricularPlanCurricularCourses,
		IStudentCurricularPlan studentActiveCurricularPlan) {

		List scopesOfCurricularCourses = computeScopesOfCurricularCourses(degreeCurricularPlanCurricularCourses);
		Iterator iteratorScopesOfCurricularCourses = scopesOfCurricularCourses.iterator();
		List aux = new ArrayList();
		while (iteratorScopesOfCurricularCourses.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorScopesOfCurricularCourses.next();
			if ((curricularCourseScope.getBranch().equals(studentActiveCurricularPlan.getBranch()))
				|| (curricularCourseScope.getBranch().getName().equals(""))) {
				if (!aux.contains(curricularCourseScope.getCurricularCourse())) {
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

		infoCurricularCourseScopeList = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (infoCurricularCourseScopeList != null && !infoCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList.add(curricularCourseScope);
			}
		}

		List curricularCourseScopeList2 = new ArrayList();
		List infoCurricularCourseScopeList2 = infoEnrolmentContext.getActualEnrolment();
		if (infoCurricularCourseScopeList2 != null && !infoCurricularCourseScopeList2.isEmpty()) {
			Iterator iterator = infoCurricularCourseScopeList2.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				curricularCourseScopeList2.add(curricularCourseScope);
			}
		}

		List infoEnroledCurricularCourseScopeList = infoEnrolmentContext.getInfoCurricularCoursesScopesEnroledByStudent();
		List enroledCurricularCourseScopeList = new ArrayList();
		if (infoEnroledCurricularCourseScopeList != null && !infoEnroledCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = infoEnroledCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				enroledCurricularCourseScopeList.add(curricularCourseScope);
			}
		}

		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		List optionalDegreeList = new ArrayList();
		if (infoDegreeList != null && !infoDegreeList.isEmpty()) {
			Iterator iterator = infoDegreeList.iterator();
			while (iterator.hasNext()) {
				InfoDegree infoDegree = (InfoDegree) iterator.next();
				ICurso degree = getDegree(infoDegree);
				optionalDegreeList.add(degree);
			}
		}

		List infoOptionalCurricularCourseScopesList = infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree();
		List optionalCurricularCourseScopesList = new ArrayList();
		if (infoOptionalCurricularCourseScopesList != null && !infoOptionalCurricularCourseScopesList.isEmpty()) {
			Iterator iterator = infoOptionalCurricularCourseScopesList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) iterator.next();
				ICurricularCourseScope curricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScope);
				optionalCurricularCourseScopesList.add(curricularCourseScope);
			}
		}

		ICurso degree = getDegree(infoEnrolmentContext.getChosenOptionalInfoDegree());

		try {
			BeanUtils.copyProperties(enrolmentContext, infoEnrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCourseScopeList);
		enrolmentContext.setStudent(student);
		enrolmentContext.setActualEnrolment(curricularCourseScopeList2);
		enrolmentContext.setCurricularCoursesScopesEnroledByStudent(enroledCurricularCourseScopeList);

		enrolmentContext.setChosenOptionalDegree(degree);
		enrolmentContext.setDegreesForOptionalCurricularCourses(optionalDegreeList);
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(optionalCurricularCourseScopesList);

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

		List enroledCurricularCourseScopeList = enrolmentContext.getCurricularCoursesScopesEnroledByStudent();
		List infoEnroledCurricularCourseScopeList = new ArrayList();
		if (enroledCurricularCourseScopeList != null && !enroledCurricularCourseScopeList.isEmpty()) {
			Iterator iterator = enroledCurricularCourseScopeList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoEnroledCurricularCourseScopeList.add(infoCurricularCourseScope);
			}
		}

		List optionalDegreeList = enrolmentContext.getDegreesForOptionalCurricularCourses();
		List infoDegreeList = new ArrayList();
		if (optionalDegreeList != null && !optionalDegreeList.isEmpty()) {
			Iterator iterator = optionalDegreeList.iterator();
			while (iterator.hasNext()) {
				ICurso degree = (ICurso) iterator.next();
				InfoDegree infoDegree = getInfoDegree(degree);
				infoDegreeList.add(infoDegree);
			}
		}

		List optionalCurricularCourseScopesList = enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
		List infoOptionalCurricularCourseScopesList = new ArrayList();
		if (optionalCurricularCourseScopesList != null && !optionalCurricularCourseScopesList.isEmpty()) {
			Iterator iterator = optionalCurricularCourseScopesList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				InfoCurricularCourseScope infoCurricularCourseScope =
					Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
				infoOptionalCurricularCourseScopesList.add(infoCurricularCourseScope);
			}
		}

		InfoDegree infoDegree = getInfoDegree(enrolmentContext.getChosenOptionalDegree());

		try {
			BeanUtils.copyProperties(infoEnrolmentContext, enrolmentContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoEnrolmentContext.setInfoStudentActiveCurricularPlan(infoStudentActiveCurricularPlan);
		infoEnrolmentContext.setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(infoCurricularCourseScopeList);
		infoEnrolmentContext.setInfoStudent(infoStudent);
		infoEnrolmentContext.setActualEnrolment(infoCurricularCourseScopeList2);
		infoEnrolmentContext.setInfoCurricularCoursesScopesEnroledByStudent(infoEnroledCurricularCourseScopeList);

		infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);
		infoEnrolmentContext.setInfoDegreesForOptionalCurricularCourses(infoDegreeList);
		infoEnrolmentContext.setOptionalInfoCurricularCoursesToChooseFromDegree(infoOptionalCurricularCourseScopesList);

		return infoEnrolmentContext;
	}

	private static InfoDegree getInfoDegree(ICurso degree) {
		InfoDegree infoDegree = null;
		if (degree != null) {
			infoDegree = Cloner.copyIDegree2InfoDegree(degree);
			List degreeCurricularPlanList = degree.getDegreeCurricularPlans();
			List infoDegreeCurricularPlanList = new ArrayList();
			if (degreeCurricularPlanList != null && !degreeCurricularPlanList.isEmpty()) {
				Iterator iterator = degreeCurricularPlanList.iterator();
				while (iterator.hasNext()) {
					IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
					InfoDegreeCurricularPlan infoDegreeCurricularPlan =	Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan);
					infoDegreeCurricularPlanList.add(infoDegreeCurricularPlan);
				}
			}
			infoDegree.setInfoDegreeCurricularPlans(infoDegreeCurricularPlanList);
		}
		return infoDegree;
	}

	private static ICurso getDegree(InfoDegree infoDegree) {

		ICurso degree = null;
		if (infoDegree != null) {
			degree = Cloner.copyInfoDegree2IDegree(infoDegree);
			List infoDegreeCurricularPlanList = infoDegree.getInfoDegreeCurricularPlans();
			List degreeCurricularPlanList = new ArrayList();
			if (infoDegreeCurricularPlanList != null && !infoDegreeCurricularPlanList.isEmpty()) {
				Iterator iterator = infoDegreeCurricularPlanList.iterator();
				while (iterator.hasNext()) {
					InfoDegreeCurricularPlan infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) iterator.next();
					IDegreeCurricularPlan degreeCurricularPlan = Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoDegreeCurricularPlan);
					degreeCurricularPlanList.add(degreeCurricularPlan);
				}
			}
			degree.setDegreeCurricularPlans(degreeCurricularPlanList);
		}
		return degree;
	}
}
