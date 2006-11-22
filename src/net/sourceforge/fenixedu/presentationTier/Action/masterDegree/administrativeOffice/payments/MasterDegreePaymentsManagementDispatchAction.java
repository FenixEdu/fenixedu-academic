package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;

public class MasterDegreePaymentsManagementDispatchAction extends PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(final HttpServletRequest request) {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
    }


}
