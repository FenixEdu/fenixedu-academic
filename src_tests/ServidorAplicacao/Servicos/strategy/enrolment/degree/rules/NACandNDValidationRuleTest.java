package ServidorAplicacao.Servicos.strategy.enrolment.degree.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterBranchRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentFilterSemesterRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentValidateNACandNDRule;
import ServidorAplicacao.strategy.enrolment.degree.rules.IEnrolmentRule;
import Tools.dbaccess;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 15/Abr/2003
 */
public class NACandNDValidationRuleTest extends BaseEnrolmentRuleTest {

	private dbaccess dbAcessPoint;

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(NACandNDValidationRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentServicesLERCIDataSet.xml";
	}

	public void testApplyNACandNDValidationRule() {

		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA), new Integer(1));

		doApplyRule(new EnrolmentFilterBranchRule(), enrolmentContext);
		doApplyRule(new EnrolmentFilterSemesterRule(), enrolmentContext);
		doApplyRule(new EnrolmentFilterNACandNDRule(), enrolmentContext);

		// tudo ok
		enrolmentContext.getActualEnrolment().clear();
		for (int i = 0; i < 5; i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolment().add(curricularCourseScope);
		}
		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), true);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// menos que 3
		enrolmentContext.getActualEnrolment().clear();
		for (int i = 0; i < 2; i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolment().add(curricularCourseScope);
		}
		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// mais que 7
		enrolmentContext.getActualEnrolment().clear();
		for (int i = 0; i < 9; i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolment().add(curricularCourseScope);
		}
		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);

		// mais que 10 acumuladas
		enrolmentContext.getActualEnrolment().clear();
		for (int i = 0; i < 7; i++) {
			ICurricularCourseScope curricularCourseScope =
				(ICurricularCourseScope) enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(i);
			enrolmentContext.getActualEnrolment().add(curricularCourseScope);
		}
		doApplyRule(new EnrolmentValidateNACandNDRule(), enrolmentContext);
		assertEquals(enrolmentContext.getEnrolmentValidationResult().isSucess(), false);
		enrolmentContext.getEnrolmentValidationResult().setSucess(true);
	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
		enrolmentRule.apply(enrolmentContext);
	}
}