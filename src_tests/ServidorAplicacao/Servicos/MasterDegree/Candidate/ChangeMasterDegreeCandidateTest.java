/*
 * ChangeMasterDegreeCandidateTest.java
 *
 * Created on 07 de Dezembro de 2002, 17:49
 *
 * Tests :
 * 
 * - 1 : Change Master Degree Candidate Personal Information
 * - 2 : Change Master Degree Candidate Personal Information (Unexisting
 *       Candidate)
 */

/**
 *
 * Autores : 
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servicos.MasterDegree.Candidate;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.NotExecutedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

public class ChangeMasterDegreeCandidateTest extends TestCaseServicosCandidato {
    
    public ChangeMasterDegreeCandidateTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ChangeMasterDegreeCandidateTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    public void testAlterarCandidatoComPrivilegios() {
        System.out.println("- Test 1 : Change Master Degree Candidate Personal Information");
		IMasterDegreeCandidate tempCandidate1 = null;
		IMasterDegreeCandidate tempCandidate2 = null;
        
        try {
            suportePersistente.iniciarTransaccao();
            tempCandidate1 = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            tempCandidate2 = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand2");
            suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Erro no teste");
        }

        InfoMasterDegreeCandidate tempMasterDegreeCandidate = new InfoMasterDegreeCandidate(tempCandidate1);
        ISuportePersistente sp = null;
        
		tempMasterDegreeCandidate.setApplicationYear(tempCandidate2.getApplicationYear());
		tempMasterDegreeCandidate.setUsername(tempCandidate2.getUsername());
		tempMasterDegreeCandidate.setCandidateNumber(tempCandidate2.getCandidateNumber());
		tempMasterDegreeCandidate.setDegreeName(tempCandidate2.getDegree().getNome());
		tempMasterDegreeCandidate.setDegreeCode(tempCandidate2.getDegree().getSigla());


		Object args[] = new Object[1];
		args[0] = tempMasterDegreeCandidate;
		 
		
        try {
            gestor.executar(userView1, "ChangeMasterDegreeCandidate", args);
        } catch (Exception ex) {
            System.out.println("Service not Executed: " + ex);
        }   

     	IMasterDegreeCandidate masterDegreeCandidate = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            masterDegreeCandidate = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode(new Integer(2), new Integer(2003), "MIC");
            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            NotExecutedException newEx = new NotExecutedException("Persistence layer error");
            newEx.fillInStackTrace();
        }  
        
        assertNotNull(masterDegreeCandidate);
        // Test the Candidate
        assertTrue(masterDegreeCandidate.getIdentificationDocumentNumber().equals("112233"));
        
        assertEquals(masterDegreeCandidate.getIdentificationDocumentType(), new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
        assertTrue(masterDegreeCandidate.getIdentificationDocumentIssuePlace().equals("Lisboa"));
        
//        assertTrue(masterDegreeCandidate.getIdentificationDocumentIssueDate().toString().equals("2002-11-17"));
        assertTrue(masterDegreeCandidate.getName().equals("Nuno Nunes"));
        assertEquals(masterDegreeCandidate.getSex(), new Sexo(Sexo.MASCULINO));
        assertEquals(masterDegreeCandidate.getMaritalStatus(), new EstadoCivil(EstadoCivil.SOLTEIRO));
        
//        assertTrue(masterDegreeCandidate.getBirth().toString().equals("2000-12-10"));
        assertTrue(masterDegreeCandidate.getFatherName().equals("Manuel"));
        assertTrue(masterDegreeCandidate.getMotherName().equals("Maria"));
        assertTrue(masterDegreeCandidate.getNationality().equals("Portugal"));
        assertTrue(masterDegreeCandidate.getBirthPlaceParish().equals("Oeiras"));
        assertTrue(masterDegreeCandidate.getBirthPlaceMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidate.getBirthPlaceDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidate.getAddress().equals("Rua Nuno"));
        assertTrue(masterDegreeCandidate.getPlace().equals("Localidade Nuno"));
        assertTrue(masterDegreeCandidate.getPostCode().equals("2795-833"));
        assertTrue(masterDegreeCandidate.getAddressParish().equals("Queijas"));
        assertTrue(masterDegreeCandidate.getAddressMunicipality().equals("Oeiras"));
        assertTrue(masterDegreeCandidate.getAddressDistrict().equals("Lisboa"));
        assertTrue(masterDegreeCandidate.getTelephone().equals("11111"));
        assertTrue(masterDegreeCandidate.getMobilePhone().equals("33333"));
        assertTrue(masterDegreeCandidate.getEmail().equals("nmsn@rnl.ist.utl.pt"));
        assertTrue(masterDegreeCandidate.getWebSite().equals("www.nuno.com"));
        assertTrue(masterDegreeCandidate.getContributorNumber().equals("11111"));
        assertTrue(masterDegreeCandidate.getOccupation().equals("Estudante"));
        assertTrue(masterDegreeCandidate.getMajorDegree().equals("LEIC"));
        assertTrue(masterDegreeCandidate.getUsername().equals("Cand2"));
        assertTrue(masterDegreeCandidate.getPassword().equals("Pass1"));
        assertTrue(masterDegreeCandidate.getCandidateNumber().equals(new Integer(2)));
        assertTrue(masterDegreeCandidate.getApplicationYear().equals(new Integer(2003)));
        assertEquals(masterDegreeCandidate.getSpecialization(), new Specialization(Specialization.MESTRADO));
        assertTrue(masterDegreeCandidate.getMajorDegreeSchool().equals("IST"));
        assertTrue(masterDegreeCandidate.getMajorDegreeYear().equals(new Integer(2000)));
//        assertTrue(masterDegreeCandidate.getAverage().equals(new Double(11)));

   }

           
   public void testAlterarCandidatoInexistente() {
        System.out.println("- Test 2 : Change Master Degree Candidate Personal Information (Unexisting Candidate)");
		IMasterDegreeCandidate tempCandidate1 = null;
		IMasterDegreeCandidate tempCandidate2 = null;
        
        try {
            suportePersistente.iniciarTransaccao();
            tempCandidate1 = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            tempCandidate2 = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand2");
            suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Erro no teste");
        }

        InfoMasterDegreeCandidate masterDegreeCandidate = new InfoMasterDegreeCandidate(tempCandidate1);
        
		masterDegreeCandidate.setApplicationYear(tempCandidate2.getApplicationYear());
		masterDegreeCandidate.setUsername("Inexistente");
		masterDegreeCandidate.setCandidateNumber(new Integer(1000));
		masterDegreeCandidate.setDegreeName(tempCandidate2.getDegree().getNome());
		masterDegreeCandidate.setDegreeCode(tempCandidate2.getDegree().getSigla());

		Object args[] = new Object[1];
		args[0] = masterDegreeCandidate;

		Object result = null;        
        try {
            result = gestor.executar(userView1, "ChangeMasterDegreeCandidate", args);
            fail("Espected Error !!!");
        } catch (Exception ex) {
            // All Is OK
        }   
        assertNull(result);
   }
    
}

