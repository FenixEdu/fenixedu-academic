/*
 * Created on 21/Mar/2003 by jpvl
 *
 */
package ServidorApresentacao;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import Util.RoleType;

/**
 * @author jpvl
 */
public abstract class TestCaseActionPresentationPersonPortal extends TestCaseActionExecution {
	/**
	 * @param testName
	 */
	public TestCaseActionPresentationPersonPortal(String testName) {
		super(testName);
	}

	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getAuthorizedRolesCollection()
	 */
	public Collection getAuthorizedRolesCollection() {
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
		roles.add(infoRole);
		return roles;
	}
	
	/* (non-Javadoc)
	 * @see ServidorApresentacao.TestCaseActionExecution#getServletConfigFile()
	 */
	protected String getServletConfigFile() {
		return "/WEB-INF/tests/web-person.xml";
	}

}
