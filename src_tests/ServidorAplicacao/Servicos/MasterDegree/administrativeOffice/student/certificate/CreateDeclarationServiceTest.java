
/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.student.certificate;

/**
 *
 * @author Nuno Nunes & Joana Mota 
 */
import java.util.ArrayList;
import java.util.Collection;

import framework.factory.ServiceManagerServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRole;
import DataBeans.InfoStudent;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;
import Util.Specialization;
import Util.TipoCurso;

public class CreateDeclarationServiceTest extends TestCaseServicos {

	public CreateDeclarationServiceTest(java.lang.String testName) {
		super(testName);
	}
    
	public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	}
    
	public static Test suite() {
		TestSuite suite = new TestSuite(CreateDeclarationServiceTest.class);
        
		return suite;
	}
    
	protected void setUp() {
		super.setUp();
        
	}
    
	protected void tearDown() {
		super.tearDown();
	}
	public void testCreateDeclarationForExistingStudent() {
		System.out.println("- Test 1 : Create Declaration For Existing Student");
		
		UserView userView = this.getUserViewToBeTested("nmsn", true);

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(new Integer(46865));
		infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
		
		
		Object[] args = {infoStudent, new Specialization(Specialization.ESPECIALIZACAO)};
	
	 
		 try {
			 ServiceManagerServiceFactory.executeService(userView, "CreateDeclaration", args);
		 } catch (FenixServiceException ex) {
			fail("Fenix Service Exception");
		 } catch (Exception ex) {
			fail("Exception");
		 }
		
	}

	public void testCreateDeclarationForUnexistingStudent() {
			System.out.println("- Test 2 : Create Declaration For Unexisting Student");
		
			UserView userView = this.getUserViewToBeTested("nmsn", true);

			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setNumber(new Integer(1));
			infoStudent.setDegreeType(new TipoCurso(TipoCurso.MESTRADO));
		
		
			Object[] args = {infoStudent, new Specialization(Specialization.ESPECIALIZACAO)};
	
	 
			 try {
				 ServiceManagerServiceFactory.executeService(userView, "CreateDeclaration", args);
			 } catch (FenixServiceException ex) {
				fail("Fenix Service Exception");
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


}