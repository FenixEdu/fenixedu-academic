/*
 * Created on 21/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Contributor;
import Dominio.IContributor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ContributorOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentContributor persistentContributor = null;
	
	public ContributorOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ContributorOJBTest.class);
        
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
		persistentContributor = persistentSupport.getIPersistentContributor();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadContributor() {
		System.out.println("Test 1 - Read Contributor");        
		IContributor contributor = null;

		try {
			persistentSupport.iniciarTransaccao();
			contributor = persistentContributor.readByContributorNumber(new Integer(123));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteContributor: unexpected exception");
		}
		
		assertNotNull(contributor);
		assertEquals(contributor.getContributorNumber(), new Integer(123));
		assertEquals(contributor.getContributorName(), "Nome1");
		assertEquals(contributor.getContributorAddress(), "Morada1");
		try {
			persistentSupport.iniciarTransaccao();
			contributor = persistentContributor.readByContributorNumber(new Integer(33333));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteContributor: unexpected exception");
		}
		
		assertNull(contributor);

	}

	public void testWriteNonExistingContributor() {
		System.out.println("Test 2 - Write a Non Existing Contributor");        
		IContributor contributor = new Contributor(new Integer(1111), "Desc", "Desc");

		try {
			persistentSupport.iniciarTransaccao();
			persistentContributor.write(contributor);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteContributor: unexpected exception");
		}
		
		// Check the Insert
		IContributor contributorTest = null;
		
		try {
			persistentSupport.iniciarTransaccao();
			contributorTest = persistentContributor.readByContributorNumber(new Integer(1111));
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteContributor: unexpected exception");
		}
		
		assertNotNull(contributorTest);
		assertEquals(contributor.getContributorNumber(), contributorTest.getContributorNumber());
		assertEquals(contributor.getContributorName(), contributorTest.getContributorName());
		assertEquals(contributor.getContributorAddress(), contributorTest.getContributorAddress());
	}

	public void testWriteExistingContributor() {
		System.out.println("Test 3 - Write an Existing Contributor");        
		IContributor contributor = new Contributor(new Integer(123), "Nome1", "Morada1");

		try {
			persistentSupport.iniciarTransaccao();
			persistentContributor.write(contributor);
			persistentSupport.confirmarTransaccao();
			fail("testWriteContributor");
		} catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex) {
				fail("testWriteContributor: unexpected exception");
		}
	}
        
	public void testReadAll() {
		System.out.println("Test 4 - Read All Contributors");        
		
		try {
			persistentSupport.iniciarTransaccao();
			List result = persistentContributor.readAll();
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 2);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
				fail("testAllContributors: unexpected exception");
		}
	
	}
		
	public void testReadContributorListByNumber() {
		System.out.println("Test 5 - Read Contributor List By Number");        
		List contributorList = null;
		IContributor contributor = null;

		try {
			persistentSupport.iniciarTransaccao();
			contributorList = persistentContributor.readContributorListByNumber(new Integer(123));
			assertTrue(!contributorList.isEmpty());
			contributor = (IContributor) contributorList.get(0);
			assertEquals(contributor.getContributorNumber(),new Integer(123));	
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadContributorListByNumber: unexpected exception");
		}
	
		
		
		try {
			persistentSupport.iniciarTransaccao();
			contributorList = persistentContributor.readContributorListByNumber(null);
			assertTrue(!contributorList.isEmpty());
			assertEquals(contributorList.size(), 2);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadContributorListByNumber: unexpected exception");
		}

	}
 


}