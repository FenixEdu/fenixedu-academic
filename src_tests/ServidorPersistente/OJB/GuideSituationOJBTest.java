
package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.GuideSituation;
import Dominio.IGuide;
import Dominio.IGuideSituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideSituation;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.SituationOfGuide;
import Util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideSituationOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentGuide persistentGuide = null;
	IPersistentGuideSituation persistentGuideSituation = null;
	
	public GuideSituationOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(GuideSituationOJBTest.class);
        
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
		persistentGuide = persistentSupport.getIPersistentGuide();
		persistentGuideSituation = persistentSupport.getIPersistentGuideSituation();
	}
    
	protected void tearDown() {
		super.tearDown();
	}

	public void testReadActiveGuideSituation() {
		System.out.println("Test 1 - Read Active Guide Situation");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);

			IGuideSituation guideSituation = persistentGuideSituation.readGuideActiveSituation(guide);
			assertNotNull(guideSituation);
			
			assertEquals(guideSituation.getGuide(), guide);
			assertEquals(guideSituation.getState(), new State(State.ACTIVE));
			assertEquals(guideSituation.getRemarks(), "pago");
			
			assertEquals(guideSituation.getDate().toString(), "2003-10-06");
			assertEquals(guideSituation.getSituation(), SituationOfGuide.PAYED_TYPE);

			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadGuideSituation: unexpected exception");
		}
	}
	
	public void testReadAllGuideSituation() {
		System.out.println("Test 2 - Read All Guide Situations");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);

			List result = persistentGuideSituation.readByGuide(guide);
			assertNotNull(result);
			
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 2);
			

			guide = persistentGuide.readByNumberAndYear(new Integer(2), new Integer(2003));
			assertNotNull(guide);

			result = persistentGuideSituation.readByGuide(guide);
			assertNotNull(result);

			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 1);

			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadGuideSituation: unexpected exception");
		}
	}


	public void testReadGuideSituation() {
		System.out.println("Test 3 - Read Guide Situations");        

		try {
			persistentSupport.iniciarTransaccao();

			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);

			
			IGuideSituation guideSituation = persistentGuideSituation.readByGuideAndSituation(guide, SituationOfGuide.PAYED_TYPE);
			assertNotNull(guideSituation);
			
			assertEquals(guideSituation.getGuide(), guide);
			assertEquals(guideSituation.getState(), new State(State.ACTIVE));
			assertEquals(guideSituation.getRemarks(), "pago");
			
			assertEquals(guideSituation.getDate().toString(), "2003-10-06");
			assertEquals(guideSituation.getSituation(), SituationOfGuide.PAYED_TYPE);

			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadGuideSituation: unexpected exception");
		}
	}

	public void testWriteExistingGuideSituation() {
		System.out.println("Test 4 - Write Existing Guide Situations");        

		try {
			persistentSupport.iniciarTransaccao();

			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);

			IGuideSituation guideSituation = new GuideSituation();
			guideSituation.setGuide(guide);
			guideSituation.setSituation(SituationOfGuide.PAYED_TYPE);
			persistentGuideSituation.write(guideSituation);			

			persistentSupport.confirmarTransaccao();
		} catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteExistingGuideSituation: unexpected exception");
		}
	}

	public void testWriteNonExistingGuideSituation() {
		System.out.println("Test 5 - Write Non Existing Guide Situations");        

		Calendar calendar = Calendar.getInstance();
		
		try {
			persistentSupport.iniciarTransaccao();

			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);

			IGuideSituation guideSituation = new GuideSituation();
			guideSituation.setGuide(guide);
			guideSituation.setSituation(SituationOfGuide.ANNULLED_TYPE);
			guideSituation.setDate(calendar.getTime());
			guideSituation.setState(new State(State.ACTIVE));

			// Check if Exists
			IGuideSituation guideSituationBD = persistentGuideSituation.readByGuideAndSituation(guide, SituationOfGuide.ANNULLED_TYPE);
			assertNull(guideSituationBD);

			persistentGuideSituation.write(guideSituation);			
			persistentSupport.confirmarTransaccao();
			
			
			persistentSupport.iniciarTransaccao();
			guideSituationBD = persistentGuideSituation.readByGuideAndSituation(guide, SituationOfGuide.ANNULLED_TYPE);
			assertNotNull(guideSituationBD);
			assertEquals(guideSituation, guideSituationBD);
			persistentSupport.confirmarTransaccao();
			
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteNonExistingGuideSituation: unexpected exception");
		}
	}

}