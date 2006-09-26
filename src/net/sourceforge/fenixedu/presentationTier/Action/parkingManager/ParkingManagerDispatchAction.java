package net.sourceforge.fenixedu.presentationTier.Action.parkingManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestSearch;
import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ParkingManagerDispatchAction extends FenixDispatchAction {
    public ActionForward showParkingRequests(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        ParkingRequestSearch parkingRequestSearch = (ParkingRequestSearch) getRendererObject();

        if (parkingRequestSearch == null) {
            parkingRequestSearch = new ParkingRequestSearch();
        }

        request.setAttribute("parkingRequestSearch", parkingRequestSearch);
        return mapping.findForward("showParkingRequests");
    }

    public ActionForward showRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        final Integer code = new Integer(request.getParameter("idInternal"));
        final ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
        request.setAttribute("parkingRequest", parkingRequest);
//        if (code != null) {
//            ParkingRequest parkingRequest = rootDomainObject.readParkingRequestByOID(code);
//            ParkingRequestFactoryEditor parkingRequestFactoryEditor = parkingRequest
//                    .getParkingRequestFactoryEditor();
//            request.setAttribute("parkingRequestFactoryEditor", parkingRequestFactoryEditor);
//
//        }
        return mapping.findForward("showParkingRequest");
    }

    public ActionForward acceptRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer parkingRequestID = new Integer(request.getParameter("parkingRequestID"));
        if (parkingRequestID == null) {
            ParkingRequest parkingRequest = (ParkingRequest) RenderUtils.getViewState("edit")
                    .getMetaObject().getObject();
            parkingRequestID = parkingRequest.getIdInternal();
        }
        request.setAttribute("parkingRequestAccepted", "parkingRequestAccepted");
        Object[] args = { parkingRequestID, ParkingRequestState.ACCEPTED };
        // verificar autorização
        ServiceUtils
                .executeService(SessionUtils.getUserView(request), "UpdateParkingRequestState", args);
        return showParkingRequests(mapping, actionForm, request, response);
    }

    public ActionForward rejectRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Integer parkingRequestID = new Integer(request.getParameter("parkingRequestID"));

        request.setAttribute("parkingRequestAccepted", "parkingRequestAccepted");
        Object[] args = { parkingRequestID, ParkingRequestState.REJECTED };
        // verificar autorização
        ServiceUtils
                .executeService(SessionUtils.getUserView(request), "UpdateParkingRequestState", args);
        return showParkingRequests(mapping, actionForm, request, response);
    }

    public ActionForward prepareSearchParkingParty(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // verificar autorização
        return mapping.findForward("searchParkingParty");
    }

}