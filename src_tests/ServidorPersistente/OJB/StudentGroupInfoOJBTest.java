
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IStudentGroupInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupInfo;
import Util.StudentType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentGroupInfoOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentStudentGroupInfo persistentStudentGroupInfo = null;
	
	public StudentGroupInfoOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(StudentGroupInfoOJBTest.class);
        
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
		persistentStudentGroupInfo = persistentSupport.getIPersistentStudentGroupInfo();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadContributor() {
		System.out.println("Test 1 - Read Student Group Info");        
		IStudentGroupInfo studentGroupInfo = null;

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