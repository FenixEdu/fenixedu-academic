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
import Dominio.ICurso;
import Dominio.IDisciplinaDepartamento;
import Dominio.IPlanoCurricularCurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IDisciplinaDepartamentoPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPlanoCurricularCursoPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;


public class CurricularCourseOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IDisciplinaDepartamentoPersistente persistentDepartmentCourse = null;
	ICursoPersistente persistentDegree = null;
	IPlanoCurricularCursoPersistente persistentDegreeCurricularPlan = null;
	IPersistentCurricularCourse persistentCurricularCourse = null;
	
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
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentDepartmentCourse = persistentSupport.getIDisciplinaDepartamentoPersistente();	
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentDegreeCurricularPlan = persistentSupport.getIPlanoCurricularCursoPersistente();
		persistentCurricularCourse = persistentSupport.getIPersistentCurricularCourse();
    }
    
    protected void tearDown() {
        super.tearDown();
    }

// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testWriteCurricularCourse() {
		IDisciplinaDepartamento departmentCourse = null;
        IPlanoCurricularCurso degreeCurricularPlan = null;

        try {
            persistentSupport.iniciarTransaccao();
            departmentCourse = persistentDepartmentCourse.lerDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");
            
			ICurso degree = persistentDegree.readBySigla("LEIC");
			assertNotNull(degree);
            
            degreeCurricularPlan = persistentDegreeCurricularPlan.readByNameAndDegree("plano1", degree);
            persistentSupport.confirmarTransaccao();        	
        } catch(ExcepcaoPersistencia ex) {
            fail("testWriteCurricularCourse");
        }
		
		assertNotNull(departmentCourse);
		assertNotNull(degreeCurricularPlan);
        
        ICurricularCourse curricularCourse = new CurricularCourse(new Double(0.0),new Double(0.0), new Double(0.0),new Double(0.0), new Double(0.0), new Integer(2), new Integer(1), "Trabalho Final de Curso I", "TFCI", departmentCourse, degreeCurricularPlan);

        try {
            persistentSupport.iniciarTransaccao();
            persistentCurricularCourse.writeCurricularCourse(curricularCourse);
            persistentSupport.confirmarTransaccao();
            fail("testWriteCurricularCourse: confirmarTransaccao_1");
        } catch(ExistingPersistentException ex) {
			// All Is OK
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteCurricularCourse: unexpected exception");
		}

        curricularCourse = new CurricularCourse(new Double(0.0),new Double(0.0), new Double(0.0), new Double(0.0),new Double(0.0), new Integer(2), new Integer(1), "Trabalho Final de Curso IX", "TFCIX", departmentCourse, degreeCurricularPlan);

        try {
            persistentSupport.iniciarTransaccao();
            persistentCurricularCourse.writeCurricularCourse(curricularCourse);
            persistentSupport.confirmarTransaccao();
            assertTrue("testWriteCurricularCourse: Unexisting Object", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testWriteCurricularCourse: confirmarTransaccao_2");
        }

        ICurricularCourse dc2 = null;

        try {
            persistentSupport.iniciarTransaccao();
            dc2 = persistentCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso IX", "TFCIX");
            persistentSupport.confirmarTransaccao();
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
            persistentSupport.iniciarTransaccao();
            persistentCurricularCourse.deleteAllCurricularCourse();
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarTodasAsDisciplinasCurriculares: Disciplinas Curriculares apagadas", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodasAsDisciplinasCurriculares: confirmarTransaccao_1");
        }

        ArrayList result = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            result = persistentCurricularCourse.readAllCurricularCourses();
            persistentSupport.confirmarTransaccao();
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
            persistentSupport.iniciarTransaccao();
            dc = persistentCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
            persistentSupport.confirmarTransaccao();
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
            persistentSupport.iniciarTransaccao();
            dc = persistentCurricularCourse.readCurricularCourseByNameCode("Unknown", "unk");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDisciplinaCurricular: confirmarTransaccao_2");
        }
        assertNull(dc);
    }
//// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testDeleteCurricularCourse() {

		ICurricularCourse curricularCourse = null;

		try {
			persistentSupport.iniciarTransaccao();
    		curricularCourse = persistentCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso I", "TFCI");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplinaCurricular: iniciarTransaccao_1");
        }
		assertNotNull(curricularCourse);

        try {
            persistentSupport.iniciarTransaccao();
            persistentCurricularCourse.deleteCurricularCourse(curricularCourse);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            fail("testApagarDisciplinaCurricular: confirmarTransaccao_1");
        }

        ICurricularCourse dc = null;
        try {
            persistentSupport.iniciarTransaccao();
            dc = persistentCurricularCourse.readCurricularCourseByNameCode("Trabalho Final de Curso", "TFCI");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplinaCurricular: lerDisciplinaCurricularPorDisciplinaESigla");
        }
        assertNull(dc);

        try {
            persistentSupport.iniciarTransaccao();
            persistentCurricularCourse.deleteCurricularCourse(new CurricularCourse());
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarDisciplinaCurricular: Disciplina Curricular apagada", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarDisciplinaCurricular: confirmarTransaccao_2");
        }
    }
//// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testReadAllCurricularCourses() {
        ArrayList list = null;


        try {
            persistentSupport.iniciarTransaccao();
            list = persistentCurricularCourse.readAllCurricularCourses();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerTodasDisciplinasCurriculares: confirmarTransaccao_1");
        }
        assertNotNull(list);
        assertEquals(list.size(), 10);
    }
}