package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.pricesManagement;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class DegreePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.DEGREE;
    }

}
