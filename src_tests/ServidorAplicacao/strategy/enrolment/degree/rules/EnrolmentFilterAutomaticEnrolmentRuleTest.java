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
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAutomaticEnrolmentRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class EnrolmentFilterAutomaticEnrolmentRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentFilterAutomaticEnrolmentRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testApplyEnrolmentFilterAutomaticEnrolmentRule() {
		List finalSpan = null;
		List initialSpan = new ArrayList();
		List automaticSpan = null;

		EnrolmentContext enrolmentContext = getEnrolmentContext(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
		initialSpan.addAll(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled());

//		doApplyRule(new EnrolmentFilterAutomaticEnrolmentRule(), enrolmentContext);

		finalSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		automaticSpan = enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled();
		
		assertEquals("Inital span Size:", initialSpan.size(), 60);
		assertEquals("Final span size:", finalSpan.size(),58);
		assertEquals("Contains assertion!", true, initialSpan.containsAll(finalSpan));
		assertEquals("Automatic span size:", automaticSpan.size(),2);
			
		ICurricularCourse curricularCourse = getCurricularCourse("SISTEMAS OPERATIVOS", "");
		ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, finalSpan.contains(curricularCourseScope));
		assertEquals(true, !automaticSpan.contains(curricularCourseScope));
		
		curricularCourse = getCurricularCourse("ANÁLISE MATEMÁTICA III", "");
		curricularCourseScope = (ICurricularCourseScope) curricularCourse.getScopes().get(0);
		assertEquals(true, !finalSpan.contains(curricularCourseScope));
		assertEquals(true, automaticSpan.contains(curricularCourseScope));

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
//			enrolmentRule.apply(enrolmentContext);
	}
}
