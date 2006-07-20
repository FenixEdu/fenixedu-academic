package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.payments;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class DegreePaymentsManagementDispatchAction extends
        PaymentsManagementDispatchAction {
    
    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.DEGREE;
    }

}
