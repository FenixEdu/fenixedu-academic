
package ServidorPersistente.OJB;

import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Guide;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGuide;
import Dominio.IPessoa;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentContributor;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.GuideRequester;
import Util.PaymentType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class GuideOJBTest extends TestCaseOJB {
	
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentGuide persistentGuide = null;
	IPessoaPersistente persistentPerson = null;
	IPersistentContributor persistentContributor = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	
	
	public GuideOJBTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(GuideOJBTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentGuide = persistentSupport.getIPersistentGuide();
		persistentPerson = persistentSupport.getIPessoaPersistente();
		persistentContributor = persistentSupport.getIPersistentContributor();
		persistentExecutionDegree = persistentSupport.getICursoExecucaoPersistente();
		persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testReadAll() {
		System.out.println("Test 1 - Read Guide");        

		try {
			persistentSupport.iniciarTransaccao();
			
			IPessoa person = persistentPerson.lerPessoaPorUsername("nmsn");
			assertNotNull(person);
			
			IContributor contributor = persistentContributor.readByContributorNumber(new Integer(123));
			assertNotNull(contributor);
			
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);
			ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeNameAndExecutionYear("Mestrado em Engenharia Electrotecnica e de Computadores", executionYear);
			assertNotNull(executionDegree);
			
			
			IGuide guide = persistentGuide.readByNumberAndYear(new Integer(1), new Integer(2003));
			assertNotNull(guide);
			
			
			assertEquals(guide.getNumber(), new Integer(1));
			assertEquals(guide.getYear(), new Integer(2003));
			assertEquals(guide.getPerson(), person);
			assertEquals(guide.getContributor(), contributor);
			assertEquals(guide.getRemarks(), "guia1");
			assertEquals(guide.getTotal(), new Double(600.04));
			assertEquals(guide.getGuideRequester(), GuideRequester.CANDIDATE_TYPE);
			assertEquals(guide.getGuideSituations().size(), 2);
			assertEquals(guide.getPaymentType(), PaymentType.CASH_TYPE);
			
			assertEquals(guide.getCreationDate().toString(), "2003-04-04");
			assertEquals(guide.getVersion(), new Integer(1));
			
			guide = persistentGuide.readByNumberAndYear(new Integer(2), new Integer(2003));
			assertNotNull(guide);
					
			assertEquals(guide.getNumber(), new Integer(2));
			assertEquals(guide.getYear(), new Integer(2003));
			assertEquals(guide.getPerson(), person);
			assertEquals(guide.getContributor(), contributor);
			assertEquals(guide.getRemarks(), "guia2");
			assertEquals(guide.getTotal(), new Double(400.04));
			assertEquals(guide.getExecutionDegree(), executionDegree);
			
			assertEquals(guide.getGuideEntries().size(), 2);
			assertEquals(guide.getGuideRequester(), GuideRequester.CANDIDATE_TYPE);
			assertEquals(guide.getGuideSituations().size(), 1);
			assertEquals(guide.getPaymentType(), PaymentType.ATM_TYPE);
			assertEquals(guide.getCreationDate().toString(), "2003-04-03");
			assertEquals(guide.getVersion(), new Integer(1));
					

			guide = persistentGuide.readByNumberAndYear(new Integer(5), new Integer(2003));
			assertNull(guide);


			persistentSupport.confirmarTransaccao();
		} catch(ExcepcaoPersistencia ex) {
			fail("testReadGuide: unexpected exception");
		}
	}

	public void testWriteExisting() {
		System.out.println("Test 2 - Write Existing Guide");        

		try {
			persistentSupport.iniciarTransaccao();
			IGuide guide = new Guide();
			guide.setNumber(new Integer(1));
			guide.setYear(new Integer(2003));
			
			persistentGuide.write(guide);
						
			persistentSupport.confirmarTransaccao();
		} catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex) {
			fail("testWriteGuide: unexpected exception" + ex);
		}
	}

	public void testWriteNonExisting() {
			System.out.println("Test 3 - Write Non-Existing Guide");        

			try {
				persistentSupport.iniciarTransaccao();
				IGuide guide = new Guide();
				IPessoa person = persistentPerson.lerPessoaPorUsername("nmsn");
				assertNotNull(person);
			
				IContributor contributor = persistentContributor.readByContributorNumber(new Integer(123));
				assertNotNull(contributor);

				IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
				assertNotNull(executionYear);
				ICursoExecucao executionDegree = persistentExecutionDegree.readByDegreeNameAndExecutionYear("Mestrado em Engenharia Electrotecnica e de Computadores", executionYear);
				assertNotNull(executionDegree);

				Calendar date = Calendar.getInstance();
				date.set(2002, Calendar.NOVEMBER, 17);
			
				guide.setNumber(new Integer(2));
				guide.setYear(new Integer(2000));
				guide.setContributor(contributor);
				guide.setPerson(person);
				guide.setRemarks("non-existing guide");
				guide.setTotal(new Double(20.05));
				guide.setGuideRequester(GuideRequester.CANDIDATE_TYPE);
				guide.setExecutionDegree(executionDegree);
				guide.setCreationDate(date.getTime());
				guide.setVersion(new Integer (2));
			
				persistentGuide.write(guide);
						
				persistentSupport.confirmarTransaccao();
				
				persistentSupport.iniciarTransaccao();
				IGuide guideBD = persistentGuide.readByNumberAndYear(new Integer(2), new Integer(2000));

				assertNotNull(guideBD);

				assertEquals(guideBD.getRemarks(), guide.getRemarks());
				assertEquals(guideBD.getTotal(), guide.getTotal());
				assertEquals(guideBD.getContributor(), guide.getContributor());
				assertEquals(guideBD.getPerson(), guide.getPerson());
				assertEquals(guideBD.getNumber(), guide.getNumber());
				assertEquals(guideBD.getYear(), guide.getYear());
				assertEquals(guideBD.getCreationDate(), guide.getCreationDate());
				assertEquals(guideBD.getVersion(), guide.getVersion());
				
				persistentSupport.confirmarTransaccao();
				
			} catch(ExcepcaoPersistencia ex) {
				fail("testWriteGuide: unexpected exception" + ex);
			}
		}

	public void testGenerateGuideNumber() {
		System.out.println("Test 4 - Generate Guide Number");        

		try {
			persistentSupport.iniciarTransaccao();
			Integer guideNumber = persistentGuide.generateGuideNumber(new Integer(2003));
			assertEquals(guideNumber, new Integer(3));
									
			persistentSupport.confirmarTransaccao();
		} catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex) {
			fail("testGenerateGuideNumber: unexpected exception" + ex);
		}
	}



}