/*
 * Created on 10/Dec/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
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

public class ManageGrantOwnerAction extends FenixDispatchAction {
    public ActionForward prepareManageGrantOwnerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer idInternal = null;
	if (verifyParameterInRequest(request, "idInternal")) {
	    idInternal = new Integer(request.getParameter("idInternal"));
	} else if ((Integer) request.getAttribute("idInternal") != null) {
	    idInternal = (Integer) request.getAttribute("idInternal");
	}

	if (idInternal == null) {
	    throw new Exception();
	}

	//Read Grant Owner
	Object[] args = { idInternal };
	IUserView userView = UserView.getUser();
	InfoGrantOwner infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService(
		"ReadGrantOwner", args);

	if (infoGrantOwner == null) {
	    throw new Exception();
	}

	request.setAttribute("infoGrantOwner", infoGrantOwner);
	return mapping.findForward("manage-grant-owner");
    }
}