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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoRole;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.Servico.UserView;
import Util.RoleType;
import Util.SituationName;

public class ReadActiveCandidateSituationTest extends TestCaseServicosCandidato {
    
    public ReadActiveCandidateSituationTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ReadActiveCandidateSituationTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }

    public void testReadMasterDegreeCandidateExisting() {
        System.out.println("- Test 1 : Read an Active Master Degree Situation");
        
        UserView userView = this.getUserViewToBeTested("nmsn", true);
		Object args[] = new Object[1];
		
		args[0] = userView;

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
		
        try {
            infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) gestor.executar(userView, "ReadActiveCandidateSituation", args);
        } catch (Exception ex) {
            System.out.println("Service Not Executed: " + ex);
        }   
        assertNotNull(infoMasterDegreeCandidate);
        assertEquals("nmsn", infoMasterDegreeCandidate.getInfoPerson().getUsername());
		assertEquals(infoMasterDegreeCandidate.getInfoCandidateSituation().getRemarks(), "Nothing");
		assertEquals(infoMasterDegreeCandidate.getInfoCandidateSituation().getSituation(), SituationName.ADMITIDO_STRING);

		userView = this.getUserViewToBeTested("jccm", true);
		args[0] = userView;

		infoMasterDegreeCandidate = null;
		
		try {
			infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) gestor.executar(userView, "ReadActiveCandidateSituation", args);
		} catch (Exception ex) {
			System.out.println("Service Not Executed: " + ex);
		}   
		assertNotNull(infoMasterDegreeCandidate);
		assertEquals("jccm", infoMasterDegreeCandidate.getInfoPerson().getUsername());
		assertEquals(infoMasterDegreeCandidate.getInfoCandidateSituation().getRemarks(), "Nothing");
		assertEquals(infoMasterDegreeCandidate.getInfoCandidateSituation().getSituation(), SituationName.PENDENTE_STRING);

   }

   public void testReadMasterDegreeCandidateExistingWithoutRole() {
	   System.out.println("- Test 2 : Read an Active Master Degree Situation without Role");
        
	   UserView userView = this.getUserViewToBeTested("nmsn", false);
	   Object args[] = new Object[1];
		
	   args[0] = userView;

	   InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

		try {
			infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) gestor.executar(userView, "ReadActiveCandidateSituation", args);
		} catch (FenixServiceException ex) {
			// All is OK
	    } catch (Exception ex) {
		   fail("Error Reading without Role");
	    }
		assertNull(infoMasterDegreeCandidate);   
   }

   private UserView getUserViewToBeTested(String username, boolean withRole) {
	   Collection roles = new ArrayList();
	   InfoRole infoRole = new InfoRole();
	   if (withRole)
	   	infoRole.setRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
	   roles.add(infoRole);
	   UserView userView = new UserView(username, roles);
	   return userView;
   }

      	
}

