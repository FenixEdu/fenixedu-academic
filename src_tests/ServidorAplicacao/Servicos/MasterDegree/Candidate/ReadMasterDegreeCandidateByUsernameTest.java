/*
 * ReadMasterDegreeCandidateByUsernameTest.java
 *
 * Created on 06 de Dezembro de 2002, 18:51
 *
 * Tests :
 * 
 * - 1 : Read an existing Master Degree Candidate
 * - 2 : Read an non existing Master Degree Candidate
 * 
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
import ServidorAplicacao.Servico.UserView;
import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

public class ReadMasterDegreeCandidateByUsernameTest extends TestCaseServicosCandidato {
    
    public ReadMasterDegreeCandidateByUsernameTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ReadMasterDegreeCandidateByUsernameTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    public void testReadMasterDegreeCandidateExisting() {
        System.out.println("- Test 1 : Read an existing Master Degree Candidate");
        InfoMasterDegreeCandidate candidateTemp = null;

		Object args[] = new Object[1];
		args[0] = userView1;
		
		
        try {
            candidateTemp = (InfoMasterDegreeCandidate) gestor.executar(userView1, "ReadMasterDegreeCandidateByUsername", args);
        } catch (Exception ex) {
            System.out.println("Service Not Executed: " + ex);
        }   
        
        
        assertNotNull(candidateTemp);
        // Test the candidate
        assertTrue(candidateTemp.getIdentificationDocumentNumber().equals("112233"));
        assertTrue(candidateTemp.getIdentificationDocumentType().equals(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_STRING));
        assertTrue(candidateTemp.getIdentificationDocumentIssuePlace().equals("Lisboa"));
        assertTrue(candidateTemp.getIdentificationDocumentIssueDate().toString().equals("2002-11-17"));

        assertTrue(candidateTemp.getName().equals("Nuno Nunes"));
        assertTrue(candidateTemp.getSex().equals(Sexo.MASCULINO_STRING));
        assertTrue(candidateTemp.getMaritalStatus().equals(EstadoCivil.SOLTEIRO_STRING));
        
        assertTrue(candidateTemp.getBirth().toString().equals("2000-12-10"));
        assertTrue(candidateTemp.getFatherName().equals("Manuel"));
        assertTrue(candidateTemp.getMotherName().equals("Maria"));
        assertTrue(candidateTemp.getNationality().equals("Portugal"));
        assertTrue(candidateTemp.getBirthPlaceParish().equals("Oeiras"));
        assertTrue(candidateTemp.getBirthPlaceMunicipality().equals("Oeiras"));
        assertTrue(candidateTemp.getBirthPlaceDistrict().equals("Lisboa"));
        assertTrue(candidateTemp.getAddress().equals("Rua Nuno"));
        assertTrue(candidateTemp.getPlace().equals("Localidade Nuno"));
        assertTrue(candidateTemp.getPostCode().equals("2795-833"));
        assertTrue(candidateTemp.getAddressParish().equals("Queijas"));
        assertTrue(candidateTemp.getAddressMunicipality().equals("Oeiras"));
        assertTrue(candidateTemp.getAddressDistrict().equals("Lisboa"));
        assertTrue(candidateTemp.getTelephone().equals("11111"));
        assertTrue(candidateTemp.getMobilePhone().equals("33333"));
        assertTrue(candidateTemp.getEmail().equals("nmsn@rnl.ist.utl.pt"));
        assertTrue(candidateTemp.getWebSite().equals("www.nuno.com"));
        assertTrue(candidateTemp.getContributorNumber().equals("11111"));
        assertTrue(candidateTemp.getOccupation().equals("Estudante"));
        assertTrue(candidateTemp.getMajorDegree().equals("LEIC"));
        assertTrue(candidateTemp.getUsername().equals("Cand1"));
        assertTrue(candidateTemp.getPassword().equals("Pass1"));
        assertTrue(candidateTemp.getCandidateNumber().equals(new Integer(1)));
        assertTrue(candidateTemp.getApplicationYear().equals(new Integer(2002)));
        assertTrue(candidateTemp.getSpecialization().equals(Specialization.MESTRADO_STRING));
        assertTrue(candidateTemp.getMajorDegreeSchool().equals("IST"));
        assertTrue(candidateTemp.getMajorDegreeYear().equals(new Integer(2000)));
        assertTrue(candidateTemp.getAverage().equals(new Double(11)));

   }

        
   public void testReadMasterDegreeCandidateNonExisting() {
        System.out.println("- Test 2 : Read an non existing Master Degree Candidate");
        IMasterDegreeCandidate candidatoMestradoTemp = null;
        UserView userViewTemp = new UserView("Desc", null); 
		
		Object args[] = new Object[1];
		args[0] = userViewTemp;
        
        try {
            candidatoMestradoTemp = (IMasterDegreeCandidate) gestor.executar(userView1, "ReadMasterDegreeCandidateByUsername", args);
            fail("Espected Error");
        } catch (Exception ex) {
            // All is OK
        }   
        assertNull(candidatoMestradoTemp);
   }
    
}

