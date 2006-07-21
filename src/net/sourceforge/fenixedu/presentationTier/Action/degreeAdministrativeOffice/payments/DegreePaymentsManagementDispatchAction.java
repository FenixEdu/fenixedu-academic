package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.payments;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;

public class DegreePaymentsManagementDispatchAction extends
        PaymentsManagementDispatchAction {
    
    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.DEGREE;
    }

}
