/*
 * PlanoCurricularCursoOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;
import Dominio.PlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;

public class PlanoCurricularCursoOJBTest extends TestCaseOJB {
    public PlanoCurricularCursoOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(PlanoCurricularCursoOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testEscreverPlanoCurricular() {
        System.out.println("testEscreverPlanoCurricular");
        
        // Tentativa de escrita na BD de um objecto existente.
        IPlanoCurricularCurso planoCurricular = new PlanoCurricularCurso("plano1", curso1);
        
        try {
            _suportePersistente.iniciarTransaccao();
            planoCurricularCursoPersistente.escreverPlanoCurricular(planoCurricular);
            _suportePersistente.confirmarTransaccao();
            fail("testEscreverPlanoCurricular: confirmarTransaccao_1");
        } catch(ExcepcaoPersistencia ex2) {
			// All Is Ok
        }
        
        // Tentativa de escrita na BD de um objecto inexistente.
		ICurso cursoTemp = null;
		try {
            _suportePersistente.iniciarTransaccao();
            cursoTemp = cursoPersistente.readBySigla("LEIC");
            _suportePersistente.confirmarTransaccao();
			
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_2");
        }

        planoCurricular = new PlanoCurricularCurso("plano inexistente", cursoTemp);
        try {
            _suportePersistente.iniciarTransaccao();
            planoCurricularCursoPersistente.escreverPlanoCurricular(planoCurricular);
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_2");
        }
        
        // Agora faz-se uma tentativa de ler a planoCurricular que se acabou de escrever.
        IPlanoCurricularCurso planoCurricularCurso2 = null;

        try {
            _suportePersistente.iniciarTransaccao();
            planoCurricularCurso2 = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla(planoCurricular.getName(), null);
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverPlanoCurricular: confirmarTransaccao_3");
        }
        
        assertNotNull(planoCurricularCurso2);
        assertEquals(planoCurricularCurso2, planoCurricular);
        
    }
    
    
    public void testApagarTodosOsPlanosCurriculares() {
        System.out.println("testApagarTodosOsPlanosCurriculares");
        try { 
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodosOsPlanosCurriculares: iniciarTransaccao_1");
        }
        
        try {
            planoCurricularCursoPersistente.apagarTodosOsPlanosCurriculares();
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarTodosOsPlanosCurriculares: apagarTodosOsPlanosCurriculares");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarTodosOsPlanosCurriculares: Planos Curriculares apagados", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarTodosOsPlanosCurriculares: cancelarTransaccao_1");
            }
            fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
        
        // Agora faz-se uma tentativa de ler a planoCurricular que se acabou de escrever.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodasAsPlanoCurriculars: iniciarTransaccao_2");
        }
        
        ArrayList result = null;
        try {
            result = planoCurricularCursoPersistente.lerTodosOsPlanosCurriculares();
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarTodosOsPlanosCurriculares: lerTodosOsPlanosCurriculares");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarTodosOsPlanosCurriculares: cancelarTransaccao_2");
            }
            fail("testApagarTodosOsPlanosCurriculares: confirmarTransaccao_2");
        }
        // Porque ao tentar ler uma tabela vazia ele nao se queixa logo e'
        // devolvido um ArrayList vazio.
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerPlanoCurricular() {
        System.out.println("testLerPlanoCurricular");        
        // Tentativa de leitura da BD de um objecto existente.
        IPlanoCurricularCurso planoCurricularCurso = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            ICurso cursoTemp = cursoPersistente.readBySigla("LEIC");
            assertNotNull(cursoTemp);
            planoCurricularCurso = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeSiglaCurso("plano1", "pc1", cursoTemp);
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerPlanoCurricular: confirmarTransaccao_1");
        }
        assertNotNull(planoCurricularCurso);
        
        assertTrue(planoCurricularCurso.getName().equals("plano1"));

        assertTrue(planoCurricularCurso.getCurso().getNome().equals("Licenciatura de Engenharia Informatica e de Computadores"));
        
        // Tentativa de leitura da BD de um objecto inexistente.
        planoCurricularCurso = null;
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerPlanoCurricular: iniciarTransaccao_2");
        }
        
        try {
            planoCurricularCurso = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("nao existe", "ne");
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerPlanoCurricular: lerPlanoCurricularPorNomeESigla");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerPlanoCurricular: cancelarTransaccao_2");
            }
            fail("testLerPlanoCurricular: confirmarTransaccao_2");
        }
        assertNull(planoCurricularCurso);
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarPlanoCurricular() {
        System.out.println("testApagarPlanoCurricular");
        // Tentativa de apagar da BD um objecto existente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarPlanoCurricular: iniciarTransaccao_1");
        }
 
        try {
            planoCurricularCursoPersistente.apagarPlanoCurricularPorNomeESigla("plano1", "p1");
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarPlanoCurricular: apagarPlanoCurricularPorNomeESigla_1");
        }
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarPlanoCurricular: cancelarTransaccao_1");
            }
            fail("testApagarPlanoCurricular: confirmarTransaccao_1");
        }
        IPlanoCurricularCurso planoCurricularCurso = null;
        try {
            _suportePersistente.iniciarTransaccao();
            planoCurricularCurso = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("plano1", "p1");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarPlanoCurricular: cancelarTransaccao para lerPlanoCurricularPorNomeESigla");
            }
            fail("testApagarPlanoCurricular: lerPlanoCurricularPorNomeESigla");
        }
        assertNull(planoCurricularCurso);

        // Tentativa de apagar da BD um objecto inexistente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarPlanoCurricular: iniciarTransaccao_2");
        }
 
        try {
            planoCurricularCursoPersistente.apagarPlanoCurricular(new PlanoCurricularCurso());
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarPlanoCurricular: apagarPlanoCurricular_2");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarPlanoCurricular: PlanoCurricular apagada", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarPlanoCurricular: cancelarTransaccao_2");
            }
            fail("testApagarPlanoCurricular: confirmarTransaccao_2");
        }
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerTodosOsPlanosCurriculares() {
        System.out.println("testLerTodosOsPlanosCurriculares");
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodasPlanoCurriculars: iniciarTransaccao_1");
        }
 
        ArrayList listap = null;
        try {
            listap = planoCurricularCursoPersistente.lerTodosOsPlanosCurriculares();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodosOsPlanosCurriculares: lerTodosOsPlanosCurriculares");
        }
 
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerTodosOsPlanosCurriculares: cancelarTransaccao_1");
            }
            fail("testLerTodosOsPlanosCurriculares: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(listap.size(), 2);
    }
}