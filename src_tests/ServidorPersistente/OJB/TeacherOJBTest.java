package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.exceptions.ExistingPersistentException;

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
        ITeacher teacher = new Teacher("teacher1", "password", new Integer("1"));

		// write existing
        try {
            persistentSupport.iniciarTransaccao();
            persistentTeacher.lockWrite(teacher);
            persistentSupport.confirmarTransaccao();
            fail("testLockWrite: existing teacher");
		} catch(ExistingPersistentException ex) {
			// All is OK
        } catch(ExcepcaoPersistencia excepcaoPersistencia) {
			fail("testLockWrite: unexpected exception writing");
        }
        
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
        assertEquals(teacherRead.getSitesOwned(), teacher.getSitesOwned());
        assertEquals(teacherRead.getProfessorShipsSites(), teacher.getProfessorShipsSites());
    }

//    public void testDeleteAllCountrys() {
//        
//        try {
//            persistentSupport.iniciarTransaccao();
//            persistentCountry.deleteAllCountrys();
//            persistentSupport.confirmarTransaccao();
//            assertTrue("testApagarTodosOsPaises: Paises apagados", true);
//        } catch(ExcepcaoPersistencia ex2) {
//            fail("testApagarTodosOsPaises: confirmarTransaccao_1");
//        }
//
//        ArrayList result = null;
//        
//        try {
//            persistentSupport.iniciarTransaccao();
//            result = persistentCountry.readAllCountrys();
//            persistentSupport.confirmarTransaccao();
//        } catch(ExcepcaoPersistencia ex) {
//            fail("testApagarTodosOsPaises: confirmarTransaccao_2");
//        }
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

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
        assertTrue(teacher.getSitesOwned().equals(new ArrayList()));
		assertTrue(teacher.getProfessorShipsSites().equals(new ArrayList()));

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
		assertTrue(teacher.getSitesOwned().equals(new ArrayList()));
		assertTrue(teacher.getProfessorShipsSites().equals(new ArrayList()));

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

//    public void testDeleteCountry() {
//
//
//        try {
//            persistentSupport.iniciarTransaccao();
//			persistentCountry.deleteCountryByName("Portugal");
//            persistentSupport.confirmarTransaccao();
//        } catch(ExcepcaoPersistencia ex3) {
//            fail("testApagarPais: confirmarTransaccao_1");
//        }
//        ICountry country = null;
//        
//        try {
//            persistentSupport.iniciarTransaccao();
//            country = persistentCountry.readCountryByName("Portugal");
//            persistentSupport.confirmarTransaccao();
//        } catch(ExcepcaoPersistencia ex) {
//            fail("testApagarPais: lerPaisPorNome");
//        }
//        assertNull(country);
//
//        try {
//            persistentSupport.iniciarTransaccao();
//            persistentCountry.deleteCountryByName("Chipre");
//            persistentSupport.confirmarTransaccao();
//            assertTrue("testApagarPais: Pais apagado", true);
//        } catch(ExcepcaoPersistencia ex2) {
//            fail("testApagarPais: confirmarTransaccao_2");
//        }
//    }

//    public void testReadAllCountrys() {
//        ArrayList list = null;
//
//
//        try {
//            persistentSupport.iniciarTransaccao();
//            list = persistentCountry.readAllCountrys();
//            persistentSupport.confirmarTransaccao();
//        } catch(ExcepcaoPersistencia ex2) {
//            fail("testLerTodosOsPaises: confirmarTransaccao_1");
//        }
//        assertNotNull(list);
//        assertEquals(list.size(), 2);
//    }
}