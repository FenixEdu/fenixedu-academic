package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterAutomaticEnrolmentRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterCurricularYearPrecedence;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterLEQTrainingCourseRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterPrecedenceNotDoneRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterPrecedenceSpanRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterRestrictedOptionalCoursesRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterRestrictedOptionalDegreeRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterSemesterRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateCurricularYearPrecedenceRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateLEQLaboratoryRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyLEQ implements IEnrolmentStrategy {

	private EnrolmentContext enrolmentContext = null;

	public EnrolmentStrategyLEQ() {
	}

	public EnrolmentContext getAvailableCurricularCourses() {
		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterSemesterRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterPrecedenceSpanRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterCurricularYearPrecedence();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterAutomaticEnrolmentRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);		

		enrolmentRule = new EnrolmentFilterLEQTrainingCourseRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);		

		//	NOTE: David-Ricardo: Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
		enrolmentRule = new EnrolmentFilterNACandNDRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		return this.enrolmentContext;
	}

	/**
	 * @return EnrolmentContext
	 */
	public EnrolmentContext getEnrolmentContext() {
		return enrolmentContext;
	}

	/**
	 * Sets the enrolmentContext.
	 * @param enrolmentContext The enrolmentContext to set
	 */
	public void setEnrolmentContext(EnrolmentContext enrolmentContext) {
		this.enrolmentContext = enrolmentContext;
	}

	public EnrolmentContext validateEnrolment() {
		IEnrolmentRule validateRule = null;

		validateRule = new EnrolmentValidateNACandNDRule();
		this.enrolmentContext = validateRule.apply(this.enrolmentContext);

		validateRule = new EnrolmentValidateCurricularYearPrecedenceRule();
		this.enrolmentContext = validateRule.apply(this.enrolmentContext);

		validateRule = new EnrolmentValidateLEQLaboratoryRule();
		this.enrolmentContext = validateRule.apply(this.enrolmentContext);

		return this.enrolmentContext;
	}

	public EnrolmentContext getOptionalCurricularCourses() {
		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterRestrictedOptionalCoursesRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterPrecedenceNotDoneRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);		

		return this.enrolmentContext;
	}

	public EnrolmentContext getDegreesForOptionalCurricularCourses() {

		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterRestrictedOptionalDegreeRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		return this.enrolmentContext;
	}
}