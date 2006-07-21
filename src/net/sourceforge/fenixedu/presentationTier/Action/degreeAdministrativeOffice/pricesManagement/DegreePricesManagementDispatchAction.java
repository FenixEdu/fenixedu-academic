package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.pricesManagement;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction;

public class DegreePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOfficeType getAdministrativeOfficeType() {
        return AdministrativeOfficeType.DEGREE;
    }

}
