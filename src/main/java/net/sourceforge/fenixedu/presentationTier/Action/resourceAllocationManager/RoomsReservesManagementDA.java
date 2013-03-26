package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.DeleteGenericEvent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ClosePunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.CreateRoomsPunctualScheduling;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditRoomsPunctualScheduling;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.MarkPunctualRoomsOccupationCommentsAsRead;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.OpenPunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.RequestState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "resourceAllocationManager", path = "/roomsReserveManagement", attribute = "searchSpecificRequestForm",
        formBean = "searchSpecificRequestForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "prepareFinalizeCreation", path = "prepare-finalize-creation-of-rooms-punctual-scheduling"),
        @Forward(name = "prepareViewRoomsPunctualScheduling", path = "prepare-view-rooms-punctual-scheduling"),
        @Forward(name = "prepareCreateNewRoomPunctualScheduling", path = "prepare-create-room-punctual-scheduling"),
        @Forward(name = "seeRoomsReserveRequests", path = "see-rooms-reserve-requests"),
        @Forward(name = "seeSpecifiedRoomsReserveRequest", path = "see-specified-rooms-reserve-request") })
public class RoomsReservesManagementDA extends RoomsPunctualSchedulingDA {

    public ActionForward seeSpecificRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {
        ActionForward forward = seeRoomsReserveRequests(mapping, form, request, response);
        DynaActionForm dynaForm = (DynaActionForm) form;
        final Integer requestID = (Integer) dynaForm.get("requestID");
        final PunctualRoomsOccupationRequest roomRequest = PunctualRoomsOccupationRequest.getRequestById(requestID);
        request.setAttribute("specificRequest", roomRequest);
        return forward;
    }

