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
 *  - 10 : Test Equal Objects
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

import Dominio.IStudent;
import Dominio.IPlanoCurricularCurso;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.StudentCurricularPlanState;

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
        IStudentCurricularPlan curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }

		assertNotNull(curricularTemp);
		assertTrue(curricularTemp.getStudent().getNumber().intValue() == 600);
		assertTrue(curricularTemp.getCourseCurricularPlan().getCurso().getSigla().equals("LEIC"));
		assertTrue(curricularTemp.getCurrentState().getState().intValue() == StudentCurricularPlanState.ACTIVE);
        assertTrue(curricularTemp.getStartDate().toString().equals("2002-12-21"));
    }

    public void testReadNonExisting() {
        System.out.println("- Test 2 : Read a Non Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(999999);
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Non Existing");
        }

		assertNull(curricularTemp);
    }

    public void testWriteExisting() {
        System.out.println("- Test 3 : Write a Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan curricularTemp = null;
        IStudent studentTemp = null;
        IPlanoCurricularCurso courseTemp;
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentTemp = persistentStudent.readByNumero(new Integer(600), licenciatura);
            courseTemp = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("plano1", "pc1");
            _suportePersistente.confirmarTransaccao();

			curricularTemp = new StudentCurricularPlan(studentTemp, courseTemp, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.lockWrite(curricularTemp);
            _suportePersistente.confirmarTransaccao();
            fail("    -> Espected Error");            
        } catch (ExcepcaoPersistencia ex) {
			// All is OK
        }
	}    

    public void testWriteNonExisting() {
        System.out.println("- Test 4 : Write a Non Existing Student Curricular Plan (Active) ");
        IStudentCurricularPlan curricularTemp = null;
        IStudent studentTemp = null;
        IPlanoCurricularCurso courseTemp;
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentTemp = persistentStudent.readByNumero(new Integer(600), licenciatura);
            courseTemp = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("plano2", "pc2");
            _suportePersistente.confirmarTransaccao();

			curricularTemp = new StudentCurricularPlan(studentTemp, courseTemp, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.lockWrite(curricularTemp);
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Writing Non Existing");
        }
	}    
	
    public void testDeleteExisting() {
        System.out.println("- Test 5 : Delete a Existing Student Curricular Plan");
        IStudentCurricularPlan curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            _suportePersistente.confirmarTransaccao();
         
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.delete(curricularTemp);
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }    

    public void testDeleteNonExisting() {
        System.out.println("- Test 6 : Delete a Non Existing Student Curricular Plan");
        IStudentCurricularPlan curricularTemp = null;
        IStudent studentTemp = null;
        IPlanoCurricularCurso courseTemp;
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentTemp = persistentStudent.readByNumero(new Integer(600), licenciatura);
            courseTemp = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("plano2", "pc2");
            _suportePersistente.confirmarTransaccao();

			curricularTemp = new StudentCurricularPlan(studentTemp, courseTemp, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.delete(curricularTemp);
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Deleting Non Existing");
        }
	}    
    
    public void testDeleteAll() {
        System.out.println("- Test 7 : Delete all Existing Student Curricular Plan");
        IStudentCurricularPlan curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            _suportePersistente.confirmarTransaccao();
         
         	assertNotNull(curricularTemp);
         
            _suportePersistente.iniciarTransaccao();
            studentCurricularPlanPersistente.deleteAll();
            _suportePersistente.confirmarTransaccao();
            
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readActiveStudentCurricularPlan(600);
            _suportePersistente.confirmarTransaccao();
         
         	assertNull(curricularTemp);
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Reading Existing");
        }
    }    
    
    public void testReadAllFromStudent() {
        System.out.println("- Test 8 : Read All Curricular Plans from a Student (Existing)");
        List curricularTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            curricularTemp = studentCurricularPlanPersistente.readAllFromStudent(600);
            _suportePersistente.confirmarTransaccao();
         
         	assertNotNull(curricularTemp);
         	assertTrue(curricularTemp.size() == 2);
         
            
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
    
    public void testEquals() {
        System.out.println("- Test 10 : Test Equal Objects");
        IStudentCurricularPlan curricularTemp1 = null;
        IStudentCurricularPlan curricularTemp2 = null;
        IStudent studentTemp = null;
        IPlanoCurricularCurso courseTemp = null;
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
        
        try {
            _suportePersistente.iniciarTransaccao();
            studentTemp = persistentStudent.readByNumero(new Integer(600), licenciatura);
            courseTemp = planoCurricularCursoPersistente.lerPlanoCurricularPorNomeESigla("plano2", "pc2");
            _suportePersistente.confirmarTransaccao();

        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Failed Deleting Non Existing");
        }
    	curricularTemp1 = new StudentCurricularPlan(studentTemp, courseTemp, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));
		curricularTemp2 = new StudentCurricularPlan(studentTemp, courseTemp, data.getTime(), new StudentCurricularPlanState(StudentCurricularPlanState.ACTIVE));

		assertTrue(curricularTemp1.equals(curricularTemp2));

	}    
    
}
