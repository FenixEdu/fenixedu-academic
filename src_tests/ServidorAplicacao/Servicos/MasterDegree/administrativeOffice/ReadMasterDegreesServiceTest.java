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

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servicos.TestCaseServicos;

public class ReadMasterDegreesServiceTest extends TestCaseServicos {
    
    public ReadMasterDegreesServiceTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ReadMasterDegreesServiceTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
        
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    public void testReadMasterDegreeCandidateExisting() {
        System.out.println("- Test 1 : Read Master Degrees");
        List masterDegrees = null;

        try {
            masterDegrees = (List) _gestor.executar(this.getUserViewToBeTested(), "ReadMasterDegrees", null);
        } catch (Exception ex) {
            System.out.println("Service Not Executed: " + ex);
        }   
        
        
        assertNotNull(masterDegrees);
        assertTrue(!masterDegrees.isEmpty());
        assertEquals(masterDegrees.size(), 1);

   }
   
   private UserView getUserViewToBeTested() {
	   UserView userView = new UserView("nmsn", null);
	   return userView;
   }
    
}

