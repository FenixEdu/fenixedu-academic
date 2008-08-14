/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantTypeAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantTypeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Object[] args = {};
	IUserView userView = UserView.getUser();
	List infoGrantTypeList = (List) ServiceUtils.executeService("ReadAllGrantTypes", args);

	if (infoGrantTypeList != null && !infoGrantTypeList.isEmpty())
	    request.setAttribute("infoGrantTypeList", infoGrantTypeList);

	return mapping.findForward("manage-grant-type");

    }
}