/*
 * StudentCurricularPlanOJBTest.java
 *
 * Created on 21 of December of 2002, 17:16
 *
 * Tests :
 *  -  1 : Read a Existing Student Curricular Plan (Active) 
 *  -  2 : Read a Non Existing Student Curricular Plan (Active)
 *  -  3 : Write a Existing Student Curricular Plan
 *  -  4 : Write a Non Existing Student Curricular Plan
 *  -  5 : Delete a Existing Student Curricular Plan
 *  -  6 : Delete a Non Existing Student Curricular Plan
 *  -  7 : Delete All
 *  -  8 : Read All Curricular Plans from a Student (Existing)
 *  -  9 : Read All Curricular Plans from a Student (Non Existing)
 * 
 */
 
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */


package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICurso;
import Dominio.IPlanoCurricularCurso;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

public class StudentCurricularPlanOJBTest extends TestCaseOJB {
    
    public StudentCurricularPlanOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        System.out.println("Begin of StudentCurricularPlanOJB Test \n");
        junit.textui.TestRunner.run(suite());
        System.out.println("End of StudentCurricularPlanOJB Test \n");
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(StudentCurricularPlanOJBTest.class);
        return suite;
    }
    
    protected void setUp(){
        super.setUp();
    }
    
    protected void tearDown(){
        super.tearDown();
    }
    
    public void testReadExisting() {
        System.out.println("- Test 1 : Read a Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan studentCurricularPlanTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }

		assertNotNull(studentCurricularPlanTemp);
		assertTrue(studentCurricularPlanTemp.getStudent().getNumber().intValue() == 600);
		assertTrue(studentCurricularPlanTemp.getCourseCurricularPlan().getCurso().getSigla().equals("LEIC"));
		assertTrue(studentCurricularPlanTemp.getCurrentState().getState().intValue() == StudentCurricularPlanState.ACTIVE);
        assertTrue(studentCurricularPlanTemp.getStartDate().toString().equals("2002-12-21"));
    }

    public void testReadNonExisting() {
        System.out.println("- Test 2 : Read a Non Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan studentCurricularPlanTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(999999);
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Non Existing");
        }

		assertNull(studentCurricularPlanTemp);
    }

    public void testWriteExisting() {
        System.out.println("- Test 3 : Write a Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan studentCurricularPlan = null;
        IStudent studentTemp = null;
        ICurso degree = null;
        IPlanoCurricularCurso degreeCurricularPlan;
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
            assertNotNull(studentTemp);
            
            degree = cursoPersistente.readBySigla("LEIC");
            assertNotNull(degree);
            degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano1", degree);
			assertNotNull(degreeCurricularPlan);
            _suportePersistente.confirmarTransaccao();

			studentCurricularPlan = new StudentCurricularPlan(studentTemp, degreeCurricularPlan, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.lockWrite(studentCurricularPlan);
            _suportePersistente.confirmarTransaccao();
            fail("    -> Espected Error");            
        } catch (ExcepcaoPersistencia ex) {
			// All is OK
        }
	}    

    public void testWriteNonExisting() {
        System.out.println("- Test 4 : Write a Non Existing Student Curricular Plan (Active) ");
		IStudentCurricularPlan studentCurricularPlan = null;
		IStudent studentTemp = null;
		ICurso degree = null;
		IPlanoCurricularCurso degreeCurricularPlan;
        
		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);
        
		try {
			_suportePersistente.iniciarTransaccao();
			studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentTemp);
            
			degree = cursoPersistente.readBySigla("LEEC");
			assertNotNull(degree);
			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano2", degree);
			assertNotNull(degreeCurricularPlan);
			_suportePersistente.confirmarTransaccao();

			studentCurricularPlan = new StudentCurricularPlan(studentTemp, degreeCurricularPlan, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			_suportePersistente.iniciarTransaccao();
			studentCurricularPlanPersistente.lockWrite(studentCurricularPlan);
			_suportePersistente.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail(" Fail");            		}
	}    
	
    public void testDeleteExisting() {
        System.out.println("- Test 5 : Delete a Existing Student Curricular Plan");
        IStudentCurricularPlan studentCurricularPlan = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlan = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            assertNotNull(studentCurricularPlan);
            _suportePersistente.confirmarTransaccao();
         
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.delete(studentCurricularPlan);
            _suportePersistente.confirmarTransaccao();
            
			_suportePersistente.iniciarTransaccao();
			studentCurricularPlan = null;
			studentCurricularPlan = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
			assertNull(studentCurricularPlan);
			_suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }    

    public void testDeleteNonExisting() {
        System.out.println("- Test 6 : Delete a Non Existing Student Curricular Plan");
		IStudentCurricularPlan studentCurricularPlan = null;
		IStudent studentTemp = null;
		ICurso degree = null;
		IPlanoCurricularCurso degreeCurricularPlan;
        
		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);
        
		try {
			_suportePersistente.iniciarTransaccao();
			studentTemp = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
			assertNotNull(studentTemp);
            
			degree = cursoPersistente.readBySigla("LEEC");
			assertNotNull(degree);
			degreeCurricularPlan = planoCurricularCursoPersistente.readByNameAndDegree("plano2", degree);
			assertNotNull(degreeCurricularPlan);
			_suportePersistente.confirmarTransaccao();

			studentCurricularPlan = new StudentCurricularPlan(studentTemp, degreeCurricularPlan, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
			_suportePersistente.iniciarTransaccao();
			studentCurricularPlanPersistente.delete(studentCurricularPlan);
			_suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Deleting Non Existing");
        }
	}    
    
    public void testDeleteAll() {
        System.out.println("- Test 7 : Delete all Existing Student Curricular Plan");
        IStudentCurricularPlan studentCurricularPlan = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlan = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            assertNotNull(studentCurricularPlan);
            _suportePersistente.confirmarTransaccao();
         
         	assertNotNull(studentCurricularPlan);
         
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.deleteAll();
            _suportePersistente.confirmarTransaccao();
            
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlan = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
			assertNull(studentCurricularPlan);
            _suportePersistente.confirmarTransaccao();
         
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }    
    
    public void testReadAllFromStudent() {
        System.out.println("- Test 8 : Read All Curricular Plans from a Student (Existing)");
        List studentCurricularPlans = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlans = studentCurricularPlanPersistente.readAllFromStudent(600);
            _suportePersistente.confirmarTransaccao();
         
         	assertNotNull(studentCurricularPlans);
         	assertTrue(studentCurricularPlans.size() == 2);
         
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }    
    
    public void testReadAllFromStudentNonExisting() {
        System.out.println("- Test 9 : Read All Curricular Plans from a Student (Non Existing)");
        List curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readAllFromStudent(99999);
            _suportePersistente.confirmarTransaccao();
         
         	assertNotNull(curricularTemp);
         	assertTrue(curricularTemp.size() == 0);
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }        
    
}
