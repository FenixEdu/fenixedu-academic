
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IPrice;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrice;
import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PriceOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentPrice persistentPrice = null;
	
	public PriceOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(PriceOJBTest.class);
        
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
		persistentPrice = persistentSupport.getIPersistentPrice();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadAll() {
		System.out.println("Test 1 - Read All Prices");        

		try {
			persistentSupport.iniciarTransaccao();
			List result = persistentPrice.readAll();
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 6);
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadAllPrices: unexpected exception");
		}
	}

	public void testReadByGraduationType() {
		System.out.println("Test 2 - Read All by Graduation Type");        

		try {
			persistentSupport.iniciarTransaccao();
			List result = persistentPrice.readByGraduationType(GraduationType.MAJOR_DEGREE_TYPE);
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 2);
			
			result = persistentPrice.readByGraduationType(GraduationType.MASTER_DEGREE_TYPE);
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 4);

			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadAllByGraduationType: unexpected exception" + ex);
		}
	}

	public void testReadByGraduationTypeAndDocumentTypeAndDescription() {
		System.out.println("Test 3 - Read By Graduation Type And Document Type And Description");        

		try {
			persistentSupport.iniciarTransaccao();
			IPrice price = persistentPrice.readByGraduationTypeAndDocumentTypeAndDescription(GraduationType.MASTER_DEGREE_TYPE, DocumentType.CERTIFICATE_TYPE, "Matrícula");
			assertNotNull(price);
		
			assertEquals(price.getDescription(), "Matrícula");
			assertEquals(price.getDocumentType(), DocumentType.CERTIFICATE_TYPE);
			assertEquals(price.getGraduationType(), GraduationType.MASTER_DEGREE_TYPE);
			assertEquals(price.getPrice(), new Double(75));
			

			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadAllByGraduationType: unexpected exception" + ex);
		}
	}

	public void testReadByGraduationTypeAndDocumentType() {
			System.out.println("Test 4 - Read By Graduation Type And Document Type");        

			try {
				persistentSupport.iniciarTransaccao();
				List price = persistentPrice.readByGraduationTypeAndDocumentType(GraduationType.MASTER_DEGREE_TYPE, DocumentType.CERTIFICATE_TYPE);
				assertTrue(!price.isEmpty());
				assertEquals(price.size(), 4);

				persistentSupport.confirmarTransaccao();
			} catch(ExcepcaoPersistencia ex) {
				fail("testReadByGraduationTypeAndDocumentType: unexpected exception" + ex);
			}
		}

}