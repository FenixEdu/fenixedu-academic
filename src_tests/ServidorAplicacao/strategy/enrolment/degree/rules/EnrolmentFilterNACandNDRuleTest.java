/*
 * Created on 6/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class EnrolmentFilterNACandNDRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentFilterNACandNDRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testApplyEnrolmentFilterNACandNDRule() {
		List finalSpan = new ArrayList();
		List initialSpan = null;

		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
		initialSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();

//		doApplyRule(new EnrolmentMaximumNumberOfAcumulatedEnrollmentsAndMaximumNumberOfCoursesToEnrollFilterRule(), enrolmentContext);

		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();

		assertEquals("Inital span Size:", initialSpan.size(), 60);
		assertEquals("Final span Size:", finalSpan.size(), 12);
		assertEquals("Contains assertion!", true, initialSpan.containsAll(finalSpan));

		ICurricularCourse curricularCourse = getCurricularCourse("FÍSICA II", "");
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, finalSpan.contains(curricularCourseScope));

		curricularCourse = getCurricularCourse("TRABALHO FINAL DE CURSO I", "");
		curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, !finalSpan.contains(curricularCourseScope));

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
//		enrolmentRule.apply(enrolmentContext);
	}
}