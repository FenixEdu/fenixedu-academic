package org.fenixedu.parking.ui.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.parking.dto.SearchParkingPartyBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ParkingManagerApp.class, path = "parking-party", titleKey = "link.users")
@Mapping(path = "/parkingParty", module = "parkingManager", formBean = "parkingForm")
@Forwards({ @Forward(name = "searchParty", path = "/parkingManager/searchParty.jsp"),
        @Forward(name = "showParkingPartyRequests", path = "/parkingManager/searchParty.jsp"),
        @Forward(name = "showParkingRequests", path = "/parkingManager/showParkingRequests.jsp"),
        @Forward(name = "editParkingParty", path = "/parkingManager/editParkingParty.jsp"),
        @Forward(name = "showParkingHistories", path = "/parkingManager/showParkingHistories.jsp"),
        @Forward(name = "showParkingRequest", path = "/parkingManager/showParkingRequest.jsp") })
public class ParkingPartyDA extends ParkingManagerDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchParty(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("searchPartyBean", new SearchParkingPartyBean());
        return mapping.findForward("searchParty");
    }

}
