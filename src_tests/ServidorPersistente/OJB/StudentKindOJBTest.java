
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IStudentKind;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentKind;
import Util.StudentType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentKindOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentStudentKind persistentStudentGroupInfo = null;
	
	public StudentKindOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(StudentKindOJBTest.class);
        
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
		persistentStudentGroupInfo = persistentSupport.getIPersistentStudentKind();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadContributor() {
		System.out.println("Test 1 - Read Student Group Info");        
		IStudentKind studentGroupInfo = null;

		try {
			persistentSupport.iniciarTransaccao();
			studentGroupInfo = persistentStudentGroupInfo.readByStudentType(new StudentType(StudentType.NORMAL));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteContributor: unexpected exception");
		}
		
		assertNotNull(studentGroupInfo);
		assertEquals(studentGroupInfo, new StudentType(StudentType.NORMAL));

	}

 


}