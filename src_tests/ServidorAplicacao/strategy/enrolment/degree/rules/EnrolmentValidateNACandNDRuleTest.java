package ServidorAplicacao.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public class EnrolmentValidateNACandNDRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentValidateNACandNDRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testApplyEnrolmentValidateNACandNDRule() {

		autentication();
		Object serviceArgs[] = { userView };
		EnrolmentContext enrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs);

		// tudo ok
		enrolmentContext.getActualEnrolments().clear();
		for (int i = 0; i < enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().size(); i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}

//		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), true);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// menos que 3
		enrolmentContext.getActualEnrolments().clear();
		for (int i = 0; i < 2; i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}

//		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// mais que 7
		enrolmentContext.getActualEnrolments().clear();
		for (int i = 0; i < enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().size(); i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}
		for (int i = 0; i < enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().size(); i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}

//		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// mais que 10 acumuladas
		enrolmentContext.getActualEnrolments().clear();
		for (int i = 0; i < enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().size(); i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}
		for (int i = 0; i < enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().size(); i++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolments().add(curricularCourseScope);
		}

//		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);
	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
//		enrolmentRule.apply(enrolmentContext);
	}
}