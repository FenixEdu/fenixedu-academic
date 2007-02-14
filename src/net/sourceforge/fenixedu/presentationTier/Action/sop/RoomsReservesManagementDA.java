package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.RequestState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class RoomsReservesManagementDA extends RoomsPunctualSchedulingDA {
       
    public ActionForward seeRoomsReserveRequests(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws FenixServiceException, FenixFilterException, InvalidArgumentException {	
		
	Person person = getLoggedPerson(request);
	
	List<PunctualRoomsOccupationRequest> personRequests = person.getPunctualRoomsOccupationRequestsToProcessOrderByDate();
	Set<PunctualRoomsOccupationRequest> openedRequests = PunctualRoomsOccupationRequest.getRequestsByTypeAndDiferentOwnerOrderByDate(RequestState.OPEN, person);
	Set<PunctualRoomsOccupationRequest> newRequests = PunctualRoomsOccupationRequest.getRequestsByTypeOrderByDate(RequestState.NEW);
	
	Set<PunctualRoomsOccupationRequest> resolvedRequests = PunctualRoomsOccupationRequest.getResolvedRequestsOrderByMoreRecentComment();	
	CollectionPager<DomainObjectActionLog> collectionPager = new CollectionPager<DomainObjectActionLog>(resolvedRequests != null ? resolvedRequests : new ArrayList(), 10);	
	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1); 
	
	request.setAttribute("pageNumber", pageNumber);	
	request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));		
	
	request.setAttribute("personRequests", personRequests);
	request.setAttribute("openedRequests", openedRequests);
	request.setAttribute("newRequests", newRequests);
	request.setAttribute("resolvedRequests", resolvedRequests);
	
	return mapping.findForward("seeRoomsReserveRequests");	
    }
    
    public ActionForward seeSpecifiedRoomsReserveRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws FenixServiceException, FenixFilterException, InvalidArgumentException {	
			
	Person loggedPerson = getLoggedPerson(request);
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);	
	executeService("MarkPunctualRoomsOccupationCommentsAsRead", new Object[] {roomsReserveRequest, false});	
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(loggedPerson, roomsReserveRequest));	
	return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }
             
    public ActionForward createNewRoomsReserveComment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {	
	return createNewRoomsReserveComment(mapping, request, false, false);
    }
       
    public ActionForward createNewRoomsReserveCommentAndMakeRequestResolved(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {		
	return createNewRoomsReserveComment(mapping, request, false, true);
    }   
        
    @Override
    public ActionForward deleteRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException, FenixFilterException, FenixServiceException {
	GenericEvent genericEventFromParameter = getGenericEventFromParameter(request);
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	executeService("DeleteGenericEvent", new Object[] { genericEventFromParameter });
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));		
	return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    @Override
    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {
	ActionForward forward = super.prepareView(mapping, form, request, response);
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) request.getAttribute("roomsPunctualSchedulingBean");
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	
	if(roomsReserveRequest == null) {
	    throw new InvalidArgumentException();
	}
	
	bean.setRoomsReserveRequest(roomsReserveRequest);
	return forward;
    }
    
    @Override
    public ActionForward editRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();	
	
	try {
            executeService("EditRoomsPunctualScheduling", new Object[] {bean});	    
       
	} catch(DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);	
            return mapping.findForward("prepareFinalizeCreation");        
	} 	
	
	PunctualRoomsOccupationRequest roomsReserveRequest = bean.getRoomsReserveRequest();
	if(roomsReserveRequest == null) {
	    throw new InvalidArgumentException();
	}	
	
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));		
	return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }

    @Override
    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
	ActionForward forward = super.prepareCreate(mapping, form, request, response);
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) request.getAttribute("roomsPunctualSchedulingBean");
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	
	if(roomsReserveRequest == null) {
	    throw new InvalidArgumentException();
	}
	
	bean.setRoomsReserveRequest(roomsReserveRequest);
	return forward;
    }

    @Override
    public ActionForward createRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixServiceException, FenixFilterException, InvalidArgumentException {
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
	
	try {
            executeService("CreateRoomsPunctualScheduling", new Object[] {bean});	           
	} catch(DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);	
            return mapping.findForward("prepareFinalizeCreation");        
	} 
		
	PunctualRoomsOccupationRequest roomsReserveRequest = bean.getRoomsReserveRequest();
	if(roomsReserveRequest == null) {
	    throw new InvalidArgumentException();
	}	
	
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(getLoggedPerson(request), roomsReserveRequest));	
	return mapping.findForward("seeSpecifiedRoomsReserveRequest");
    }
    
    public ActionForward openRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
		
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	executeService("OpenPunctualRoomsOccupationRequest", new Object[] {roomsReserveRequest, getLoggedPerson(request)});	    	
	return seeRoomsReserveRequests(mapping, form, request, response);	
    } 
    
    public ActionForward openRequestAndReturnToSeeRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
		
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	executeService("OpenPunctualRoomsOccupationRequest", new Object[] {roomsReserveRequest, getLoggedPerson(request)});	    	
	return seeSpecifiedRoomsReserveRequest(mapping, form, request, response);	
    } 

    public ActionForward closeRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
		
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	executeService("ClosePunctualRoomsOccupationRequest", new Object[] {roomsReserveRequest, getLoggedPerson(request)});	    	
	return seeRoomsReserveRequests(mapping, form, request, response);	
    }  
    
    public ActionForward closeRequestAndReturnToSeeRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
		
	PunctualRoomsOccupationRequest roomsReserveRequest = getRoomsReserveRequest(request);
	executeService("ClosePunctualRoomsOccupationRequest", new Object[] {roomsReserveRequest, getLoggedPerson(request)});	    	
	return seeSpecifiedRoomsReserveRequest(mapping, form, request, response);	
    }  
    
    // Private Methods
    
    private ActionForward createNewRoomsReserveComment(ActionMapping mapping, HttpServletRequest request, boolean reOpen, boolean resolveRequest) 
		throws FenixFilterException, FenixServiceException {
	
	IViewState viewState = RenderUtils.getViewState("roomsReserveBeanWithNewComment");
	RoomsReserveBean bean = (RoomsReserveBean) viewState.getMetaObject().getObject();
	
	try {	    
	    bean.setRequestor(getLoggedPerson(request));
	    executeService("CreateNewRoomsReserveComment", new Object[] {bean, reOpen, resolveRequest});	    
	} catch (DomainException e) {
	    saveMessages(request, e);	    
	}
	
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
