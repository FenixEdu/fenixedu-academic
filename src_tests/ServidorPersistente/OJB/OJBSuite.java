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
        //--JUNIT:
        //This block was automatically generated and can be regenerated again.
        //Do NOT change lines enclosed by the --JUNIT: and :JUNIT-- tags.
        
        TestSuite suite = new TestSuite("OJBSuite");
        //suite.addTest(ServidorPersistente.OJB.ItemOJBTest.suite());

		suite.addTest(ServidorPersistente.OJB.AulaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.CandidateSituationOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.CountryOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.CurricularCourseOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.CursoExecucaoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.CursoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.DepartamentoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.DisciplinaDepartamentoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.DisciplinaExecucaoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.EnrolmentOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.ExecutionPeriodOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.ExecutionYearOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.FrequentaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.ItemOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.MasterDegreeCandidateOJBTest.suite());

		suite.addTest(ServidorPersistente.OJB.PessoaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.PlanoCurricularCursoOJBTest.suite());
		
		suite.addTest(ServidorPersistente.OJB.SalaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.StudentCurricularPlanOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.StudentOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.TurmaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.TurmaTurnoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.TurnoAlunoOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.TurnoAulaOJBTest.suite());
		suite.addTest(ServidorPersistente.OJB.TurnoOJBTest.suite());

        //:JUNIT--
        //This value MUST ALWAYS be returned from this function.
        return suite;
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    
}
