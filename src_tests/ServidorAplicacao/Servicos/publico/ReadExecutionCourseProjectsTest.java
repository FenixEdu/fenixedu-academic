/*
 * Created on 1/Ago/2003
 */

package ServidorAplicacao.Servicos.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import DataBeans.InfoRole;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import Util.RoleType;

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

	public void testSucessfullExecution() {
		
		Object[] args = {new Integer(27)};
		
		try{
			
			GestorServicos serviceManager = GestorServicos.manager();
			List projectsName = (List) serviceManager.executar(authorizedUserView(), getNameOfServiceToBeTested(), args);
			
			assertEquals("nameB",projectsName.get(0));
			assertEquals("nameS",projectsName.get(1));
			
			System.out.println("testSuccessfulExecutionOfReadExecutionCourseProjects was SUCCESSFULY runned by class: "+this.getClass().getName());
								
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


