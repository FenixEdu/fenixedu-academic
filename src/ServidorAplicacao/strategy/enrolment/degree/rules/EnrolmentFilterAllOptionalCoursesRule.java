package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterAllOptionalCoursesRule implements IEnrolmentRule {

	// FIXME : David-Ricardo: Todas estas constantes sao para parametrizar
	private static final int MIN_YEAR_OF_OPTIONAL_COURSES = 3;
	
	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
	
		int min_year_of_optional_courses = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegreeCurricularPlanEnrolmentInfo().getMinimalYearForOptionalCourses().intValue();
		
		List degreeCurricularPlanList = enrolmentContext.getChosenOptionalDegree().getDegreeCurricularPlans();

		List activeDegreeCurricularPlanList = (List) CollectionUtils.select(degreeCurricularPlanList, new Predicate() {
			public boolean evaluate(Object obj) {
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
				return degreeCurricularPlan.getState().equals(new DegreeCurricularPlanState(DegreeCurricularPlanState.ACTIVE));
			}
		});

		List finalCurricularCourseList = new ArrayList();
		if (activeDegreeCurricularPlanList != null && !activeDegreeCurricularPlanList.isEmpty()) {
			List curricularCoursesFromDegreeList = new ArrayList();
			Iterator iterator = activeDegreeCurricularPlanList.iterator();
			while (iterator.hasNext()) {
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
				try {
					ISuportePersistente sp = SuportePersistenteOJB.getInstance();
					IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
					List curricularCoursesList = persistentCurricularCourse.readCurricularCoursesByDegreeCurricularPlan(degreeCurricularPlan);
					curricularCoursesFromDegreeList.addAll(curricularCoursesList);
				} catch (ExcepcaoPersistencia e) {
					e.printStackTrace(System.out);
					throw new IllegalStateException("Cannot read from data base" + e.toString());
				}
			}

			List curricularCourseScopesFromDegreeList = new ArrayList();
			Iterator iterator2 = curricularCoursesFromDegreeList.iterator();
			while (iterator2.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator2.next();
				curricularCourseScopesFromDegreeList.addAll(curricularCourse.getScopes());
			}

			List curricularCoursesEnroledByStudent = getDistinctCurricularCoursesOfScopes(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled());
			List curricularCoursesFromFinalSpan = getDistinctCurricularCoursesOfScopes(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());
			List opionalCurricularCoursesEnrolmentsList = (List) CollectionUtils.collect(enrolmentContext.getOptionalCurricularCoursesEnrolments(), new Transformer() {
				public Object transform(Object obj) {
					IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) obj;
					return enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();
				}
			});


			Iterator iterator3 = curricularCourseScopesFromDegreeList.iterator();
			while (iterator3.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator3.next();
				if(
						(curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) &&
//						(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() >= MIN_YEAR_OF_OPTIONAL_COURSES) &&
						(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() >= min_year_of_optional_courses) &&
						(!finalCurricularCourseList.contains(curricularCourseScope.getCurricularCourse())) &&
						(!curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE))) &&
						(!curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) &&
						(!enrolmentContext.getCurricularCoursesDoneByStudent().contains(curricularCourseScope.getCurricularCourse())) &&
						(!curricularCoursesEnroledByStudent.contains(curricularCourseScope.getCurricularCourse())) &&
						(!curricularCoursesFromFinalSpan.contains(curricularCourseScope.getCurricularCourse())) &&
						(!opionalCurricularCoursesEnrolmentsList.contains(curricularCourseScope.getCurricularCourse()))
					) {
						finalCurricularCourseList.add(curricularCourseScope.getCurricularCourse());
				}
			}
		}
		
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(finalCurricularCourseList);
		return enrolmentContext;
	}

	private List getDistinctCurricularCoursesOfScopes(List curricularCoursesScopes) {

		List curricularCoursesScopes2 = new ArrayList();
		curricularCoursesScopes2.addAll(curricularCoursesScopes);

		List finalCurricularCoursesList = new ArrayList();

		while(!curricularCoursesScopes2.isEmpty()) {
			final ICurricularCourseScope curricularCourseScope1 = (ICurricularCourseScope) curricularCoursesScopes2.get(0);
			List aux = (List) CollectionUtils.select(curricularCoursesScopes2, new Predicate() {
				public boolean evaluate(Object obj) {
					ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) obj;
					return curricularCourseScope2.getCurricularCourse().equals(curricularCourseScope1.getCurricularCourse());
				}
			});

			curricularCoursesScopes2.removeAll(aux);
			finalCurricularCoursesList.add(( (ICurricularCourseScope) aux.get(0) ).getCurricularCourse());
		}
		return finalCurricularCoursesList;
	}
}