package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentRuleBranch;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentRuleNACandND;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentRuleSemester;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentStrategyLERCI implements IEnrolmentStrategy {

	private EnrolmentContext enrolmentContext = null;

	public EnrolmentStrategyLERCI() {
	}

	public EnrolmentContext getAvailableCurricularCourses() throws ExcepcaoPersistencia {
		IEnrolmentRule enrolmentRule = null;

		enrolmentRule = new EnrolmentRuleBranch();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);
		
		
		enrolmentRule = new EnrolmentRuleSemester();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);
		

		enrolmentRule = new EnrolmentRuleNACandND();
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

}