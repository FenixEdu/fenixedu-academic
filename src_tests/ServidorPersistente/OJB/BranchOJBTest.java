package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Branch;
import Dominio.IBranch;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class BranchOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentBranch persistentBranch = null;

	public BranchOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class BranchOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class BranchOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(BranchOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error in SetUp.");
		}
		persistentBranch = persistentSupport.getIPersistentBranch();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------
	public void testWriteBranch() {

		// branch ja existente
		IBranch branch = new Branch("Inteligencia Artificial", "IA");

		System.out.println("- Test 1.1 : Write Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			persistentBranch.lockWrite(branch);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing Branch");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
//				NOTE: DAVID-RICARDO: Aqui devia estar um cancelarTransaccao mas nao esta porque rebenta (java.util.ConcurrentModificationException)
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing Branch");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing Branch");
		}

		// branch inexistente
		branch = new Branch("Inteligencia Artificial IX", "IA IX");

		System.out.println("- Test 1.2 : Write Non Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			persistentBranch.lockWrite(branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing Branch");
		}

		IBranch b2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			b2 = persistentBranch.readBranchByNameAndCode(branch.getName(), branch.getCode());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing Branch Just Writen Before");
		}

		assertNotNull(b2);

		assertTrue(b2.getCode().equals(branch.getCode()));
		assertTrue(b2.getName().equals(branch.getName()));

		assertNull(b2.getScopes());
		assertNull(b2.getAssociatedStudentCurricularPlans());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllBranches() {

		System.out.println("- Test 2 : Delete All Branches");
		try {
			persistentSupport.iniciarTransaccao();
			persistentBranch.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete All Branches");
		}

		ArrayList result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentBranch.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All Branches");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------
	public void testReadBranch() {

		IBranch branch = null;

		// branch ja existente
		System.out.println("- Test 3.1 : Read Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			branch = persistentBranch.readBranchByNameAndCode("Inteligencia Artificial", "IA");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing Branch");
		}
		assertNotNull(branch);
		assertTrue(branch.getName().equals("Inteligencia Artificial"));
		assertTrue(branch.getCode().equals("IA"));
		assertNotNull(branch.getScopes());
		assertNotNull(branch.getAssociatedStudentCurricularPlans());

		// branch inexistente
		branch = null;
		System.out.println("- Test 3.2 : Read Non Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			branch = persistentBranch.readBranchByNameAndCode("Unknown", "unk");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing Branch");
		}
		assertNull(branch);
	}

	// -------------------------------------------------------------------------------------------------------------------------
	public void testDeleteBranch() {

		IBranch branch = null;

		// branch ja existente
		System.out.println("- Test 4.1 : Delete Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			branch = persistentBranch.readBranchByNameAndCode("Inteligencia Artificial", "IA");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing Branch To Delete");
		}
		assertNotNull(branch);

		try {
			persistentSupport.iniciarTransaccao();
			persistentBranch.delete(branch);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing Branch");
		}

		IBranch b = null;
		try {
			persistentSupport.iniciarTransaccao();
			b = persistentBranch.readBranchByNameAndCode("Inteligencia Artificial", "IA");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted Branch");
		}
		assertNull(b);

		// branch inexistente
		System.out.println("- Test 4.2 : Delete Non Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			persistentBranch.delete(new Branch());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing Branch");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------
	public void testReadAllBranches() {

		ArrayList list = null;

		System.out.println("- Test 5 : Read All Existing Branch");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentBranch.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All Branches");
		}
		assertNotNull(list);
		assertEquals(list.size(), 3);
	}

}