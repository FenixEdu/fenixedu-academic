
package ServidorPersistente.OJB;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.PersonRole;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RoleType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PersonRoleOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentRole persistentRole = null;
	IPersistentPersonRole persistentPersonRole = null;
	IPessoaPersistente persistentPerson = null;
	
	public PersonRoleOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(PersonRoleOJBTest.class);
        
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
		persistentPersonRole = persistentSupport.getIPersistentPersonRole();
		persistentPerson = persistentSupport.getIPessoaPersistente();
	}
    
	protected void tearDown() {
		super.tearDown();
	}

	public void testReadExistingPersonRole() {
		System.out.println("Test 1 - Read Existing Person Role");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IPessoa person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
			assertNotNull(person);
			
			IRole role = persistentSupport.getIPersistentRole().readByRoleType(RoleType.PERSON);
			assertNotNull(role);

			IPersonRole personRole = persistentSupport.getIPersistentPersonRole().readByPersonAndRole(person, role);
			assertNotNull(personRole);			
			assertEquals(personRole.getPerson().getUsername(), person.getUsername());
			assertEquals(personRole.getRole().getRoleType(), RoleType.PERSON);
			
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testPersonRole: unexpected exception");
		}
	}

	public void testReadNonExistingPersonRole() {
		System.out.println("Test 2 - Read Non Existing Person Role");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IPessoa person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
			assertNotNull(person);
			
			IRole role = persistentSupport.getIPersistentRole().readByRoleType(RoleType.STUDENT);
			assertNotNull(role);

			IPersonRole personRole = persistentSupport.getIPersistentPersonRole().readByPersonAndRole(person, role);
			assertNull(personRole);			
			
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testPersonRole: unexpected exception");
		}
	}

	public void testWriteNonExistingPersonRole() {
		System.out.println("Test 3 - Write Non Existing Person Role");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IPessoa person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
			assertNotNull(person);
			
			IRole role = persistentSupport.getIPersistentRole().readByRoleType(RoleType.STUDENT);
			assertNotNull(role);

			IPersonRole personRole = new PersonRole();
			personRole.setPerson(person);
			personRole.setRole(role);
			

			persistentSupport.getIPersistentPersonRole().write(personRole);
			persistentSupport.confirmarTransaccao();
			
			// Check the Insert

			persistentSupport.iniciarTransaccao();
			
			personRole = persistentSupport.getIPersistentPersonRole().readByPersonAndRole(person, role);
			assertNotNull(personRole);			
			assertEquals(personRole.getPerson().getUsername(), person.getUsername());
			assertEquals(personRole.getRole().getRoleType(), RoleType.STUDENT);
			persistentSupport.confirmarTransaccao();
			
			
		} catch(ExcepcaoPersistencia ex) {
			fail("testPersonRole: unexpected exception");
		}
	}

	public void testWriteExistingPersonRole() {
		System.out.println("Test 4 - Write Existing Person Role");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IPessoa person = persistentSupport.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
			assertNotNull(person);
			
			IRole role = persistentSupport.getIPersistentRole().readByRoleType(RoleType.PERSON);
			assertNotNull(role);

			IPersonRole personRole = new PersonRole();
			personRole.setPerson(person);
			personRole.setRole(role);
			persistentSupport.getIPersistentPersonRole().write(personRole);
			
			persistentSupport.confirmarTransaccao();
		} catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex) {
			fail("testPersonRole: unexpected exception");
		}
	}


}