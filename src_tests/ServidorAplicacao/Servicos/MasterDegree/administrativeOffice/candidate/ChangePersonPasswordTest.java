
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

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

public class ChangePersonPasswordTest extends TestCaseServicos {

	public ChangePersonPasswordTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(ChangePersonPasswordTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
        
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	
	public void testChangePersonPassword() {
		System.out.println("- Test 1 : Change Person Password");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);

		InfoMasterDegreeCandidate infoMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");
		
		InfoPerson infoPerson = infoMasterDegreeCandidate.getInfoPerson();
		infoPerson.setPassword("pass2");
		
		Object[] args = { infoPerson };
	
		 InfoMasterDegreeCandidate changedMasterDegreeCandidate = null;
		 
		 try {
			 ServiceManagerServiceFactory.executeService(userView, "ChangePersonPassword", args);
		 } catch (FenixServiceException ex) {
			fail("Fenix Service Exception");
		 } catch (Exception ex) {
			fail("Exception");
		 }

		changedMasterDegreeCandidate = this.readMasterDegreeCandidate("nmsn");
		
		assertTrue(PasswordEncryptor.areEquals(changedMasterDegreeCandidate.getInfoPerson().getPassword(), "pass2"));

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
		
		return Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate((IMasterDegreeCandidate) result.get(0));
	}


}