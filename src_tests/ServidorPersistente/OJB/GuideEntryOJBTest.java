
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.GuideEntry;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPersistentGuideEntry;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.DocumentType;
import Util.GraduationType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GuideEntryOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentGuideEntry persistentGuideEntry = null;
	IPersistentGuide persistentGuide = null;
	
	public GuideEntryOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(GuideEntryOJBTest.class);
        
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
		persistentGuideEntry = persistentSupport.getIPersistentGuideEntry();
		persistentGuide = persistentSupport.getIPersistentGuide();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadAll() {
		System.out.println("Test 1 - Read All Guide Entries");        

		try {
			persistentSupport.iniciarTransaccao();
			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);
			List result = persistentGuideEntry.readByGuide(guide);
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 3);
			
			IGuideEntry guideEntry = persistentGuideEntry.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide, GraduationType.MAJOR_DEGREE_TYPE, 
									 DocumentType.CERTIFICATE_OF_DEGREE_TYPE, "Conclusão");
			assertNotNull(guideEntry);
			assertEquals(guideEntry.getGuide(), guide);
			assertEquals(guideEntry.getGraduationType(), GraduationType.MAJOR_DEGREE_TYPE);
			assertEquals(guideEntry.getDocumentType(), DocumentType.CERTIFICATE_OF_DEGREE_TYPE);
			assertEquals(guideEntry.getDescription(), "Conclusão");
			assertEquals(guideEntry.getPrice(), new Double(10.02));
			assertEquals(guideEntry.getQuantity(), new Integer(1));
			
			guide = persistentGuide.readByNumberAndYear(new Integer(2), new Integer(2003));
			assertNotNull(guide);
			result = persistentGuideEntry.readByGuide(guide);
			assertTrue(!result.isEmpty());
			assertEquals(result.size(), 2);
			
		
			
			
			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadAllGuideEntries: unexpected exception");
		}
	}
	
	public void testDelete() {
			System.out.println("Test 2 - Delete");        

			try {
				persistentSupport.iniciarTransaccao();
				IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
				assertNotNull(guide);
			
				IGuideEntry guideEntry = persistentGuideEntry.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide, GraduationType.MAJOR_DEGREE_TYPE, 
										 DocumentType.CERTIFICATE_OF_DEGREE_TYPE, "Conclusão");
				assertNotNull(guideEntry);
				persistentGuideEntry.delete(guideEntry);
								
				persistentSupport.confirmarTransaccao();
				
				persistentSupport.iniciarTransaccao();
				guideEntry = persistentGuideEntry.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide, GraduationType.MAJOR_DEGREE_TYPE, 
														 DocumentType.CERTIFICATE_OF_DEGREE_TYPE, "Conclusão");
				assertNull(guideEntry);
				persistentSupport.confirmarTransaccao();
				
			} catch(ExcepcaoPersistencia ex) {
				fail("testReadAllGuideEntries: unexpected exception");
			}
		}

	public void testWriteExisting() {
			System.out.println("Test 3 - Write Existing GuideEntry");        

			try {
				persistentSupport.iniciarTransaccao();
				IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
				assertNotNull(guide);
				
				IGuideEntry guideEntry = new GuideEntry();
				guideEntry.setDescription("Conclusão");
				guideEntry.setGuide(guide);
				guideEntry.setGraduationType(GraduationType.MAJOR_DEGREE_TYPE);
				guideEntry.setDocumentType(DocumentType.CERTIFICATE_OF_DEGREE_TYPE);
					
				persistentGuide.write(guide);
						
				persistentSupport.confirmarTransaccao();
							
				
			} catch(ExistingPersistentException ex) {
				// All is OK
			} catch(ExcepcaoPersistencia ex) {
				fail("testWriteExistingGuideEntry: unexpected exception" + ex);
			}
		}

	public void testWriteNonExisting() {
			System.out.println("Test 4 - Write Non-Existing GuideEntry");        

			try {
				persistentSupport.iniciarTransaccao();
				IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
				assertNotNull(guide);
			
				IGuideEntry guideEntry = new GuideEntry();
				guideEntry.setDescription("Conclusão");
				guideEntry.setGuide(guide);
				guideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
				guideEntry.setDocumentType(DocumentType.CERTIFICATE_OF_DEGREE_TYPE);
				guideEntry.setPrice(new Double(10.02));
				guideEntry.setQuantity(new Integer(1));
				persistentGuideEntry.write(guideEntry);
				persistentSupport.confirmarTransaccao();
						
				persistentSupport.iniciarTransaccao();
				IGuideEntry guideEntryTemp = persistentGuideEntry.readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide, GraduationType.MASTER_DEGREE_TYPE, 
																		 DocumentType.CERTIFICATE_OF_DEGREE_TYPE, "Conclusão");
				assertNotNull(guideEntryTemp);
				assertEquals(guideEntry, guideEntryTemp);
				persistentSupport.confirmarTransaccao();
				
			} catch(ExcepcaoPersistencia ex) {
				fail("testWriteNonExistingGuideEntry: unexpected exception" + ex);
			}
			}


}