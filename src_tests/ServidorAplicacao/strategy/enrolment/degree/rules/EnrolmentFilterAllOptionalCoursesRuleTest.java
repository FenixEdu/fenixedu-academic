package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorAplicacao.strategy.enrolment.rules.EnrolmentFilterAllOptionalCoursesRule;
import ServidorAplicacao.strategy.enrolment.rules.IEnrolmentRule;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class EnrolmentFilterAllOptionalCoursesRuleTest extends BaseEnrolmentRuleTestCase {

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(EnrolmentFilterAllOptionalCoursesRuleTest.class);
		return suite;
	}

	protected String getDataSetFilePath() {
		return "etc/testEnrolmentDataSet.xml";
	}
	
	public void testApplyFilterAllOptionalCoursesRule() {

		List optionalSpan = null;
		InfoEnrolmentContext infoEnrolmentContext = null;
		EnrolmentContext enrolmentContext = null;
		
		autentication();
		Object serviceArgs1[] = { userView };
		enrolmentContext = executeService("ShowAvailableCurricularCourses", serviceArgs1);

		infoEnrolmentContext = EnrolmentContextManager.getInfoEnrolmentContext(enrolmentContext);
		Object serviceArgs2[] = { infoEnrolmentContext };
		enrolmentContext = executeService("ShowAvailableDegreesForOption", serviceArgs2);
		
		enrolmentContext.setChosenOptionalDegree((ICurso) enrolmentContext.getDegreesForOptionalCurricularCourses().get(0));

//		doApplyRule(new EnrolmentFilterAllOptionalCoursesRule(), enrolmentContext);

		optionalSpan = enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();

		assertEquals("Optional Span size:", 13, optionalSpan.size());

		ICurricularCourse curricularCourse = getCurricularCourse("INTERFACES PESSOA-MÁQUINA", "");
		assertEquals(true, optionalSpan.contains(curricularCourse));

		curricularCourse = getCurricularCourse("TRABALHO FINAL DE CURSO I", "");
		assertEquals(true, !optionalSpan.contains(curricularCourse));

	}

	public void doApplyRule(IEnrolmentRule enrolmentRule, EnrolmentContext enrolmentContext) {
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
//			enrolmentRule.apply(enrolmentContext);
			sp.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
		}
	}

}