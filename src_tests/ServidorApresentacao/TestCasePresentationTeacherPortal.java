/*
 * Created on 18/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import Util.RoleType;

/**
 * @author PTRLV
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class TestCasePresentationTeacherPortal extends TestCaseActionExecution{

	public TestCasePresentationTeacherPortal (String testName) {
			super(testName);
		}

	public Collection getAuthorizedRolesCollection() {
			Collection roles = new ArrayList();
			InfoRole infoRole = new InfoRole();
			infoRole.setRoleType(RoleType.TEACHER);
			roles.add(infoRole);
			return roles;
		}
}
