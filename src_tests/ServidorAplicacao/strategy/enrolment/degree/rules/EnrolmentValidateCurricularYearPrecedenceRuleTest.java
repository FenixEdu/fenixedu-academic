package ServidorAplicacao.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentValidateCurricularYearPrecedenceRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public class EnrolmentValidateCurricularYearPrecedenceRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentValidateCurricularYearPrecedenceRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}

	public void testApplyEnrolmentValidateCurricularYearPrecedenceRule() {

		autentication();
		Object serviceArgs[] = { userView };
		EnrolmentContext enrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs);

		// tudo ok
		enrolmentContext.getActualEnrolments().clear();
		for (int i = 0; i < enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().size(); i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}

		doApplyRule(new EnrolmentValidateCurricularYearPrecedenceRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), true);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// falha
		enrolmentContext.getActualEnrolments().clear();
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(4);
		enrolmentContext.getActualEnrolments().add(curricularCourseScope);

		doApplyRule(new EnrolmentValidateCurricularYearPrecedenceRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
		enrolmentRule.apply(enrolmentContext);
	}
}