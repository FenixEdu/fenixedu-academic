/*
 * ChangeApplicationInfoTest.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class ChangeApplicationInfoTest extends TestCaseServicosCandidato {
    
    public ChangeApplicationInfoTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ChangeApplicationInfoTest.class);
        
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
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);
		

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");
		
		infoMasterDegreeCandidate.setAverage(new Double(100));
		infoMasterDegreeCandidate.setMajorDegree("Curso");
		infoMasterDegreeCandidate.setMajorDegreeSchool("Escola");
		infoMasterDegreeCandidate.setMajorDegreeYear(new Integer(1));
		
		Object[] args = { infoMasterDegreeCandidate }; 
		
        try {
            ServiceManagerServiceFactory.executeService(userView, "ChangeApplicationInfo", args);
        } catch (Exception ex) {
            System.out.println("Service not Executed: " + ex);
        }   

		// Check the alteration

		InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");
		
		assertEquals(infoMasterDegreeCandidate.getAverage(), newInfoMasterDegreeCandidate.getAverage());
		assertEquals(infoMasterDegreeCandidate.getMajorDegree(), newInfoMasterDegreeCandidate.getMajorDegree());
		assertEquals(infoMasterDegreeCandidate.getMajorDegreeSchool(), newInfoMasterDegreeCandidate.getMajorDegreeSchool());
		assertEquals(infoMasterDegreeCandidate.getMajorDegreeYear(), newInfoMasterDegreeCandidate.getMajorDegreeYear());

   }
   public void testReadMasterDegreeCandidateExistingWithoutRole() {
	   System.out.println("- Test 2 : Change Application Info without Role");
        
	   UserView userView = this.getUserViewToBeTested("nmsn", false);
	   
		
	   InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;

		try {
			infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory.executeService(userView, "ChangeApplicationInfo", null);
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
	   if (withRole) infoRole.setRoleType(RoleType.MASTER_DEGREE_CANDIDATE);
	   else infoRole.setRoleType(RoleType.PERSON);
	   
	   roles.add(infoRole);
	   UserView userView = new UserView(username, roles);
	   return userView;
   }


   private InfoMasterDegreeCandidate readMasterDegreeCandidate(String username) {
	   ISuportePersistente sp = null;
	   List result = null; 
	   try {
		   sp = SuportePersistenteOJB.getInstance();

		   sp.iniciarTransaccao();
		   result = sp.getIPersistentMasterDegreeCandidate().readMasterDegreeCandidatesByUsername(username);

		   sp.confirmarTransaccao();
	   } catch (ExcepcaoPersistencia e) {
		   try {
			   sp.cancelarTransaccao();
		   } catch (ExcepcaoPersistencia ex) {
			   //ignored
		   }
		   e.printStackTrace();
		   fail("Error reading person to test equal password!");
	   }
		
	   return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate((IMasterDegreeCandidate) result.get(0));
   }
}

