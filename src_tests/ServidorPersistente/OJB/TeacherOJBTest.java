package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;

/**
 * @author Ivo Brandão
 */ 
public class TeacherOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentTeacher persistentTeacher = null;
	
    public TeacherOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TeacherOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error while setting up");
		}
		persistentTeacher = persistentSupport.getIPersistentTeacher();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
    public void testLockWrite() {
		ITeacher teacher = null;

        // write non existing
        teacher = new Teacher("newUser", "newPassword", new Integer("2"));
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentTeacher.lockWrite(teacher);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia excepcaoPersistencia) {
		    fail("testLockWrite: write non Existing");	
        }
        
        ITeacher teacherRead = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            teacherRead = persistentTeacher.readTeacherByNumber(teacher.getTeacherNumber());
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia excepcaoPersistencia) {
            fail("testEscreverPais: unexpected exception reading");
        }
        assertNotNull(teacherRead);
        assertEquals(teacherRead.getTeacherNumber(), teacher.getTeacherNumber());
//        assertEquals(teacherRead.getSitesOwned(), teacher.getSitesOwned());
//        assertEquals(teacherRead.getProfessorShipsSites(), teacher.getProfessorShipsSites());
		assertEquals(teacherRead.getProfessorShipsExecutionCourses(), new ArrayList());
		assertEquals(teacherRead.getResponsableForExecutionCourses(), new ArrayList());
        
    }

    public void testDeleteAllTeachers() {

		//read all existing
		List result = null;
		try {
			persistentSupport.iniciarTransaccao();
			result = persistentTeacher.readAll();
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testDeleteAllTeachers: readAll");
		}
		assertNotNull(result);
		assertEquals(result.isEmpty(), false);

		//erase all existing        
        try {
            persistentSupport.iniciarTransaccao();
            persistentTeacher.deleteAll();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testDeleteAllTeachers: deleteAll");
        }

		//read all existing again
        result = null;
        try {
            persistentSupport.iniciarTransaccao();
            result = persistentTeacher.readAll();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testDeleteAllTeachers: readAll");
        }
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    public void testReadTeacherByUsername() {
        ITeacher teacher = null;

		//read existing
        try {
            persistentSupport.iniciarTransaccao();
            teacher = persistentTeacher.readTeacherByUsername("teacher1");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia excepcaoPersistencia) {
            fail("testReadTeacherByUsername: confirmarTransaccao");
        }
        assertNotNull(teacher);
        assertTrue(teacher.getTeacherNumber().equals(new Integer("1")));
        assertTrue(teacher.getResponsableForExecutionCourses().equals(new ArrayList()));
		assertTrue(teacher.getProfessorShipsExecutionCourses().equals(new ArrayList()));

		//read unexisting
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByUsername("teacher2");
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByUsername: confirmarTransaccao");
		}
		assertNull(teacher);
	}

	public void testReadTeacherByNumber() {
		ITeacher teacher = null;

		//read existing
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByUsername: confirmarTransaccao");
		}
		assertNotNull(teacher);
		assertTrue(teacher.getTeacherNumber().equals(new Integer("1")));
		assertTrue(teacher.getProfessorShipsExecutionCourses().equals(new ArrayList()));
		assertTrue(teacher.getResponsableForExecutionCourses().equals(new ArrayList()));

		//read unexisting
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer("2"));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testReadTeacherByUsername: confirmarTransaccao");
		}
		assertNull(teacher);
	}

    public void testDeleteTeacher() {

        ITeacher teacher = null;

		//read existing        
        try {
            persistentSupport.iniciarTransaccao();
            teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testDeleteTeacher: readTeacherByNumber existing");
        }
        assertNotNull(teacher);

		//erase it
        try {
            persistentSupport.iniciarTransaccao();
            persistentTeacher.delete(teacher);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testDeleteTeacher: delete");
        }
        
        //read it again
		try {
			persistentSupport.iniciarTransaccao();
			teacher = persistentTeacher.readTeacherByNumber(new Integer("1"));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testDeleteTeacher: readTeacherByNumber unexisting");
		}
		assertNull(teacher);
        
    }

    public void testReadAllTeachers() {
        List list = null;

        try {
            persistentSupport.iniciarTransaccao();
            list = persistentTeacher.readAll();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testReadAllTeachers: readAll");
        }
        assertNotNull(list);
    }
}