package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.payments;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class MasterDegreePaymentsManagementDispatchAction extends PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.MASTER_DEGREE;
    }

}
