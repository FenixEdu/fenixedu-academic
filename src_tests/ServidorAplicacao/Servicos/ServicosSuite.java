/*
 * ServicosSuite.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package net.sourceforge.fenixedu.applicationTier.Servicos;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author tfc130
 */
public class ServicosSuite extends TestCase {
    
    public ServicosSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        
        TestSuite suite = new TestSuite("ServicosSuite");
        suite.addTest(ServidorAplicacao.Servicos.sop.AdicionarAulaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.AdicionarTurnoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.ApagarSalaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.ApagarTurmaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.ApagarTurnoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.CriarSalaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.CriarTurmaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.CriarTurnoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.EditarSalaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.EditarTurmaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.EditarTurnoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerAlunosDeTurnoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerAulasDeDisciplinaExecucaoServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerAulasDeDisciplinaExecucaoETipoServicosTest
                .suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerAulasDeSalaEmSemestreServicosTest.suite());
        suite
                .addTest(ServidorAplicacao.Servicos.sop.LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricularServicosTest
                        .suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerSalaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerTurmaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerSalasServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerTurmasServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerTurnosDeTurmaServicosTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.sop.LerTurnosDeDisciplinaExecucaoServicosTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.sop.LerTurnoServicosTest.suite()); 
		suite.addTest(ServidorAplicacao.Servicos.sop.RemoverTurnoServicosTest.suite());        
//		suite.addTest(ServidorAplicacao.Servicos.sop.LerAulaServicosTest.suite());
//		suite.addTest(ServidorAplicacao.Servicos.sop.LerLicenciaturaServicosTest.suite());
//		suite.addTest(ServidorAplicacao.Servicos.sop.LerLicenciaturaExecucaoDeLicenciaturaServicosTest.suite());
//		suite.addTest(ServidorAplicacao.Servicos.sop.LerLicenciaturasServicosTest.suite());

		suite.addTest(ServidorAplicacao.Servicos.student.ReadShiftEnrolmentTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.student.ReadShiftsByTypeFromExecutionCourseServicesTest
                .suite());
		suite.addTest(ServidorAplicacao.Servicos.student.ReadStudentLessonsTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.student.ReadShiftLessonsTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.student.ReadStudentTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.student.StudentShiftEnrolmentTest.suite());

		suite.addTest(ServidorAplicacao.Servicos.publico.SelectRoomsTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.publico.SelectClassesTest.suite());
		suite.addTest(ServidorAplicacao.Servicos.publico.SelectShiftsTest.suite());
        suite.addTest(ServidorAplicacao.Servicos.publico.ReadCurricularCourseListOfExecutionCourseTest
                .suite());
		suite.addTest(ServidorAplicacao.Servicos.publico.SelectExecutionCourseTest.suite());
        suite
                .addTest(ServidorAplicacao.Servicos.publico.SelectExecutionShiftsWithAssociatedLessonsAndClassesTest
                        .suite());
       
        return suite;
    }
}
