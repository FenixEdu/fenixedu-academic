/*
 * CandidateAuthenticationTest.java
 *
 * Created on 06 de Dezembro de 2002, 13:15
 *
 *
 * Tests:
 * 1 - Authenticate a Master Degree Candidate With a correct password
 * 2 - Authenticate a Master Degree Candidate With a incorrect password
 * 3 - Authenticate a Master Degree Candidate With a incorrect username
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

import ServidorAplicacao.IUserView;


public class CandidateAuthenticationTest extends TestCaseServicosCandidato {
    
    public CandidateAuthenticationTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CandidateAuthenticationTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    public void testAuthenticateCandidatePasswordCorrect() {
        System.out.println("- Test 1 : Authenticate a Master Degree Candidate With a correct password");
        IUserView userView = null;
         
        Object argsAutenticacao[] = { "Cand1", "Pass1" };
        
        try {
            userView = (IUserView) gestor.executar(null, "CandidateAuthentication", argsAutenticacao);
        } catch (Exception ex) {
            System.out.println("Serviço não executado: " + ex);
        }   
        assertNotNull(userView);
   }

   public void testAuthenticateCandidatePasswordIncorrect() {
        System.out.println("- Test 2 : Authenticate a Master Degree Candidate With a incorrect password");
        IUserView userView = null;
         
        String argsAutenticacao[] = { "Cand1", "Incorrecta" };
        
        try {
            userView = (IUserView) gestor.executar(null, "CandidateAuthentication", argsAutenticacao);
            fail("Erro esperado na Autenticacao");
        } catch (Exception ex) {
            // All is OK
        }   
        assertNull(userView);
   }

        
   public void testAuthenticateCandidateUsernameIncorrect() {
        System.out.println("- Test 3 : Authenticate a Master Degree Candidate With a incorrect username");
        IUserView userView = null;
         
        String argsAutenticacao[] = { new String("Rito"), new String("silva") };
        
        try {
            userView = (IUserView) gestor.executar(null, "CandidateAuthentication", argsAutenticacao);
            fail("Erro esperado na Autenticacao");
        } catch (Exception ex) {
            // All is OK
        }   
        assertNull(userView);
   }

    
}

