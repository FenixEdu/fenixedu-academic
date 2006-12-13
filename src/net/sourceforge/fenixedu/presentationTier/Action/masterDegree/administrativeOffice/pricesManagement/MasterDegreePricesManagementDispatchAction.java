package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.pricesManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction;

public class MasterDegreePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
    }
   
}
