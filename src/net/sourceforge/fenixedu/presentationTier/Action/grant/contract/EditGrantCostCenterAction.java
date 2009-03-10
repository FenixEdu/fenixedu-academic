/*
 * Created on 20/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantCostCenterAction extends FenixDispatchAction {

    public ActionForward prepareEditGrantCostCenterForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer idGrantCostCenter = getIntegerFromRequest(request, "idGrantCostCenter");
	if (idGrantCostCenter != null) {
	    GrantCostCenter grantCostCenter = (GrantCostCenter) rootDomainObject.readGrantPaymentEntityByOID(idGrantCostCenter);
	    request.setAttribute("grantCostCenter", grantCostCenter);
	}
	return mapping.findForward("edit-grant-costcenter");
    }
}