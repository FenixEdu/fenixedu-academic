/*
 * MasterDegreeCandidateOJBTest.java
 * NetBeans JUnit based test
 *
 * Created on 17 de Outubro de 2002, 11:37
 * 
 * Testes :
 *  - 1 : Read existing MasterDegreeCandidate by username
 *  - 2 : Read non-existing MasterDegreeCandidate by username
 *  - 3 : Write existing MasterDegreeCandidate 
 *  - 4 : Write non-existing MasterDegreeCandidate 
 *  - 5 : Delete existing MasterDegreeCandidate 
 *  - 6 : Delete non-existing MasterDegreeCandidate 
 *  - 7 : Delete all MasterDegreeCandidates 
 *  - 8 : Read existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode
 *  - 9 : Read non-existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode
 */
 
/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorPersistente.OJB;

import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICountry;
import Dominio.ICurso;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

public class MasterDegreeCandidateOJBTest extends TestCaseOJB {
    
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
	IPersistentCountry persistentCountry = null;
	ICursoPersistente persistentDegree = null;
	IPersistentExecutionYear persistentExecutionYear = null;
    
    public MasterDegreeCandidateOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        System.out.println("Beginning of test from class MasterDegreeCandidateOJB" + "\n");
        junit.textui.TestRunner.run(suite());
        System.out.println("End of test from class MasterDegreeCandidateOJB" + "\n");       
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MasterDegreeCandidateOJBTest.class);
        
        return suite;
    }
    
    protected void setUp(){
        super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentMasterDegreeCandidate = persistentSupport.getIPersistentMasterDegreeCandidate();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentCountry = persistentSupport.getIPersistentCountry();
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
    }
    
    protected void tearDown(){
        super.tearDown();
    }
    
    public void testReadExistingMasterDegreeCandidate() {
        System.out.println("- Test 1 : Read Existing Master Degree Candidate");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        // Testing the obtained candidate
        
        assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentNumber().equals("112233"));
        assertEquals(masterDegreeCandidateTemp.getIdentificationDocumentType(), new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
        assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentIssuePlace().equals("Lisboa"));
        
		assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentIssueDate().toString().equals("2002-11-17"));
		assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentExpirationDate().toString().equals("2004-11-17"));
        assertTrue(masterDegreeCandidateTemp.getName().equals("Nuno Nunes"));
        assertEquals(masterDegreeCandidateTemp.getSex(), new Sexo(Sexo.MASCULINO));
        assertEquals(masterDegreeCandidateTemp.getMaritalStatus(), new EstadoCivil(EstadoCivil.SOLTEIRO));
        
        assertTrue(masterDegreeCandidateTemp.getBirth().toString().equals("2000-12-10"));
        assertTrue(masterDegreeCandidateTemp.getFatherName().equals("Manuel"));
        assertTrue(masterDegreeCandidateTemp.getMotherName().equals("Maria"));
        assertTrue(masterDegreeCandidateTemp.getNationality().getNationality().equals("Portuguesa"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceParish().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidateTemp.getAddress().equals("Rua Nuno"));
        assertTrue(masterDegreeCandidateTemp.getPlace().equals("Localidade Nuno"));
        assertTrue(masterDegreeCandidateTemp.getPostCode().equals("2795-833"));
        assertTrue(masterDegreeCandidateTemp.getAddressParish().equals("Queijas"));
        assertTrue(masterDegreeCandidateTemp.getAddressMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getAddressDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidateTemp.getTelephone().equals("11111"));
        assertTrue(masterDegreeCandidateTemp.getMobilePhone().equals("33333"));
        assertTrue(masterDegreeCandidateTemp.getEmail().equals("nmsn@rnl.ist.utl.pt"));
        assertTrue(masterDegreeCandidateTemp.getWebSite().equals("www.nuno.com"));
        assertTrue(masterDegreeCandidateTemp.getContributorNumber().equals("11111"));
        assertTrue(masterDegreeCandidateTemp.getOccupation().equals("Estudante"));
        assertTrue(masterDegreeCandidateTemp.getMajorDegree().equals("LEIC"));
        assertTrue(masterDegreeCandidateTemp.getUsername().equals("Cand1"));
        assertTrue(masterDegreeCandidateTemp.getPassword().equals("Pass1"));
        assertTrue(masterDegreeCandidateTemp.getCandidateNumber().equals(new Integer(1)));
        assertTrue(masterDegreeCandidateTemp.getExecutionYear().getYear().equals("2003/2004"));
        assertEquals(masterDegreeCandidateTemp.getSpecialization(), new Specialization(Specialization.MESTRADO));
        assertTrue(masterDegreeCandidateTemp.getMajorDegreeSchool().equals("IST"));
        assertTrue(masterDegreeCandidateTemp.getMajorDegreeYear().equals(new Integer(2000)));
        assertTrue(masterDegreeCandidateTemp.getAverage().equals(new Double(11)));

        assertEquals(masterDegreeCandidateTemp.getSituations().size(), 2);

    }
    
    public void testReadNonExistingMasterDegreeCandidate() {
        System.out.println("- Test 2 : Read non-existing Master Degree Candidate");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand_Inex");
            assertNull(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    }
 
    
    public void testWriteExistingMasterDegreeCandidate() {
        System.out.println("- Test 3 : Write Existing Master Degree Candidate");    

        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);

        ICurso masterDegreeTemp = null;
		ICountry countryTemp = null;
        IExecutionYear executionYear = null;
        try {
            persistentSupport.iniciarTransaccao();
	        masterDegreeTemp = persistentDegree.readBySigla("LEIC");
	        assertNotNull(masterDegreeTemp);
	        countryTemp = persistentCountry.readCountryByName("Portugal");
	        assertNotNull(countryTemp);
	        executionYear = persistentExecutionYear.readExecutionYearByName("2003/2004");
	        assertNotNull(executionYear);
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }


        IMasterDegreeCandidate candidateTemp = new MasterDegreeCandidate("1", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "1", data.getTime(), "Nome", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.SOLTEIRO), data.getTime(), "1", "1", countryTemp, "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "Cand1", "Pass1", new Integer(1), executionYear, new Specialization(Specialization.MESTRADO), "1", new Integer(1), new Double(1.0), masterDegreeTemp, countryTemp, data.getTime());
        try {
            persistentSupport.iniciarTransaccao();
            persistentMasterDegreeCandidate.writeMasterDegreeCandidate(candidateTemp);
            persistentSupport.confirmarTransaccao();
            fail("testWriteExistingMasterDegreeCandidate");
            
		} catch (ExistingPersistentException eex) {
			// all is ok
			System.out.println("Caught ExistingPersistentException" + eex);
		} catch (ExcepcaoPersistencia ex) {
		  fail("Caught ExcepcaoPersistencia" + ex);
		}
    }
    
    
    public void testWriteNonExistingMasterDegreeCandidate() {
        System.out.println("- Test 4 : Write NonExisting Master Degree Candidate");    


        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);

        ICurso masterDegreeTemp = null;
		ICountry countryTemp = null;
		IExecutionYear executionYear = null;
        try {
            persistentSupport.iniciarTransaccao();
	        masterDegreeTemp = persistentDegree.readBySigla("MIC");
	        assertNotNull(masterDegreeTemp);
	        countryTemp = persistentCountry.readCountryByName("Inglaterra");
	        assertNotNull(countryTemp);
			executionYear = persistentExecutionYear.readExecutionYearByName("2003/2004");
			assertNotNull(executionYear);

            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
		assertNotNull(masterDegreeTemp);
		assertNotNull(countryTemp);

        IMasterDegreeCandidate candidateTemp = new MasterDegreeCandidate("3", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "2", data.getTime(), "Nome2", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.SOLTEIRO), data.getTime(), "12", "12", countryTemp, "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "12", "Cand3", "Pass12", new Integer(12), executionYear, new Specialization(Specialization.MESTRADO), "12", new Integer(12), new Double(2.0), masterDegreeTemp, countryTemp, data.getTime());
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentMasterDegreeCandidate.writeMasterDegreeCandidate(candidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        candidateTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand3");
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        // Testing the obtained Candidate
        assertTrue(candidateTemp.getIdentificationDocumentNumber().equals("3"));
        assertEquals(candidateTemp.getIdentificationDocumentType(), new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
        assertTrue(candidateTemp.getIdentificationDocumentIssuePlace().equals("2"));

		// TODO : Uncomment asserts after updating from CVS
		//        Also use a temporary Date variable to compare values...
		//        because toString representation varys from Linux to Windows.
        //assertEquals("2002-11-17", candidateTemp.getIdentificationDocumentIssueDate().toString());
        assertTrue(candidateTemp.getName().equals("Nome2"));
        assertEquals(candidateTemp.getSex(), new Sexo(Sexo.MASCULINO));
        assertEquals(candidateTemp.getMaritalStatus(), new EstadoCivil(EstadoCivil.SOLTEIRO));
        //assertTrue(candidateTemp.getBirth().toString().equals("2002-11-17"));
        assertTrue(candidateTemp.getFatherName().equals("12"));
        assertTrue(candidateTemp.getMotherName().equals("12"));
        assertTrue(candidateTemp.getNationality().getNationality().equals("Inglesa"));
        assertTrue(candidateTemp.getBirthPlaceParish().equals("12"));
        assertTrue(candidateTemp.getBirthPlaceMunicipality().equals("12"));
        assertTrue(candidateTemp.getBirthPlaceDistrict().equals("12"));
        assertTrue(candidateTemp.getAddress().equals("12"));
        assertTrue(candidateTemp.getPlace().equals("12"));
        assertTrue(candidateTemp.getPostCode().equals("12"));
        assertTrue(candidateTemp.getAddressParish().equals("12"));
        assertTrue(candidateTemp.getAddressMunicipality().equals("12"));
        assertTrue(candidateTemp.getAddressDistrict().equals("12"));
        assertTrue(candidateTemp.getTelephone().equals("12"));
        assertTrue(candidateTemp.getMobilePhone().equals("12"));
        assertTrue(candidateTemp.getEmail().equals("12"));
        assertTrue(candidateTemp.getWebSite().equals("12"));
        assertTrue(candidateTemp.getContributorNumber().equals("12"));
        assertTrue(candidateTemp.getOccupation().equals("12"));
        assertTrue(candidateTemp.getMajorDegree().equals("12"));
        assertTrue(candidateTemp.getUsername().equals("Cand3"));
        assertTrue(candidateTemp.getPassword().equals("Pass12"));
        assertTrue(candidateTemp.getCandidateNumber().equals(new Integer(12)));
        assertTrue(candidateTemp.getExecutionYear().getYear().equals("2003/2004"));
        assertEquals(candidateTemp.getSpecialization(), new Specialization(Specialization.MESTRADO));
        assertTrue(candidateTemp.getMajorDegreeSchool().equals("12"));
        assertTrue(candidateTemp.getMajorDegreeYear().equals(new Integer(12)));
        assertTrue(candidateTemp.getAverage().equals(new Double(2.0)));

    } 
    
    
    public void testDeleteExistingMasterDegreeCandidate() {
        System.out.println("- Test 5 : Delete Existing Master Degree Candidate");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        try {
            persistentSupport.iniciarTransaccao();
			masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentMasterDegreeCandidate.delete(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        masterDegreeCandidateTemp = null;
        
        // test if it was really deleted
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            assertNull(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
    
    public void testDeleteNonExistingMasterDegreeCandidate() {
        System.out.println("- Test 6 : Delete NonExisting Master Degree Candidate");

        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        try {
            persistentSupport.iniciarTransaccao();
			masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }


        // Clean all
        try {
            persistentSupport.iniciarTransaccao();
            persistentMasterDegreeCandidate.deleteAll();
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentMasterDegreeCandidate.delete(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
    
   
    public void testDeleteAllMasterDegreeCandidates() {
        System.out.println("- Test 7 : Delete All Master Degree Candidates");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        try {
            persistentSupport.iniciarTransaccao();
            persistentMasterDegreeCandidate.deleteAll();
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        // Test if it was really deleted
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            assertNull(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
    
    
    public void testReadExistingMasterDegreeCandidate2() {
        System.out.println("- Test 8 : Read existing Master Degree Candidate by candidateNumber, applicationYear and masterDegreeCode");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(new Integer(1), "2003/2004", "MIC");
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        
        
        // Testing the obtained Candidate 
        assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentNumber().equals("112233"));
        assertEquals(masterDegreeCandidateTemp.getIdentificationDocumentType(), new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
        assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentIssuePlace().equals("Lisboa"));
        
        assertTrue(masterDegreeCandidateTemp.getIdentificationDocumentIssueDate().toString().equals("2002-11-17"));
        assertTrue(masterDegreeCandidateTemp.getName().equals("Nuno Nunes"));
        assertEquals(masterDegreeCandidateTemp.getSex(), new Sexo(Sexo.MASCULINO));
        assertEquals(masterDegreeCandidateTemp.getMaritalStatus(), new EstadoCivil(EstadoCivil.SOLTEIRO));
        
        assertTrue(masterDegreeCandidateTemp.getBirth().toString().equals("2000-12-10"));
        assertTrue(masterDegreeCandidateTemp.getFatherName().equals("Manuel"));
        assertTrue(masterDegreeCandidateTemp.getMotherName().equals("Maria"));
        assertTrue(masterDegreeCandidateTemp.getNationality().getNationality().equals("Portuguesa"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceParish().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getBirthPlaceDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidateTemp.getAddress().equals("Rua Nuno"));
        assertTrue(masterDegreeCandidateTemp.getPlace().equals("Localidade Nuno"));
        assertTrue(masterDegreeCandidateTemp.getPostCode().equals("2795-833"));
        assertTrue(masterDegreeCandidateTemp.getAddressParish().equals("Queijas"));
        assertTrue(masterDegreeCandidateTemp.getAddressMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidateTemp.getAddressDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidateTemp.getTelephone().equals("11111"));
        assertTrue(masterDegreeCandidateTemp.getMobilePhone().equals("33333"));
        assertTrue(masterDegreeCandidateTemp.getEmail().equals("nmsn@rnl.ist.utl.pt"));
        assertTrue(masterDegreeCandidateTemp.getWebSite().equals("www.nuno.com"));
        assertTrue(masterDegreeCandidateTemp.getContributorNumber().equals("11111"));
        assertTrue(masterDegreeCandidateTemp.getOccupation().equals("Estudante"));
        assertTrue(masterDegreeCandidateTemp.getMajorDegree().equals("LEIC"));
        assertTrue(masterDegreeCandidateTemp.getUsername().equals("Cand1"));
        assertTrue(masterDegreeCandidateTemp.getPassword().equals("Pass1"));
        assertTrue(masterDegreeCandidateTemp.getCandidateNumber().equals(new Integer(1)));
        assertTrue(masterDegreeCandidateTemp.getExecutionYear().getYear().equals("2003/2004"));
        assertEquals(masterDegreeCandidateTemp.getSpecialization(), new Specialization(Specialization.MESTRADO));
        assertTrue(masterDegreeCandidateTemp.getMajorDegreeSchool().equals("IST"));
        assertTrue(masterDegreeCandidateTemp.getMajorDegreeYear().equals(new Integer(2000)));
        assertTrue(masterDegreeCandidateTemp.getAverage().equals(new Double(11)));

    }

    public void testReadNonExistingMasterDegreeCandidate2() {
        System.out.println("- Test 9 : Read non existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode");
        IMasterDegreeCandidate masterDegreeCandidateTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            masterDegreeCandidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(new Integer(999), "2000", "555");
            assertNull(masterDegreeCandidateTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    }
    
    
} // End of test from Class MasterDegreeCandidateOJB
