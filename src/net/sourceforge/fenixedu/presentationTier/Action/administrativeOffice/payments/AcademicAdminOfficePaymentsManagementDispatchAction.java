package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AcademicAdminOfficePaymentsManagementDispatchAction extends PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return AdministrativeOffice.readByEmployee(getUserView(request).getPerson().getEmployee());
    }
    
    @Override
    protected void setContextInformation(HttpServletRequest request) {
	request.setAttribute("student", getPerson(request).getStudent());
    }
    
    @Override
    protected ActionForward findMainForward(ActionMapping mapping) {
        return mapping.findForward("viewStudentDetails");
    }

}
