package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterAllOptionalCoursesRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterAllOptionalDegreesRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterAnualCurricularCourseRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterAutomaticEnrolmentRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterBranchRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterFinalistRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterSemesterRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateCurricularYearPrecedenceRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyLERCI implements IEnrolmentStrategy {

	private EnrolmentContext enrolmentContext = null;

	public EnrolmentStrategyLERCI() {
	}

	public EnrolmentContext getAvailableCurricularCourses() {
		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterBranchRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterFinalistRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterAnualCurricularCourseRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterAutomaticEnrolmentRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		enrolmentRule = new EnrolmentFilterSemesterRule();
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

//		if (this.enrolmentContext.getEnrolmentValidationResult().isSucess()) {
//			try {
//				EnrolmentTemporarilyEnrol.apply(this.enrolmentContext);
//				//				FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
//				this.enrolmentContext.getEnrolmentValidationResult().setSucessMessage("Inscrição realizada com sucesso");
//				return this.enrolmentContext;
//			} catch (ExcepcaoPersistencia e) {
//				e.printStackTrace();
//				//				FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
//				this.enrolmentContext.getEnrolmentValidationResult().setSucessMessage("Erro no acesso à base de dados");
//				return this.enrolmentContext;
//			}
//
//		}
		return this.enrolmentContext;
	}

	public EnrolmentContext getOptionalCurricularCourses() {
		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterAllOptionalCoursesRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		return this.enrolmentContext;
	}

	public EnrolmentContext getDegreesForOptionalCurricularCourses() {

		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentFilterAllOptionalDegreesRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);

		return this.enrolmentContext;
	}
}