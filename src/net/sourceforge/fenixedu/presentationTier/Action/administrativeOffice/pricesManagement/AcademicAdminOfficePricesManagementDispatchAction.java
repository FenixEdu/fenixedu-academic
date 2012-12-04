package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.pricesManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/pricesManagement", module = "academicAdminOffice")
@Forwards({
	@Forward(name = "viewPrices", path = "/academicAdminOffice/pricesManagement/viewPrices.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.payments.prices")),
	@Forward(name = "editPrice", path = "/academicAdminOffice/pricesManagement/editPrice.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.payments.prices")) })
public class AcademicAdminOfficePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return getLoggedPerson(request).getEmployee().getAdministrativeOffice();
    }

}
