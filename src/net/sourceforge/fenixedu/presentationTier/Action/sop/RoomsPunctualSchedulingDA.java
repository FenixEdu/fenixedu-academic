package net.sourceforge.fenixedu.presentationTier.Action.sop;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sop.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.YearMonthDay;

public class RoomsPunctualSchedulingDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {	
	Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();	
	if(!genericEvents.isEmpty()) {
	    
	    RoomsPunctualSchedulingBean bean = null;
	    IViewState viewState = RenderUtils.getViewState();
	    if(viewState == null) {
                bean = new RoomsPunctualSchedulingBean();
            } else {
                bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();	    	   	    
            }
	    
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewWeeklyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);	
            request.setAttribute("ganttDiagram", diagram);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
	}
	return mapping.findForward("prepareRoomsPunctualScheduling");
    }     
    
    public ActionForward prepareViewDailyView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {	
	Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();	
	if(!genericEvents.isEmpty()) {            
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewDailyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);
            RoomsPunctualSchedulingBean bean = new RoomsPunctualSchedulingBean();
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            request.setAttribute("ganttDiagram", diagram);
	}
	return mapping.findForward("prepareRoomsPunctualScheduling");
    }  
    
    public ActionForward prepareViewMonthlyView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {	
	Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();	
	if(!genericEvents.isEmpty()) {            
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewMonthlyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);	
            RoomsPunctualSchedulingBean bean = new RoomsPunctualSchedulingBean();
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            request.setAttribute("ganttDiagram", diagram);
	}
	return mapping.findForward("prepareRoomsPunctualScheduling");
    }  
         
    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {		
	GenericEvent genericEvent = getGenericEventFromParameter(request);
	request.setAttribute("genericEvent", genericEvent);
	request.setAttribute("roomsPunctualSchedulingBean", new RoomsPunctualSchedulingBean(genericEvent));
	return mapping.findForward("prepareViewRoomsPunctualScheduling");
    }  
    
    public ActionForward deleteRoomsPunctualScheduling(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException,
	    FenixFilterException, FenixServiceException {
	
	GenericEvent genericEventFromParameter = getGenericEventFromParameter(request);
	executeService("DeleteGenericEvent", new Object[] { genericEventFromParameter });	
	return prepare(mapping, form, request, response);
    }  
           
    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {			
	
	RoomsPunctualSchedulingBean bean = null;		
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithPeriodType");				
	if(viewState == null) {
	    viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithInfo");	
	}
	
	if(viewState == null) {
	    bean = new RoomsPunctualSchedulingBean();
	} else {
	    bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();	    	   	    
	}
	   		
	request.setAttribute("roomsPunctualSchedulingBean", bean);
	return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
    }  
    
    public ActionForward prepareFinalizeCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {	
	
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithInfo");
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
	request.setAttribute("roomsPunctualSchedulingBean", bean);
	
	if (bean.getBegin() != null && bean.getEnd() != null) {
	    try {
		executeService("CreateOccupationPeriod", new Object[] { bean.getBegin(), bean.getEnd() });
	    } catch (DomainException domainException) {
		saveMessages(request, domainException);
		return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
	    }
	}
	
	if(bean.getBeginTime().isAfter(bean.getEndTime()) || bean.getBeginTime().isEqual(bean.getEndTime())) {
	    saveMessages(request, "error.beginTime.after.or.equal.then.endTime");
	    return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
	}
	
	return mapping.findForward("prepareFinalizeCreation");
    }    
        
    public ActionForward associateNewRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {	
	
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithNewRoom");				
	if(viewState == null) {
	    viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");	
	}
		
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();	
	bean.addNewRoom(bean.getSelectedRoom());
	bean.setSelectedRoom(null);
		
	request.setAttribute("roomsPunctualSchedulingBean", bean);	
	RenderUtils.invalidateViewState("roomsPunctualSchedulingWithNewRoom");	
	return mapping.findForward("prepareFinalizeCreation");
    }  
    
    public ActionForward removeRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {	
	
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingBeanHidden");				
	if(viewState == null) {
	    viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");	
	}
		
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();		
	String[] selectedRooms = request.getParameterValues("selectedRoom");	
	if (selectedRooms != null && selectedRooms.length > 0) {	    		   
            for (int i = 0; i < selectedRooms.length; i++) {
        	Integer roomIdInternal = Integer.valueOf(selectedRooms[i]);
        	OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(roomIdInternal);
        	bean.removeRoom(room);
            }
	}
	
	request.setAttribute("roomsPunctualSchedulingBean", bean);	
	return mapping.findForward("prepareFinalizeCreation");
    }  
    
    public ActionForward associateNewRoomToEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {		
	associateNewRoom(mapping, form, request, response);	
	return mapping.findForward("prepareViewRoomsPunctualScheduling");	
    } 
    
    public ActionForward removeRoomToEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {	
	removeRoom(mapping, form, request, response);	
	return mapping.findForward("prepareViewRoomsPunctualScheduling");		
    }  
    
    public ActionForward createRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    	throws FenixServiceException, FenixFilterException, InvalidArgumentException {	
		
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
	
	try {
            executeService("CreateRoomsPunctualScheduling", new Object[] {bean});	    
       
	} catch(DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);	
            return mapping.findForward("prepareFinalizeCreation");        
	} 
	
	request.setAttribute("roomsPunctualSchedulingBean", bean);	
	return prepare(mapping, form, request, response);
    }    
    
    public ActionForward editRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws FenixServiceException, FenixFilterException, InvalidArgumentException {	
			
	IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
	RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
	
	try {
            executeService("EditRoomsPunctualScheduling", new Object[] {bean});	    
       
	} catch(DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);	
            return mapping.findForward("prepareFinalizeCreation");        
	} 
	
	return prepare(mapping, form, request, response);
    }
          
    // Private Methods     
    
    private YearMonthDay getFirstDay(HttpServletRequest request) {
	YearMonthDay firstDay = getFirstDayFromParameter(request);
	if(firstDay == null) {
	    firstDay = new YearMonthDay();
	}
	return firstDay;
    } 
    
    private YearMonthDay getFirstDayFromParameter(final HttpServletRequest request) {
	final String firstDayString = request.getParameter("firstDay");
	if (!StringUtils.isEmpty(firstDayString)) {
	    int day = Integer.parseInt(firstDayString.substring(0, 2));
	    int month = Integer.parseInt(firstDayString.substring(2, 4));
	    int year = Integer.parseInt(firstDayString.substring(4, 8));

	    if (year == 0 || month == 0 || day == 0) {
		return null;
	    }
	    return new YearMonthDay(year, month, day);
	}
	return null;
    }
    
    private void saveMessages(HttpServletRequest request, DomainException e) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage(e.getMessage(), e.getArgs()));
	saveMessages(request, actionMessages);
    }
    
    private void saveMessages(HttpServletRequest request, String key) {
	ActionMessages actionMessages = new ActionMessages();
	actionMessages.add("", new ActionMessage(key));
	saveMessages(request, actionMessages);
    }
    
    private GenericEvent getGenericEventFromParameter(final HttpServletRequest request) {
	final String genericEventIDString = request.getParameter("genericEventID");
	final Integer genericEventID = Integer.valueOf(genericEventIDString);
	return rootDomainObject.readGenericEventByOID(genericEventID);
    }    
}
