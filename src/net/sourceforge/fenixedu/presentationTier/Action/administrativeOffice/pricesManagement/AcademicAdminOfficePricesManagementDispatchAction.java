package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.pricesManagement;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/pricesManagement", module = "academicAdminOffice")
@Forwards( { @Forward(name = "viewPrices", path = "/academicAdminOffice/pricesManagement/viewPrices.jsp"),
	@Forward(name = "editPrice", path = "/academicAdminOffice/pricesManagement/editPrice.jsp") })
public class AcademicAdminOfficePricesManagementDispatchAction extends PricesManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return getLoggedPerson(request).getEmployee().getAdministrativeOffice();
    }

}
