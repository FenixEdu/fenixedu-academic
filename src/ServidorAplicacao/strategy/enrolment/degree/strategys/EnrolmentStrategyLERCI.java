package ServidorAplicacao.strategy.enrolment.degree.strategys;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterBranchRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterSemesterRule;
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

		enrolmentRule = new EnrolmentFilterBranchRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);
		
		enrolmentRule = new EnrolmentFilterSemesterRule();
		this.enrolmentContext = enrolmentRule.apply(this.enrolmentContext);
		
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

	/* (non-Javadoc)
	 * @see ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy#validateEnrolment()
	 */
	public EnrolmentContext validateEnrolment() throws ExcepcaoPersistencia {
		// TODO Auto-generated method stub
		return null;
	}

}