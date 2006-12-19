package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.pricesManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction;

public class AcademicAdminOfficePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return getLoggedPerson(request).getEmployee().getAdministrativeOffice();
    }

}
