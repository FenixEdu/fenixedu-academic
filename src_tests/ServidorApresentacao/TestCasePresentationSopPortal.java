/*
 * Created on 13/Mar/2003 by jpvl
 *
 */
package ServidorApresentacao;

import java.util.ArrayList;
import java.util.Collection;

import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;

/**
 * @author jpvl
 */
public abstract class TestCasePresentationSopPortal extends TestCasePresentation{

	/**
	 * @param testName
	 */
	public TestCasePresentationSopPortal(String testName) {
		super(testName);
	}
	/**
	 * @return Object
	 */
	protected void setNotAuthorizedUser() {
		
		UserView userView = new UserView("user", null);
		Collection roles = new ArrayList();
		userView.setRoles(roles);
		getSession().setAttribute(SessionConstants.U_VIEW, userView);
	}
	/**
	 * @return Object
	 */
	protected void setAuthorizedUser() {
		getSession().setAttribute(SessionConstants.U_VIEW, getAuthorizedUser());
	}
	protected IUserView getAuthorizedUser(){
		UserView userView = new UserView("user", null);
		Collection roles = new ArrayList();
		InfoRole infoRole = new InfoRole();
		infoRole.setRoleType(RoleType.TIME_TABLE_MANAGER);
		roles.add(infoRole);
		userView.setRoles(roles);
		return userView; 
	}
}
