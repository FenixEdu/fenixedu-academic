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
import Util.CandidateSituationValidation;
import Util.SituationName;

public class CandidateSituationOJBTest extends TestCaseOJB {
    
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
    }
    
    protected void tearDown(){
        super.tearDown();
    }
    
    public void testReadExistingCandidateSituation() {
        System.out.println("- Test 1 : Read existing Candidate Situation");
        ICandidateSituation candidateSituationTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(1), "MIC", new Integer(2002));
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        // Testing the obtained Candidate Situation
        assertNotNull(candidateSituationTemp);
        assertTrue(candidateSituationTemp.getDate().toString().equals("2002-11-17"));
        assertTrue(candidateSituationTemp.getRemarks().equals("Nothing"));
        assertTrue(candidateSituationTemp.getValidation().getCandidateSituationValidation().equals(new Integer(1)));
        
    }
    
    public void testReadNonExistingCandidateSituation() {
        System.out.println("- Test 2 : Read non-existing Candidate Situation");
        ICandidateSituation candidateSituationTemp = null;
        
        try {
            _suportePersistente.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(3), "2", new Integer(2003));
            assertNull(candidateSituationTemp);
            _suportePersistente.confirmarTransaccao();
            
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
            _suportePersistente.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
        
		ICandidateSituation candidateSituation = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.EXTRAORDINARIO));

        try {
            _suportePersistente.iniciarTransaccao();
            persistentCandidateSituation.writeCandidateSituation(candidateSituation);
            _suportePersistente.confirmarTransaccao();
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
            _suportePersistente.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }

		ICandidateSituation candidateSituation1 = new CandidateSituation(data.getTime(), "Nenhuma", new CandidateSituationValidation(CandidateSituationValidation.ACTIVE), candidateTemp, new SituationName(SituationName.PENDENTE));        

        try {
            _suportePersistente.iniciarTransaccao();
            persistentCandidateSituation.writeCandidateSituation(candidateSituation1);
            _suportePersistente.confirmarTransaccao();
            fail("Expected error");
        } catch (ExcepcaoPersistencia ex) {
			// All is OK
        }
    } 
    
    public void testDeleteExistingCandidateSituation() {
        System.out.println("- Test 5 : Delete Existing Candidate Situation");

        ICandidateSituation candidateSituationTemp = null;
        try {
            _suportePersistente.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(1), "MIC", new Integer(2002));
            persistentCandidateSituation.delete(candidateSituationTemp);
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
         //Test if it was really deleted
        try {
            _suportePersistente.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(1), "MIC", new Integer(2002));
            
            assertNull(candidateSituationTemp);
            _suportePersistente.confirmarTransaccao();
            
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
            _suportePersistente.iniciarTransaccao();
	        candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(1), "MIC", new Integer(2002));
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }

        try {
            _suportePersistente.iniciarTransaccao();
            persistentCandidateSituation.deleteAll();
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }


        try {
            _suportePersistente.iniciarTransaccao();
            persistentCandidateSituation.delete(candidateSituationTemp);
            _suportePersistente.confirmarTransaccao();
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
    } 
   
    public void testDeleteAllCandidateSituations() {
        System.out.println("- Test 7 : Delete All Candidate Situations");
        ICandidateSituation candidateSituationTemp = null;
        try {
            _suportePersistente.iniciarTransaccao();
            persistentCandidateSituation.deleteAll();
            _suportePersistente.confirmarTransaccao();
            
        } catch (ExcepcaoPersistencia ex) {
            fail("    -> Error on test");
        }
        
         //Test if it was really deleted
        try {
            _suportePersistente.iniciarTransaccao();
            candidateSituationTemp = persistentCandidateSituation.readCandidateSituation(new Integer(1), "MIC", new Integer(200));
            assertNull(candidateSituationTemp);
            _suportePersistente.confirmarTransaccao();
            
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
            _suportePersistente.iniciarTransaccao();
	        candidateTemp = persistentMasterDegreeCandidate.readMasterDegreeCandidateByUsername("Cand1");
            _suportePersistente.confirmarTransaccao();
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
