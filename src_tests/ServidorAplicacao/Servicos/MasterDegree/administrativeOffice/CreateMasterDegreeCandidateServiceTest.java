
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice;

/**
 *
 * @author Nuno Nunes & Joana Mota 
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class CreateMasterDegreeCandidateServiceTest extends TestCaseServicos {

	public CreateMasterDegreeCandidateServiceTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(CreateMasterDegreeCandidateServiceTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
        
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadMasterDegreeCandidateNonExisting() {
		
//		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
//		infoMasterDegreeCandidate.setName("Nuno Nunes");
//		infoMasterDegreeCandidate.setIdentificationDocumentNumber("1234567788");
//		infoMasterDegreeCandidate.setInfoIdentificationDocumentType(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA_STRING);
//		infoMasterDegreeCandidate.setSpecialization(Specialization.MESTRADO_STRING);
//				
//		Object createCandidateArgs[] = new Object[2];
//		createCandidateArgs[0] = infoMasterDegreeCandidate;
//		createCandidateArgs[1] = "Licenciatura de Engenharia Electrotecnica e de Computadores";
//	
//		InfoMasterDegreeCandidate result = null; 
//		
//		try {
//			result = (InfoMasterDegreeCandidate) _gestor.executar(_userView, "CreateMasterDegreeCandidate", createCandidateArgs);
//			assertNotNull(result);
//		} catch (Exception ex) {
//			System.out.println("Service Not Executed: " + ex);
//		} 
//		
//		assertEquals(infoMasterDegreeCandidate.getName(), result.getName());
//		assertEquals(infoMasterDegreeCandidate.getIdentificationDocumentNumber(), result.getIdentificationDocumentNumber());
//		assertEquals(infoMasterDegreeCandidate.getInfoIdentificationDocumentType(), result.getInfoIdentificationDocumentType());
//		assertEquals(infoMasterDegreeCandidate.getSpecialization(), result.getSpecialization());
//		assertEquals(result.getCandidateNumber(), new Integer(3));
		
	}
	
	public void testReadMasterDegreeCandidateExisting() {
		
//		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
//		infoMasterDegreeCandidate.setName("Nuno Nunes");
//		infoMasterDegreeCandidate.setIdentificationDocumentNumber("112233");
//		infoMasterDegreeCandidate.setInfoIdentificationDocumentType(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING);
//		infoMasterDegreeCandidate.setSpecialization(Specialization.MESTRADO_STRING);
//				
//		Object createCandidateArgs[] = new Object[2];
//		createCandidateArgs[0] = infoMasterDegreeCandidate;
//		createCandidateArgs[1] = "Licenciatura de Engenharia Electrotecnica e de Computadores";
//	
//		InfoMasterDegreeCandidate result = null; 
//		
//		try {
//			result = (InfoMasterDegreeCandidate) _gestor.executar(_userView, "CreateMasterDegreeCandidate", createCandidateArgs);
//		} catch (ExistingServiceException ex) {
//			System.out.println("Caught expected Error");
//		} catch (Exception ex) {
//			fail("Error in testReadMasterDegreeCandidateExisting ");
//		} 
//				
	}

}