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

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import Util.RoleType;

public class ReadPersonCandidatesTest extends TestCaseServicosCandidato {
    
    public ReadPersonCandidatesTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ReadPersonCandidatesTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
   
    public void testReadPersonCandidatesWithRole() {
	    System.out.println("- Test 1 : Read Existing Person Candidates with Role");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);
		
		Object[] args = { userView }; 
		
		List result = null;
	    try {
	        result = (List) gestor.executar(userView, "ReadPersonCandidates", args);
	    } catch (Exception ex) {
	        System.out.println("Service not Executed: " + ex);
	    }   
	    
	    assertEquals(result.size(), 1);
	    
	    
		userView = this.getUserViewToBeTested("desc", true);
		
		Object[] args2 = { userView }; 
	
		result = null;
		try {
			result = (List) gestor.executar(userView, "ReadPersonCandidates", args2);
		} catch (Exception ex) {
			System.out.println("Service not Executed: " + ex);
		}   
    
		assertEquals(result.size(), 0);
	    
   }
   
   public void testReadPersonCandidatesWithoutRole() {
	  System.out.println("- Test 2 : Read Existing Person Candidates without Role");
	
	  UserView userView = this.getUserViewToBeTested("nmsn", false);
	
	  Object[] args = { userView }; 
	
	  try {
		 gestor.executar(userView, "ReadPersonCandidates", args);
	  } catch (FenixServiceException ex) {
		  // All is OK
	  } catch (Exception ex) {
		  fail("Error !");
	  }   
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
}

