/*
 * CandidateSituationOJBTest.java
 * NetBeans JUnit based test
 *
 * Created on 10 de Novembro de 2002, 15:18
 *
 * Testes :
 *  - 1 : Read existing Candidate Situation
 *  - 2 : Read non-existing Candidate Situation
 *  - 3 : Write existing Candidate Situation
 *  - 4 : Write non-existing Candidate Situation
 *  - 5 : Delete existing Candidate Situation
 *  - 6 : Delete non-existing Candidate Situation
 *  - 7 : Delete all Candidate Situations
 *  - 8 : Test equal Candidate Situations
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
import Dominio.CandidateSituation;
import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCandidateSituation;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import Util.CandidateSituationValidation;
import Util.SituationName;

public class CandidateSituationOJBTest extends TestCaseOJB {
    
	SuportePersistenteOJB persistentSupport = null;
	IPersistentCandidateSituation persistentCandidateSituation = null; 
	IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
    
    public CandidateSituationOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        System.out.println("Beginning of test from class CandidateSituationOJB \n");
        junit.textui.TestRunner.run(suite());
        System.out.println("End of test from class CandidateSituationOJB \n");       
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CandidateSituationOJBTest.class);
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
		persistentCandidateSituation = persistentSupport.getIPersistentCandidateSituation();
		persistentMasterDegreeCandidate = persistentSupport.getIPersistentMasterDegreeCandidate();
    }
    
    protected void tearDown(){
        super.tearDown();
    }
    
    public void testReadExistingCandidateSituation() {
        System.out.println("- Test 1 : Read existing Candidate Situation");
        ICandidateSituation candidateSituationTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(1), "LEEC", "2003/2004");
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        // Testing the obtained Candidate Situation
        
        assertNotNull(candidateSituationTemp);
        assertTrue(candidateSituationTemp.getDate().toString().equals("2002-11-20"));
        assertTrue(candidateSituationTemp.getRemarks().equals("Nothing"));
        assertTrue(candidateSituationTemp.getValidation().equals(new CandidateSituationValidation(CandidateSituationValidation.ACTIVE)));
        
    }
    
    public void testReadNonExistingCandidateSituation() {
        System.out.println("- Test 2 : Read non-existing Candidate Situation");
        ICandidateSituation candidateSituationTemp = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(3), "2", "2003/2004");
            assertNull(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    }
 
    
    public void testWriteExistingCandidateSituation() {
        System.out.println("- Test 3 : Write Existing Candidate Situation");
        
        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);

		IMasterDegreeCandidate candidateTemp = null;        
        try {
            persistentSupport.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        
		ICandidateSituation candidateSituation = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.EXTRAORDINARIO));

        try {
            persistentSupport.iniciarTransaccao();
            persistentCandidateSituation.writeCandidateSituation(candidateSituation);
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    }
    
    
    public void testWriteNonExistingCandidateSituation() {
        System.out.println("- Test 4 : Write Non-Existing Candidate Situation");    

        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);

		IMasterDegreeCandidate candidateTemp = null;        
        try {
            persistentSupport.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }

		ICandidateSituation candidateSituation1 = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.PENDENTE));        

        try {
            persistentSupport.iniciarTransaccao();
            persistentCandidateSituation.writeCandidateSituation(candidateSituation1);
            persistentSupport.confirmarTransaccao();
            fail("Expected error");
        } catch (ExcepcaoPersistencia ex) {
			// All is OK
        }
    } 
    
    public void testDeleteExistingCandidateSituation() {
        System.out.println("- Test 5 : Delete Existing Candidate Situation");

        ICandidateSituation candidateSituationTemp = null;
        try {
            persistentSupport.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(1), "LEEC", "2003/2004");
            assertNotNull(candidateSituationTemp);
            persistentCandidateSituation.delete(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
         //Test if it was really deleted
        try {
            persistentSupport.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(1), "LEEC", "2003/2004");
            
            assertNull(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
    
    public void testDeleteNonExistingCandidateSituation() {
        System.out.println("- Test 6 : Delete Non Existing Candidate Situation");

        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);
	
		ICandidateSituation candidateSituationTemp = null;

        try {
            persistentSupport.iniciarTransaccao();
	        candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(1), "LEEC", "2003/2004");
	        assertNotNull(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }

        try {
            persistentSupport.iniciarTransaccao();
            persistentCandidateSituation.deleteAll();
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }


        try {
            persistentSupport.iniciarTransaccao();
            persistentCandidateSituation.delete(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
   
    public void testDeleteAllCandidateSituations() {
        System.out.println("- Test 7 : Delete All Candidate Situations");
        ICandidateSituation candidateSituationTemp = null;
        try {
            persistentSupport.iniciarTransaccao();
            persistentCandidateSituation.deleteAll();
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
         //Test if it was really deleted
        try {
            persistentSupport.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readActiveCandidateSituation(new Integer(1), "MIC", "2003/2004");
            assertNull(candidateSituationTemp);
            persistentSupport.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
    
    public  void testEqualCandidateSituations() {
        System.out.println("- Test 8 : Test if two Candidate Situations are equal");

        Calendar data = Calendar.getInstance();
        data.set(2002, Calendar.NOVEMBER, 17);

		Calendar data2 = Calendar.getInstance();
        data2.set(2002, 12, 18);
        
		IMasterDegreeCandidate candidateTemp = null;        
        try {
            persistentSupport.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            persistentSupport.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }

		ICandidateSituation candidateSituation1 = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.EXTRAORDINARIO));        
		ICandidateSituation candidateSituation2 = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.EXTRAORDINARIO));        
        
        
        assertTrue(candidateSituation1.equals(candidateSituation2));  
        
        candidateSituation1.setDate(data2.getTime());
        assertEquals(candidateSituation1.equals(candidateSituation2), false);  
        candidateSituation1.setDate(data.getTime());
        
        candidateSituation1.setRemarks("Nada");
        assertEquals(candidateSituation1.equals(candidateSituation2), false);  
        candidateSituation1.setRemarks("Nenhuma");
        
        candidateSituation1.setValidation(new CandidateSituationValidation(CandidateSituationValidation.INACTIVE));
        assertEquals(candidateSituation1.equals(candidateSituation2), false);  
        candidateSituation1.setValidation(new CandidateSituationValidation(CandidateSituationValidation.ACTIVE));

    }
    
} // End of test from Class CandidateSituationOJB
