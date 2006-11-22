package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;

public class DegreePaymentsManagementDispatchAction extends PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(final HttpServletRequest request) {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

}
