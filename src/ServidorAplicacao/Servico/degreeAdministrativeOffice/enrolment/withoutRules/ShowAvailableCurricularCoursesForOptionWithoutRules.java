package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalCoursesRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ShowAvailableCurricularCoursesForOptionWithoutRules implements IServico {

	private static ShowAvailableCurricularCoursesForOptionWithoutRules _servico = new ShowAvailableCurricularCoursesForOptionWithoutRules();

	public static ShowAvailableCurricularCoursesForOptionWithoutRules getService() {
		return _servico;
	}

	private ShowAvailableCurricularCoursesForOptionWithoutRules() {
	}

	public final String getNome() {
		return "ShowAvailableCurricularCoursesForOptionWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException {
		IEnrolmentRule enrolmentRule = new EnrolmentFilterAllOptionalCoursesRule();
		EnrolmentContext enrolmentContext = enrolmentRule.apply(EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext));
		InfoEnrolmentContext infoEnrolmentContext2 = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);

//		IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoEnrolmentContext2.getInfoExecutionPeriod());
//		infoEnrolmentContext2.setOptionalInfoCurricularCoursesToChooseFromDegree(this.filterByExecutionCourses(infoEnrolmentContext2.getOptionalInfoCurricularCoursesToChooseFromDegree(), executionPeriod));

		return infoEnrolmentContext2;
	}

	// FIXME DAVID-RICARDO: Eventualmente esta filtragem deverá ser feita dentro da própria regra.
	private List filterByExecutionCourses(List possibleCurricularCoursesScopesToChoose, IExecutionPeriod executionPeriod) {

		List curricularCoursesToRemove = new ArrayList();
		final IExecutionPeriod executionPeriod2 = executionPeriod;
		
		Iterator iterator = possibleCurricularCoursesScopesToChoose.iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			List executionCourseList = curricularCourseScope.getCurricularCourse().getAssociatedExecutionCourses(); 
				
			List executionCourseInExecutionPeriod = (List) CollectionUtils.select(executionCourseList, new Predicate() {
				public boolean evaluate(Object obj) {
					IDisciplinaExecucao disciplinaExecucao = (IDisciplinaExecucao) obj;
					return disciplinaExecucao.getExecutionPeriod().equals(executionPeriod2);
				}
			});
				
			if(executionCourseInExecutionPeriod.isEmpty()){
				curricularCoursesToRemove.add(curricularCourseScope);
			} else {
				executionCourseInExecutionPeriod.clear();
			}
		}

		possibleCurricularCoursesScopesToChoose.removeAll(curricularCoursesToRemove);
		
		return possibleCurricularCoursesScopesToChoose;
	}
}