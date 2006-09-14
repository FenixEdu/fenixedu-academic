package net.sourceforge.fenixedu.presentationTier.Action.person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.parking.ParkingParty;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ParkingDispatchAction extends FenixDispatchAction {
    public ActionForward prepareParking(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty == null) {
            parkingParty = (ParkingParty) ServiceUtils.executeService(SessionUtils.getUserView(request),
                    "CreateParkingParty", new Object[] { userView.getPerson() });
        }
        request.setAttribute("parkingParty", parkingParty);
        return mapping.findForward("prepareParking");
    }

    public ActionForward acceptRegulation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        if (parkingParty != null) {
            ServiceUtils.executeService(SessionUtils.getUserView(request), "AcceptRegulation",
                    new Object[] { parkingParty });
        }
        return prepareParking(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditParking(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        // em 1º lugar ver se ele tem os dados pessoais necessários
        // if (userView.getPerson().getParkingParty() != null) {
        // request.setAttribute("parkingRequest", userView.getPerson().getParkingParty()
        // .getParkingRequests().iterator().next());
        // }

        ParkingParty parkingParty = userView.getPerson().getParkingParty();
        request.setAttribute("parkingParty", parkingParty);
        return mapping.findForward("editParkingRequest");
    }

    public ActionForward editParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Object serviceResult = executeFactoryMethod(request);
        return prepareParking(mapping, actionForm, request, response);
    }

    public ActionForward createParkingRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Object serviceResult = executeFactoryMethod(request);
        return prepareParking(mapping, actionForm, request, response);
    }

}