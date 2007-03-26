package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.GuidesManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TreasuryGuidesManagementDA extends GuidesManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("administrativeOffice", getAdministrativeOffice(request));
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return rootDomainObject.readAdministrativeOfficeByOID(getRequestParameterAsInteger(request,
		"administrativeOfficeId"));
    }

}
