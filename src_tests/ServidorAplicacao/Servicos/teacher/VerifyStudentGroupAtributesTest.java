/*
 * Created on 30/Jul/2003
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import DataBeans.InfoStudentGroup;
import DataBeans.util.Cloner;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author asnr and scpo
 */
public class VerifyStudentGroupAtributesTest extends TestCaseServicos{

	/**
	 * @param testName
	 */
	public VerifyStudentGroupAtributesTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "VerifyStudentGroupAtributes";
	}

	public void testSucessfullExecutionForAtomicPolicy() {
		
		IStudentGroup studentGroup = null;
			
		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			studentGroup = (IStudentGroup)sp.getIPersistentStudentGroup().readByOId(new StudentGroup(new Integer(6)),false);
			sp.confirmarTransaccao();						
		
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		  }
			
		InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup);		
		
		Object[] args = {infoStudentGroup,new Integer(3)};
		
		try{
			
			GestorServicos serviceManager = GestorServicos.manager();
			Boolean result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
			
			assertTrue(result.booleanValue());
			
						
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public void testSucessfullExecutionForIndividualPolicy() {
		
		IStudentGroup studentGroup = null;
			
		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			studentGroup = (IStudentGroup)sp.getIPersistentStudentGroup().readByOId(new StudentGroup(new Integer(2)),false);
			sp.confirmarTransaccao();						
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		  }
			
		InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup);		
		Object[] args = {infoStudentGroup,new Integer(1)};
		
		try{
			
			GestorServicos serviceManager = GestorServicos.manager();
			Boolean result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
			assertTrue(result.booleanValue());
			
						
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

	public void testUnsucessfullExecutionForAtomicPolicy() {
		
			IStudentGroup studentGroup = null;
			
			try {
			
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				studentGroup = (IStudentGroup)sp.getIPersistentStudentGroup().readByOId(new StudentGroup(new Integer(6)),false);
				sp.confirmarTransaccao();						
			} catch (ExcepcaoPersistencia ex) {
				ex.printStackTrace();
			  }
			
			InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup);		
		
			Object[] args = {infoStudentGroup,new Integer(1)};
		
			try{
			
				GestorServicos serviceManager = GestorServicos.manager();
				Boolean result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
			
				assertFalse(result.booleanValue());
			
						
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Executing  Service!");
			}
		}
		
		
	public void testUnsucessfullExecutionForIndividualPolicy() {
		
			IStudentGroup studentGroup = null;
			
			try {
			
				ISuportePersistente sp = SuportePersistenteOJB.getInstance();
				sp.iniciarTransaccao();
				studentGroup = (IStudentGroup)sp.getIPersistentStudentGroup().readByOId(new StudentGroup(new Integer(2)),false);
				sp.confirmarTransaccao();						
			} catch (ExcepcaoPersistencia ex) {
				ex.printStackTrace();
			  }
			
			InfoStudentGroup infoStudentGroup = Cloner.copyIStudentGroup2InfoStudentGroup(studentGroup);		
			Object[] args = {infoStudentGroup,new Integer(2)};
		
			try{
			
				GestorServicos serviceManager = GestorServicos.manager();
				Boolean result = (Boolean) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
				assertFalse(result.booleanValue());
			
						
			} catch (FenixServiceException e) {
				e.printStackTrace();
				fail("Executing  Service!");
			}
		}


	public IUserView authorizedUserView() {
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TEACHER);

		Collection roles = new ArrayList();
		roles.add(infoRole);

		UserView userView = new UserView("user", roles);
	
		return userView;
}
}
