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

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoEquivalence;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IEquivalence;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionPeriod;
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
		
		IPersistentExecutionPeriod persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();

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

		List studentCurricularPlanCurricularCourses = computeStudentCurricularPlanCurricularCourses(degreeCurricularPlanCurricularCourses, studentActiveCurricularPlan);
		
		IExecutionPeriod actualExecutionPeriod = persistentExecutionPeriod.readActualExecutionPeriod(); 

		enrolmentContext.setCurricularCoursesFromStudentCurricularPlan(studentCurricularPlanCurricularCourses);
		enrolmentContext.setStudent(student);
		enrolmentContext.setSemester(semester);
		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(computeCurricularCoursesScopesNotYetDoneByStudent(degreeCurricularPlanCurricularCourses, studentEnroledAndDoneCurricularCourses));
		enrolmentContext.setCurricularCoursesDoneByStudent(studentDoneCurricularCourses);
		enrolmentContext.setAcumulatedEnrolments(CollectionUtils.getCardinalityMap(curricularCoursesEnrolled));
		enrolmentContext.setStudentActiveCurricularPlan(studentActiveCurricularPlan);
		enrolmentContext.setEnrolmentValidationResult(new EnrolmentValidationResult());
		enrolmentContext.setCurricularCoursesScopesEnroledByStudent(studentEnroledCurricularCourseScopes);
		enrolmentContext.setExecutionPeriod(actualExecutionPeriod);

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

		IStudentCurricularPlan studentActiveCurricularPlan = Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());

		// Transform final span of info curricular course scopes:
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

		// Transform list of info curricular course scopes being enroled:
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

		// Transform list of info curricular course scopes actually enroled:
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

		// Transform list of info degrees to choose from, for optional courses selection:
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

		// Transform list of optional info curricular courses to choose from:
		List infoOptionalCurricularCourseList = infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree();
		List optionalCurricularCourseList = new ArrayList();
		if (infoOptionalCurricularCourseList != null && !infoOptionalCurricularCourseList.isEmpty()) {
			Iterator iterator = infoOptionalCurricularCourseList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
				ICurricularCourse curricularCourse = Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
				optionalCurricularCourseList.add(curricularCourse);
			}
		}

		// Transform list of optional info curricular courses equivalences:
		List infoOptionalCurricularCoursesEquivalencesList = infoEnrolmentContext.getInfoOptionalCurricularCoursesEquivalences();
		List optionalCurricularCoursesEquivalencesList = new ArrayList();
		if (infoOptionalCurricularCoursesEquivalencesList != null && !infoOptionalCurricularCoursesEquivalencesList.isEmpty()) {
			Iterator iterator = infoOptionalCurricularCoursesEquivalencesList.iterator();
			while (iterator.hasNext()) {
				InfoEquivalence infoEquivalence = (InfoEquivalence) iterator.next();
				IEquivalence equivalence = Cloner.copyInfoEquivalence2IEquivalence(infoEquivalence);
				optionalCurricularCoursesEquivalencesList.add(equivalence);
			}
		}

		ICurso degree = getDegree(infoEnrolmentContext.getChosenOptionalInfoDegree());
		
		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());
		
		ICurricularCourseScope chosenCurricularCourseScope = null;
		if(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope() != null){
			chosenCurricularCourseScope = Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope());
		}

		// Transform list of info curricular courses done by the student:
		List infoCurricularCoursesDoneByStudentList = infoEnrolmentContext.getInfoCurricularCoursesDoneByStudent();
		List curricularCoursesDoneByStudentList = new ArrayList();
		if (infoCurricularCoursesDoneByStudentList != null && !infoCurricularCoursesDoneByStudentList.isEmpty()) {
			Iterator iterator = infoCurricularCoursesDoneByStudentList.iterator();
			while (iterator.hasNext()) {
				InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) iterator.next();
				ICurricularCourse curricularCourse = Cloner.copyInfoCurricularCourse2CurricularCourse(infoCurricularCourse);
				curricularCoursesDoneByStudentList.add(curricularCourse);
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
		enrolmentContext.setCurricularCoursesScopesEnroledByStudent(enroledCurricularCourseScopeList);
		enrolmentContext.setChosenOptionalDegree(degree);
		enrolmentContext.setDegreesForOptionalCurricularCourses(optionalDegreeList);
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(optionalCurricularCourseList);
		enrolmentContext.setCurricularCoursesDoneByStudent(curricularCoursesDoneByStudentList);
		enrolmentContext.setOptionalCurricularCoursesEquivalences(optionalCurricularCoursesEquivalencesList);
		enrolmentContext.setChosenOptionalCurricularCourseScope(chosenCurricularCourseScope);
		enrolmentContext.setExecutionPeriod(executionPeriod);
		
		return enrolmentContext;
	}

	public static InfoEnrolmentContext getInfoEnrolmentContext(EnrolmentContext enrolmentContext) {

		InfoEnrolmentContext infoEnrolmentContext = new InfoEnrolmentContext();

		InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(enrolmentContext.getStudent());

		InfoStudentCurricularPlan infoStudentActiveCurricularPlan = Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan());

		// Transform final span of curricular course scopes to respective info:
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

		// Transform list of curricular course scopes being enroled to respective info:
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

		// Transform list of curricular course scopes actually enroled to respective info:
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

		// Transform list of degrees to choose from, for optional courses selection to respective info:
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

		// Transform list of optional curricular courses to choose from to respective info:
		List optionalCurricularCourseList = enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
		List infoOptionalCurricularCourseList = new ArrayList();
		if (optionalCurricularCourseList != null && !optionalCurricularCourseList.isEmpty()) {
			Iterator iterator = optionalCurricularCourseList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				InfoCurricularCourse infoCurricularCourse =	Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoOptionalCurricularCourseList.add(infoCurricularCourse);
			}
		}

		// Transform list of optional curricular courses equivalences to respective info:
		List optionalCurricularCourseEquivalencesList = enrolmentContext.getOptionalCurricularCoursesEquivalences();
		List infoOptionalCurricularCoursesEquivalencesList = new ArrayList();
		if (optionalCurricularCourseEquivalencesList != null && !optionalCurricularCourseEquivalencesList.isEmpty()) {
			Iterator iterator = optionalCurricularCourseEquivalencesList.iterator();
			while (iterator.hasNext()) {
				IEquivalence equivalence = (IEquivalence) iterator.next();
				InfoEquivalence infoEquivalence =	Cloner.copyIEquivalence2InfoEquivalence(equivalence);
				infoOptionalCurricularCoursesEquivalencesList.add(infoEquivalence);
			}
		}

		InfoDegree infoDegree = getInfoDegree(enrolmentContext.getChosenOptionalDegree());
		
		InfoExecutionPeriod infoExecutionPeriod = Cloner.copyIExecutionPeriod2InfoExecutionPeriod(enrolmentContext.getExecutionPeriod());
		
		InfoCurricularCourseScope infoChosenCurricularCourseScope = null;
		if(enrolmentContext.getChosenOptionalCurricularCourseScope() != null){
			infoChosenCurricularCourseScope = Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(enrolmentContext.getChosenOptionalCurricularCourseScope());
		}

		// Transform list of info curricular courses done by the student:
		List curricularCoursesDoneByStudentList = enrolmentContext.getCurricularCoursesDoneByStudent();
		List infoCurricularCoursesDoneByStudentList = new ArrayList();
		if (curricularCoursesDoneByStudentList != null && !curricularCoursesDoneByStudentList.isEmpty()) {
			Iterator iterator = curricularCoursesDoneByStudentList.iterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
				InfoCurricularCourse infoCurricularCourse =	Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCoursesDoneByStudentList.add(infoCurricularCourse);
			}
		}

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
		infoEnrolmentContext.setOptionalInfoCurricularCoursesToChooseFromDegree(infoOptionalCurricularCourseList);
		infoEnrolmentContext.setInfoCurricularCoursesDoneByStudent(infoCurricularCoursesDoneByStudentList);
		infoEnrolmentContext.setInfoOptionalCurricularCoursesEquivalences(infoOptionalCurricularCoursesEquivalencesList);
		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoChosenCurricularCourseScope);
		infoEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		
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
