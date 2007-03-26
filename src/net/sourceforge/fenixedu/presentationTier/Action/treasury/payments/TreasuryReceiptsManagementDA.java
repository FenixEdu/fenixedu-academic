package net.sourceforge.fenixedu.presentationTier.Action.treasury.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.ReceiptsManagementDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TreasuryReceiptsManagementDA extends ReceiptsManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	request.setAttribute("administrativeOffice", getAdministrativeOffice(request));
	request.setAttribute("administrativeOfficeUnit", getAdministrativeOfficeUnit(request));
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return rootDomainObject.readAdministrativeOfficeByOID(getRequestParameterAsInteger(request,
		"administrativeOfficeId"));
    }

    protected Unit getAdministrativeOfficeUnit(HttpServletRequest request) {
	return (Unit) rootDomainObject.readPartyByOID(getRequestParameterAsInteger(request,
		"administrativeOfficeUnitId"));
    }

    @Override
    protected Unit getReceiptOwnerUnit(HttpServletRequest request) {
	return (Unit) request.getAttribute("administrativeOfficeUnit");
    }

    @Override
    protected Unit getReceiptCreatorUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

    @Override
    protected String buildContextParameters(HttpServletRequest request) {
	return super.buildContextParameters(request) + "&administrativeOfficeUnitId="
		+ getAdministrativeOfficeUnit(request).getIdInternal();
    }
}
