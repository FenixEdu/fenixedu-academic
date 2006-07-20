package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.pricesManagement;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class MasterDegreePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.MASTER_DEGREE;
    }
}
