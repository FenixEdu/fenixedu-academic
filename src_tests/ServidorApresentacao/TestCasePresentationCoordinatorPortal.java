
package ServidorApresentacao;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import Util.RoleType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */
public abstract class TestCasePresentationCoordinatorPortal extends TestCaseActionExecution {
	/**
	 * @param testName
	 */
	public TestCasePresentationCoordinatorPortal(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getAuthorizedRolesCollection()
	 */
	public Collection getAuthorizedRolesCollection() {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.COORDINATOR);
		roles.add(infoRole);
		return roles;
	}
	
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/web.xml";
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getRequestPathInfoPathAction()
	 */
	protected String getRequestPathInfoPathAction() {
		return "coordinator";
	}

}
