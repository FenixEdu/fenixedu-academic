package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterAllOptionalCoursesRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List degreeCurricularPlanList = enrolmentContext.getChosenOptionalDegree().getDegreeCurricularPlans();

		List activeDegreeCurricularPlanList = (List) CollectionUtils.select(degreeCurricularPlanList, new Predicate() {
			public boolean evaluate(Object obj) {
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) obj;
				return degreeCurricularPlan.getState().equals(new DegreeCurricularPlanState(DegreeCurricularPlanState.ACTIVE));
			}
		});

		List finalCurricularCourseScopesList = new ArrayList();
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
					throw new IllegalStateException("Cannot read from data base");
				}
			}

			List curricularCourseScopesFromDegreeList = new ArrayList();
			Iterator iterator2 = curricularCoursesFromDegreeList.iterator();
			while (iterator2.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator2.next();
				curricularCourseScopesFromDegreeList.addAll(curricularCourse.getScopes());
			}

			Iterator iterator3 = curricularCourseScopesFromDegreeList.iterator();
			while (iterator3.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator3.next();
				if( (curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) &&
						(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() > 2) ) {
							finalCurricularCourseScopesList.add(curricularCourseScope);
				}
			}
		}
		
		enrolmentContext.setOptionalCurricularCoursesToChooseFromDegree(finalCurricularCourseScopesList);
		return enrolmentContext;
	}
}