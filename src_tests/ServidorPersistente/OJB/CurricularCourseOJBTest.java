/*
 * CurricularCourseOJBTest.java
 *
 * Created on 28 of December 2002, 12:08
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IDisciplinaDepartamento;
import Dominio.IPlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;


public class CurricularCourseOJBTest extends TestCaseOJB {
    public CurricularCourseOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CurricularCourseOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }

// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testWriteCurricularCourse() {
		IDisciplinaDepartamento departmentCourse = null;
        IPlanoCurricularCurso degreeCurricularPlan = null;

        
        try {
            _suportePersistente.iniciarTransaccao();
            departmentCourse = disciplinaDepartamentoPersistente.lerDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");
            degreeCurricularPlan = planoCurricularCursoPersistente.readByName("plano1");
            _suportePersistente.confirmarTransaccao();        	
        } catch(ExcepcaoPersistencia ex) {
            fail("testWriteCurricularCourse");
        }
		
		assertNotNull(departmentCourse);
		assertNotNull(degreeCurricularPlan);
        
        
        ICurricularCourse curricularCourse = new CurricularCourse(new Double(0.0),new Double(0.0), new Double(0.0),new Double(0.0), new Double(0.0), new Integer(2), new Integer(1), "Trabalho Final de Curso I", "TFCI", departmentCourse, degreeCurricularPlan);

        try {
            _suportePersistente.iniciarTransaccao();
            persistantCurricularCourse.writeCurricularCourse(curricularCourse);
            _suportePersistente.confirmarTransaccao();
            fail("testWriteCurricularCourse: confirmarTransaccao_1");
        } catch(ExcepcaoPersistencia ex2) {
			// All Is OK
        }

        curricularCourse = new CurricularCourse(new Double(0.0),new Double(0.0), new Double(0.0), new Double(0.0),new Double(0.0), new Integer(2), new Integer(1), "Trabalho Final de Curso IX", "TFCIX", departmentCourse, degreeCurricularPlan);

        try {
            _suportePersistente.iniciarTransaccao();
            persistantCurricularCourse.writeCurricularCourse(curricularCourse);
            _suportePersistente.confirmarTransaccao();
            assertTrue("testWriteCurricularCourse: Unexisting Object", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testWriteCurricularCourse: confirmarTransaccao_2");
        }

        ICurricularCourse dc2 = null;

        try {
            _suportePersistente.iniciarTransaccao();
            dc2 = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso IX", "TFCIX");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testWriteCurricularCourse: confirmarTransaccao_3");
        }

        assertNotNull(dc2);

        assertTrue(dc2.getCode().equals(curricularCourse.getCode()));
        assertTrue(dc2.getName().equals(curricularCourse.getName()));
        assertTrue(dc2.getCredits().equals(curricularCourse.getCredits()));
        assertTrue(dc2.getCurricularYear().equals(curricularCourse.getCurricularYear()));
        assertTrue(dc2.getLabHours().equals(curricularCourse.getLabHours()));
        assertTrue(dc2.getPraticalHours().equals(curricularCourse.getPraticalHours()));
        assertTrue(dc2.getTheoPratHours().equals(curricularCourse.getTheoPratHours()));
        assertTrue(dc2.getTheoreticalHours().equals(curricularCourse.getTheoreticalHours()));
        assertTrue(dc2.getSemester().equals(curricularCourse.getSemester()));
        assertNotNull(dc2.getAssociatedExecutionCourses());
 		assertTrue(dc2.getAssociatedExecutionCourses().size() == 0);
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testDeleteAllCurricularCourses() {
        
        try {
            _suportePersistente.iniciarTransaccao();
            persistantCurricularCourse.deleteAllCurricularCourse();
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarTodasAsDisciplinasCurriculares: Disciplinas Curriculares apagadas", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodasAsDisciplinasCurriculares: confirmarTransaccao_1");
        }

        ArrayList result = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            result = persistantCurricularCourse.readAllCurricularCourses();
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodasAsDisciplinasCurriculares: confirmarTransaccao_2");
        }

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
//// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testReadCurricularCourse() {

        ICurricularCourse dc = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            dc = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDisciplinaCurricular: confirmarTransaccao_1");
        }
        assertNotNull(dc);
        assertTrue(dc.getName().equals("Trabalho Final de Curso I"));
        assertTrue(dc.getCode().equals("TFCI"));
        assertTrue(dc.getCredits().doubleValue() == 0);
        assertTrue(dc.getCurricularYear().intValue() == 2);
        assertTrue(dc.getLabHours().doubleValue() == 0);
        assertTrue(dc.getPraticalHours().doubleValue() == 0);
        assertTrue(dc.getTheoPratHours().doubleValue() == 0);
        assertTrue(dc.getTheoreticalHours().doubleValue() == 0);
        assertTrue(dc.getSemester().intValue() == 1);
        assertNotNull(dc.getAssociatedExecutionCourses());
        
        dc = null;
        try {
            _suportePersistente.iniciarTransaccao();
            dc = persistantCurricularCourse.readCurricularCourseByNameCode("Unknown", "unk");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDisciplinaCurricular: confirmarTransaccao_2");
        }
        assertNull(dc);
    }
//// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testDeleteCurricularCourse() {

		ICurricularCourse curricularCourse = null;

		try {
			_suportePersistente.iniciarTransaccao();
    		curricularCourse = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplinaCurricular: iniciarTransaccao_1");
        }
		assertNotNull(curricularCourse);

        try {
            _suportePersistente.iniciarTransaccao();
            persistantCurricularCourse.deleteCurricularCourse(curricularCourse);
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            fail("testApagarDisciplinaCurricular: confirmarTransaccao_1");
        }

        ICurricularCourse dc = null;
        try {
            _suportePersistente.iniciarTransaccao();
            dc = persistantCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso", "TFCI");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplinaCurricular: lerDisciplinaCurricularPorDisciplinaESigla");
        }
        assertNull(dc);

        try {
            _suportePersistente.iniciarTransaccao();
            persistantCurricularCourse.deleteCurricularCourse(new CurricularCourse());
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarDisciplinaCurricular: Disciplina Curricular apagada", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarDisciplinaCurricular: confirmarTransaccao_2");
        }
    }
//// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testReadAllCurricularCourses() {
        ArrayList list = null;


        try {
            _suportePersistente.iniciarTransaccao();
            list = persistantCurricularCourse.readAllCurricularCourses();
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerTodasDisciplinasCurriculares: confirmarTransaccao_1");
        }
        assertNotNull(list);
        assertEquals(list.size(), 10);
    }
}