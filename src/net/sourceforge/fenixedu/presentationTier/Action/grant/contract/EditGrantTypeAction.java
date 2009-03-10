/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantTypeAction extends FenixDispatchAction {

    public ActionForward prepareEditGrantTypeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer idGrantType = getIntegerFromRequest(request, "idGrantType");
	if (idGrantType != null) {
	    GrantType grantType = (GrantType) rootDomainObject.readGrantTypeByOID(idGrantType);
	    request.setAttribute("grantType", grantType);
	}
	return mapping.findForward("edit-grant-type");
    }

}