package ServidorAplicacao.strategy.enrolment.strategys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IPossibleCurricularCourseForOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPossibleCurricularCourseForOptionalCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public abstract class EnrolmentStrategy implements IEnrolmentStrategy {

	private EnrolmentContext enrolmentContext = null;

	public EnrolmentContext getEnrolmentContext() {
		return enrolmentContext;
	}

	public void setEnrolmentContext(EnrolmentContext enrolmentContext) {
		this.enrolmentContext = enrolmentContext;
	}

	protected EnrolmentContext filterByExecutionCourses(EnrolmentContext enrolmentContext) {

		final IExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
		
		List curricularCoursesToRemove = new ArrayList();
		// FIXME [DAVID]: Não se pode filtar as opções porque elas não têm EXECUTION_COURSE associados
		Iterator spanIterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (spanIterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) spanIterator.next();
			List executionCourseList = curricularCourseScope.getCurricularCourse().getAssociatedExecutionCourses(); 
				
			final List executionCourseInExecutionPeriod = (List) CollectionUtils.select(executionCourseList, new Predicate() {
				public boolean evaluate(Object obj) {
					IExecutionCourse disciplinaExecucao = (IExecutionCourse) obj;
					return disciplinaExecucao.getExecutionPeriod().equals(executionPeriod);
				}
			});
				
			if(executionCourseInExecutionPeriod.isEmpty()){
				curricularCoursesToRemove.add(curricularCourseScope);
			} else {
				executionCourseInExecutionPeriod.clear();
			}
		}

		enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(curricularCoursesToRemove);
		
		return enrolmentContext;
	}

	protected EnrolmentContext filterBySemester(EnrolmentContext enrolmentContext) {
		List curricularCoursesFromActualExecutionPeriod = new ArrayList();

		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) {
				curricularCoursesFromActualExecutionPeriod.add(curricularCourseScope);
			}
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCoursesFromActualExecutionPeriod);
		return enrolmentContext;
	}

	protected EnrolmentContext filterScopesOfCurricularCoursesToBeChosenForOptionalCurricularCourses(EnrolmentContext enrolmentContext) {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentPossibleCurricularCourseForOptionalCurricularCourse persistentPossibleCurricularCourseForOptionalCurricularCourse = sp.getIPersistentChosenCurricularCourseForOptionalCurricularCourse();

			List possibleCurricularCoursesForOptionalCurricularCourseList = persistentPossibleCurricularCourseForOptionalCurricularCourse.readAllByDegreeCurricularPlan(enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan());
			List possibleCurricularCourses = (List) CollectionUtils.collect(possibleCurricularCoursesForOptionalCurricularCourseList, new Transformer() {
				public Object transform(Object obj) {
					IPossibleCurricularCourseForOptionalCurricularCourse possibleCurricularCourseForOptionalCurricularCourse = (IPossibleCurricularCourseForOptionalCurricularCourse) obj;
					return possibleCurricularCourseForOptionalCurricularCourse.getPossibleCurricularCourse();
				}
			});

			List curricularCourseScopesToRemove = new ArrayList();
			Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
			while(iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				if(possibleCurricularCourses != null) {
					if(possibleCurricularCourses.contains(curricularCourseScope.getCurricularCourse())) {
						curricularCourseScopesToRemove.add(curricularCourseScope);
					}
				}
			}

			List finalSpan = (List) CollectionUtils.subtract(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled(), curricularCourseScopesToRemove);
			enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(finalSpan);
		} catch (ExcepcaoPersistencia e) {
		}

		return enrolmentContext;
	}

	public abstract EnrolmentContext getAvailableCurricularCourses();

	public abstract EnrolmentContext validateEnrolment();

	public abstract EnrolmentContext getOptionalCurricularCourses();

	public abstract EnrolmentContext getDegreesForOptionalCurricularCourses();
}