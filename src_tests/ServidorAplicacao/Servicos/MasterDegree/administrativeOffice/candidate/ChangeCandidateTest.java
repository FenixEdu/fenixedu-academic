
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.candidate;

/**
 *
 * @author Nuno Nunes & Joana Mota 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoCandidateSituation;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.SituationName;
import Util.Specialization;

public class ChangeCandidateTest extends TestCaseServicos {

	public ChangeCandidateTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ChangeCandidateTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
        
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	
	public void testChangeMasterDegreeCandidate() {
		System.out.println("- Test 1 : Change Master Degree Candidate");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);

		InfoMasterDegreeCandidate originalCandidate = this.readMasterDegreeCandidate("nmsn");
		InfoMasterDegreeCandidate newCandidate = this.readMasterDegreeCandidate("jccm");
		
		newCandidate.getInfoPerson().setNumeroDocumentoIdentificacao(originalCandidate.getInfoPerson().getNumeroDocumentoIdentificacao());
		newCandidate.getInfoPerson().setUsername(originalCandidate.getInfoPerson().getUsername());
		newCandidate.setCandidateNumber(originalCandidate.getCandidateNumber());
		newCandidate.setSpecialization(new Specialization(Specialization.MESTRADO_STRING));
		
		Object[] args = {originalCandidate, newCandidate};
	
		 InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = null;
		 
		 try {
			 newInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) _gestor.executar(userView, "ChangeCandidate", args);
		 } catch (FenixServiceException ex) {
			fail("Fenix Service Exception");
		 } catch (Exception ex) {
			fail("Eception");
		 }

 	    assertNotNull(newInfoMasterDegreeCandidate);
		assertEquals(newInfoMasterDegreeCandidate, newCandidate);
	}

	public void testChangeMasterDegreeCandidateWithoutRole() {
		System.out.println("- Test 2 : Change Master Degree Candidate Without Role");
		
		UserView userView = this.getUserViewToBeTested("nmsn", false);

		InfoMasterDegreeCandidate originalCandidate = this.readMasterDegreeCandidate("nmsn");
		InfoMasterDegreeCandidate newCandidate = this.readMasterDegreeCandidate("jccm");
		
		Object[] args = {originalCandidate, newCandidate};
	
		 InfoMasterDegreeCandidate newInfoMasterDegreeCandidate = null;
		 
		 try {
			 newInfoMasterDegreeCandidate = (InfoMasterDegreeCandidate) _gestor.executar(userView, "ChangeCandidate", args);
		 } catch (FenixServiceException ex) {
			// All is OK
		 } catch (Exception ex) {
			fail("Exception");
		 }
	}


	private UserView getUserViewToBeTested(String username, boolean withRole) {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		if (withRole) infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
		else infoRole.setRoleType(RoleType.PERSON);
		roles.add(infoRole);
		UserView userView = new UserView(username, roles);
		return userView;
	}
	
	private InfoMasterDegreeCandidate readMasterDegreeCandidate(String username) {
		IMasterDegreeCandidate masterDegreeCandidate = null;
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
			fail("Error !");
		}
		
		InfoMasterDegreeCandidate infoMasterDegreeCandidate = new InfoMasterDegreeCandidate();
		infoMasterDegreeCandidate = Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate((IMasterDegreeCandidate) result.get(0));
		
		InfoCandidateSituation infoCandidateSituation = new InfoCandidateSituation();
		infoCandidateSituation.setSituation(SituationName.ADMITIDO_OBJ);
		infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
		
		return infoMasterDegreeCandidate;
	}


}