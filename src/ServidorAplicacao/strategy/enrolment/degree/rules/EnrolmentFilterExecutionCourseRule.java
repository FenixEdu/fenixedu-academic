package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentFilterExecutionCourseRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		final IExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
		
		List curricularCoursesToRemove = new ArrayList();
		
		Iterator spanIterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (spanIterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) spanIterator.next();
			List executionCourseList = curricularCourseScope.getCurricularCourse().getAssociatedExecutionCourses(); 
				
			final List executionCourseInExecutionPeriod = (List) CollectionUtils.select(executionCourseList, new Predicate() {
				public boolean evaluate(Object obj) {
					IDisciplinaExecucao disciplinaExecucao = (IDisciplinaExecucao) obj;
					return disciplinaExecucao.getExecutionPeriod().equals(executionPeriod);
				}
			});
				
			if(executionCourseInExecutionPeriod.isEmpty()){
				curricularCoursesToRemove.add(curricularCourseScope);
			}else{
				executionCourseInExecutionPeriod.clear();
			}
		}

		enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(curricularCoursesToRemove);
		
		return enrolmentContext;
	}
}