    public ActionForward seeRoomsReserveRequests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {

        Person person = getLoggedPerson(request);

        List<PunctualRoomsOccupationRequest> personRequests = person.getPunctualRoomsOccupationRequestsToProcessOrderByDate();
        Set<PunctualRoomsOccupationRequest> openedRequests =
                PunctualRoomsOccupationRequest.getRequestsByTypeAndDiferentOwnerOrderByDate(RequestState.OPEN, person);
        Set<PunctualRoomsOccupationRequest> newRequests =
                PunctualRoomsOccupationRequest.getRequestsByTypeOrderByDate(RequestState.NEW);

        Set<PunctualRoomsOccupationRequest> resolvedRequests =
                PunctualRoomsOccupationRequest.getResolvedRequestsOrderByMoreRecentComment();
        CollectionPager<PunctualRoomsOccupationRequest> collectionPager =
                new CollectionPager<PunctualRoomsOccupationRequest>(
                        resolvedRequests != null ? resolvedRequests : new ArrayList<PunctualRoomsOccupationRequest>(), 10);
        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));

        request.setAttribute("personRequests", personRequests);
        request.setAttribute("openedRequests", openedRequests);
        request.setAttribute("newRequests", newRequests);
        request.setAttribute("resolvedRequests", collectionPager.getPage(pageNumber));

        return mapping.findForward("seeRoomsReserveRequests");
    }

    public ActionForward seeSpecifiedRoomsReserveRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {

        Person loggedPerson = getLoggedPerson(request);
        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
        if (roomsReserveRequest.getOwner() != null && roomsReserveRequest.getOwner().equals(loggedPerson)) {
            MarkPunctualRoomsOccupationCommentsAsRead.run(roomsReserveRequest, false);
        }
        request.setAttribute("roomsReserveBean", new RoomsReserveBean(loggedPerson, roomsReserveRequest));
        return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    public ActionForward createNewRoomsReserveComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        return createNewRoomsReserveComment(mapping, request, false, false);
    }

    public ActionForward createNewRoomsReserveCommentAndMakeRequestResolved(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        return createNewRoomsReserveComment(mapping, request, false, true);
    }

    @Override
    public ActionForward deleteRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {
        GenericEvent genericEventFromParameter = getGenericEventFromParameter(request);
        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);

        try {
            DeleteGenericEvent.run(genericEventFromParameter);
        } catch (DomainException e) {
            saveMessages(request, e);
        }

        request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));
        return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    @Override
    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {
        ActionForward forward = super.prepareView(mapping, form, request, response);
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) request.getAttribute("roomsPunctualSchedulingBean");
        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);

        if (roomsReserveRequest == null) {
            throw new InvalidArgumentException();
        }

        bean.setRoomsReserveRequest(roomsReserveRequest);
        return forward;
    }

    @Override
    public ActionForward editRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {
        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();

        try {
            EditRoomsPunctualScheduling.run(bean);

        } catch (DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            return mapping.findForward("prepareFinalizeCreation");
        }

        PunctualRoomsOccupationRequest roomsReserveRequest = bean.getRoomsReserveRequest();
        if (roomsReserveRequest == null) {
            throw new InvalidArgumentException();
        }

        request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));
        return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    @Override
    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
        ActionForward forward = super.prepareCreate(mapping, form, request, response);
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) request.getAttribute("roomsPunctualSchedulingBean");
        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);

        if (roomsReserveRequest == null) {
            throw new InvalidArgumentException();
        }

        bean.setRoomsReserveRequest(roomsReserveRequest);
        return forward;
    }

    @Override
    public ActionForward createRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {
        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();

        try {
            CreateRoomsPunctualScheduling.run(bean);
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            return mapping.findForward("prepareFinalizeCreation");
        }

        PunctualRoomsOccupationRequest roomsReserveRequest = bean.getRoomsReserveRequest();
        if (roomsReserveRequest == null) {
            throw new InvalidArgumentException();
        }

        request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));
        return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    public ActionForward openRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {

        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
        try {
            OpenPunctualRoomsOccupationRequest.run(roomsReserveRequest, getLoggedPerson(request));
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }
        return seeRoomsReserveRequests(mapping, form, request, response);
    }

    public ActionForward openRequestAndReturnToSeeRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {

        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
        try {
            OpenPunctualRoomsOccupationRequest.run(roomsReserveRequest, getLoggedPerson(request));
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }

        return seeSpecifiedRoomsReserveRequest(mapping, form, request, response);
    }

    public ActionForward closeRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {

        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
        try {
            ClosePunctualRoomsOccupationRequest.run(roomsReserveRequest, getLoggedPerson(request));
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }
        return seeRoomsReserveRequests(mapping, form, request, response);
    }

    public ActionForward closeRequestAndReturnToSeeRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {

        PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
        try {
            ClosePunctualRoomsOccupationRequest.run(roomsReserveRequest, getLoggedPerson(request));
        } catch (DomainException domainException) {
            saveMessages(request, domainException);
        }
        return seeSpecifiedRoomsReserveRequest(mapping, form, request, response);
    }

    // Private Methods

    private ActionForward createNewRoomsReserveComment(ActionMapping mapping, HttpServletRequest request, boolean reOpen,
            boolean resolveRequest) throws FenixFilterException, FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("roomsReserveBeanWithNewComment");
        RoomsReserveBean bean = (RoomsReserveBean) viewState.getMetaObject().getObject();

        try {
            bean.setRequestor(getLoggedPerson(request));
            executeService("CreateNewRoomsReserveComment", new Object[] { bean, reOpen, resolveRequest });
        } catch (DomainException e) {
            saveMessages(request, e);
        }

        RenderUtils.invalidateViewState("roomsReserveNewComment");

        bean.setDescription(null);
        request.setAttribute("roomsReserveBean", bean);
        return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    private PunctualRoomsOccupationRequest getRoomsReserveRequest(final HttpServletRequest request) {
        final String punctualReserveIDString = request.getParameter("reserveRequestID");
        final Integer punctualReserveID = Integer.valueOf(punctualReserveIDString);
        return rootDomainObject.readPunctualRoomsOccupationRequestByOID(punctualReserveID);
    }

    private void saveMessages(HttpServletRequest request, DomainException e) {
        ActionMessages actionMessages = new ActionMessages();
        actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
        saveMessages(request, actionMessages);
    }
}