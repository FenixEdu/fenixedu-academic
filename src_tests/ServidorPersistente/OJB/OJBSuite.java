/*
 * OJBSuite.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ars
 */
public class OJBSuite extends TestCase {

	public OJBSuite(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("OJBSuite");

		suite.addTest(AnnouncementOJBTest.suite());
		suite.addTest(AulaOJBTest.suite());
//		suite.addTest(BaseEnrolmentRestrictionOJBTest.suite());

		suite.addTest(BibliographicReferenceOJBTest.suite());
		suite.addTest(BranchOJBTest.suite());
		suite.addTest(CandidateSituationOJBTest.suite());
		suite.addTest(ContributorOJBTest.suite());
		suite.addTest(CountryOJBTest.suite());
		suite.addTest(CurricularCourseOJBTest.suite());
		suite.addTest(CurricularCourseScopeOJBTest.suite());
		suite.addTest(CurricularSemesterOJBTest.suite());
		suite.addTest(CurricularYearOJBTest.suite());
		suite.addTest(CurriculumOJBTest.suite());
		suite.addTest(CursoExecucaoOJBTest.suite());
		suite.addTest(CursoOJBTest.suite());
		suite.addTest(DegreeCurricularPlanOJBTest.suite());
		suite.addTest(DepartamentoOJBTest.suite());
		suite.addTest(DisciplinaDepartamentoOJBTest.suite());
		suite.addTest(DisciplinaExecucaoOJBTest.suite());
		suite.addTest(EnrolmentOJBTest.suite());
		suite.addTest(ExamExecutionCourseOJBTest.suite());
		suite.addTest(ExamOJBTest.suite());
		suite.addTest(ExecutionPeriodOJBTest.suite());
		suite.addTest(ExecutionYearOJBTest.suite());
		suite.addTest(FrequentaOJBTest.suite());
		suite.addTest(GuideEntryOJBTest.suite());
		suite.addTest(GuideOJBTest.suite());
		suite.addTest(GuideSituationOJBTest.suite());
		suite.addTest(ItemOJBTest.suite());
		suite.addTest(MasterDegreeCandidateOJBTest.suite());
		suite.addTest(PersonRoleOJBTest.suite());
		suite.addTest(PessoaOJBTest.suite());
//		suite.addTest(PrecedenceOJBTest.suite());
		suite.addTest(PriceOJBTest.suite());
		suite.addTest(ProfessorshipOJBTest.suite());
		suite.addTest(ResponsibleForOJBTest.suite());
//		suite.addTest(RestrictionOJBTest.suite());
		suite.addTest(RoleOJBTest.suite());
		suite.addTest(SalaOJBTest.suite());
		suite.addTest(SectionOJBTest.suite());
		suite.addTest(SiteOJBTest.suite());
		suite.addTest(StudentCurricularPlanOJBTest.suite());
		suite.addTest(StudentOJBTest.suite());
		suite.addTest(TeacherOJBTest.suite());
		suite.addTest(TurmaOJBTest.suite());
		suite.addTest(TurmaTurnoOJBTest.suite());
		suite.addTest(TurnoAlunoOJBTest.suite());
		suite.addTest(TurnoAulaOJBTest.suite());
		suite.addTest(TurnoOJBTest.suite());

		return suite;
	}

	// Add test methods here, they have to start with 'test' name.
	// for example:
	// public void testHello() {}

}
