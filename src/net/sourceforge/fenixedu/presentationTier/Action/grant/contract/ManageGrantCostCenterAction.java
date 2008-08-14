/*
 * Created on 15/Fev/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
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
public class ManageGrantCostCenterAction extends FenixDispatchAction {
    public ActionForward prepareManageGrantCostCenter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	try {

	    Object[] args = { GrantCostCenter.class.getName() };

	    IUserView userView = UserView.getUser();
	    List infoGrantCostCenterList = (List) ServiceUtils.executeService("ReadAllGrantPaymentEntitiesByClassName", args);

	    if (infoGrantCostCenterList != null && !infoGrantCostCenterList.isEmpty())
		request.setAttribute("infoGrantCostCenterList", infoGrantCostCenterList);

	    return mapping.findForward("manage-grant-costcenter");
	} catch (FenixServiceException e) {
	    return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-costcenter", null);
	}
    }
}