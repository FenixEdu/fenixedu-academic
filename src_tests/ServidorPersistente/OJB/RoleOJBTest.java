
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IRole;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRole;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentRole persistentRole = null;
	
	public RoleOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(RoleOJBTest.class);
        
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
		persistentRole = persistentSupport.getIPersistentRole();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadExistingRole() {
		System.out.println("Test 1 - Read Existing Role");        

		try {
			persistentSupport.iniciarTransaccao();
			IRole role = persistentRole.readByRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
			assertNotNull(role);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testRole: unexpected exception");
		}
	}


}