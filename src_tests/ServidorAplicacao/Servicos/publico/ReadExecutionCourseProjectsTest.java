/*
 * Created on 1/Ago/2003
 */

package ServidorAplicacao.Servicos.publico;

import java.util.List;

import DataBeans.util.Cloner;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 */

public class ReadExecutionCourseProjectsTest extends TestCaseServicos {
	
	/**
	 * @param testName
	 */
	public ReadExecutionCourseProjectsTest(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.TestCaseServices#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested() {
		return "ReadExecutionCourseProjects";
	}

	public void testSucessfullExecution()throws ExcepcaoInexistente, FenixServiceException  {
		
		Object[] args = {new Integer(27)};
		IGroupProperties groupProperties1 = null; 
		IGroupProperties groupProperties2=null; 
		try{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();
			groupProperties1 =
				(IGroupProperties) sp
					.getIPersistentGroupProperties()
					.readByOId(
					new GroupProperties(new Integer(2)),
					false);
			System.out.println("NO TESTE GROUP_PROPERTIES1"+groupProperties1);
			groupProperties2 =
							(IGroupProperties) sp
								.getIPersistentGroupProperties()
								.readByOId(
								new GroupProperties(new Integer(4)),
								false);
			sp.cancelarTransaccao();
			System.out.println("NO TESTE GROUP_PROPERTIES2"+groupProperties2);
								
		} catch (ExcepcaoPersistencia e) {
					e.printStackTrace();
					throw new FenixServiceException("error.impossibleReadExecutionCourseProjects");
				}
		try{
			GestorServicos serviceManager = GestorServicos.manager();
			List projectsGroupProperties = (List) serviceManager.executar(null, getNameOfServiceToBeTested(), args);
			
			
			assertEquals(Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties1),projectsGroupProperties.get(0));
			assertEquals(Cloner.copyIGroupProperties2InfoGroupProperties(groupProperties2),projectsGroupProperties.get(1));
			
			System.out.println("testSuccessfulExecutionOfReadExecutionCourseProjects was SUCCESSFULY runned by class: "+this.getClass().getName());
								
		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Executing  Service!");
		}
	}

//	
//	public IUserView authorizedUserView() {
//
//		InfoRole infoRole = new InfoRole();
//		infoRole.setRoleType(RoleType.TEACHER);
//
//		Collection roles = new ArrayList();
//		roles.add(infoRole);
//
//		UserView userView = new UserView("user", roles);
//	
//		return userView;
//}

}


