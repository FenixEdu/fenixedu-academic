
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.coordinator;

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
import DataBeans.InfoRole;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;

public class ReadCoordinatedDegreesTest extends TestCaseServicos {
	
	public ReadCoordinatedDegreesTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(ReadCoordinatedDegreesTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
	}

	protected void tearDown() {
		super.tearDown();
	}


	public void testReadCoordinatedDegreesList() {
		System.out.println("- Test 1 : Read Coordinated Degrees List");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);

		Object[] args = { userView };
		List coordinatedDegreesList = null;
		try {
			coordinatedDegreesList = (List) ServiceManagerServiceFactory.executeService(userView, "ReadCoordinatedDegrees", args);
		} catch (FenixServiceException ex) {
			fail("Fenix Service Exception");
		} catch (Exception ex) {
			fail("Exception");
		}
		 
		assertNotNull(coordinatedDegreesList);
		assertEquals(coordinatedDegreesList.size(), 3);

		UserView userView2 = this.getUserViewToBeTested("jorge", true);

		Object[] args2 = { userView2 };
		
		coordinatedDegreesList = null;
			 
		try {
			coordinatedDegreesList = (List) ServiceManagerServiceFactory.executeService(userView, "ReadCoordinatedDegrees", args2);
		} catch (ExcepcaoInexistente ex) {
			// All is OK
		} catch (Exception ex) {
			fail("Exception");
		}

	}
	
	

	private UserView getUserViewToBeTested(String username, boolean withRole) {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		if (withRole) infoRole.setRoleType(RoleType.COORDINATOR);
		else infoRole.setRoleType(RoleType.PERSON);
		roles.add(infoRole);
		UserView userView = new UserView(username, roles);
		return userView;
	}


}