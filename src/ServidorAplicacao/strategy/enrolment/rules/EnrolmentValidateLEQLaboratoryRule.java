package ServidorAplicacao.strategy.enrolment.rules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to validate if the student
 * has chosen only one laboratory to be enrolled in, if this is a student
 * from the LEQ degree.
 */

public class EnrolmentValidateLEQLaboratoryRule //implements IEnrolmentRule
{

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List labsEnroled = (List) CollectionUtils.select(enrolmentContext.getActualEnrolments(), new Predicate() {
			public boolean evaluate(Object obj) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
				return curricularCourseScope.getCurricularCourse().getType().equals(CurricularCourseType.LABORATORY_COURSE_OBJ);
			}
		});

		if (labsEnroled.size() > 1){
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.LEQ_UNIQUE_LABORATORY);
		}
						
		return enrolmentContext;
	}
}