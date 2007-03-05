package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class RoomsReserveManagementDA extends FenixDispatchAction {

    public ActionForward viewReserves(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	List<PunctualRoomsOccupationRequest> requests = readAndSetPunctualRequests(request);	
	Set<InfoGenericEvent> result = getInfoGenericEvents(requests);
	
	if(!result.isEmpty()) {
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewWeeklyGanttDiagram(new ArrayList<GanttDiagramEvent>(result), firstDay);	
            request.setAttribute("ganttDiagram", diagram);
	}
        	
	return mapping.findForward("viewRoomsReserves");
    }      

    public ActionForward viewReservesDailyView(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	List<PunctualRoomsOccupationRequest> requests = readAndSetPunctualRequests(request);	
	Set<InfoGenericEvent> result = getInfoGenericEvents(requests);
	
	if(!result.isEmpty()) {
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewDailyGanttDiagram(new ArrayList<GanttDiagramEvent>(result), firstDay);	
            request.setAttribute("ganttDiagram", diagram);
	}
        	
	return mapping.findForward("viewRoomsReserves");
    }
  
    public ActionForward viewReservesMonthlyView(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws InvalidArgumentException {

	List<PunctualRoomsOccupationRequest> requests = readAndSetPunctualRequests(request);	
	Set<InfoGenericEvent> result = getInfoGenericEvents(requests);
	
	if(!result.isEmpty()) {
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram = GanttDiagram.getNewMonthlyGanttDiagram(new ArrayList<GanttDiagramEvent>(result), firstDay);	
            request.setAttribute("ganttDiagram", diagram);
	}
        	
	return mapping.findForward("viewRoomsReserves");
    }
    
    public ActionForward prepareCreateNewReserve(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	Person person = getLoggedPerson(request);
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(person));
	return mapping.findForward("createNewRoomsReserve");
    }
    
    public ActionForward createRoomsReserve(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IViewState viewState = RenderUtils.getViewState("roomsReserveWithDescriptions");
	RoomsReserveBean bean = (RoomsReserveBean) viewState.getMetaObject().getObject();
		
	PunctualRoomsOccupationRequest punctualRequest = null;
	try {	    
	    punctualRequest = (PunctualRoomsOccupationRequest) executeService("CreateNewRoomsReserve", new Object[] {bean});
	    
	} catch (DomainException e) {
	    saveMessages(request, e);
	    request.setAttribute("roomsReserveBean", bean);
	    return mapping.findForward("createNewRoomsReserve");
	}
	
	bean.setDescription(null);
	bean.setReserveRequest(punctualRequest);
	
	request.setAttribute("roomsReserveBean", bean);	
	return mapping.findForward("seeSpecifiedRoomsReserve");
    }
    
    public ActionForward seeSpecifiedRoomsReserve(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException, InvalidArgumentException {
	
	Person loggedPerson = getLoggedPerson(request);
	PunctualRoomsOccupationRequest punctualRoomsReserve = getPunctualRoomsReserve(request);
	if(!punctualRoomsReserve.getRequestor().equals(loggedPerson)) {
	    saveMessages(request, "label.not.authorized.action");
	    return viewReserves(mapping, form, request, response);
	}
	
	executeService("MarkPunctualRoomsOccupationCommentsAsRead", new Object[] {punctualRoomsReserve, true});	
	request.setAttribute("roomsReserveBean", new RoomsReserveBean(loggedPerson, punctualRoomsReserve));
	return mapping.findForward("seeSpecifiedRoomsReserve");
    }
    
    public ActionForward createNewComment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
	return createNewRoomsReserveComment(mapping, request, false, false);
    }
       
    public ActionForward createNewCommentAndReOpenRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
	return createNewRoomsReserveComment(mapping, request, true, false);
    }      
    
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		
	PunctualRoomsOccupationRequest punctualRoomsReserve = getPunctualRoomsReserve(request);
	request.setAttribute("roomsReserveRequest", punctualRoomsReserve);
	return mapping.findForward("createNewRoomsReserve");
    }    
    
    // Private Methods
    
    private List<PunctualRoomsOccupationRequest> readAndSetPunctualRequests(HttpServletRequest request) {
	Person person = getLoggedPerson(request);	
	List<PunctualRoomsOccupationRequest> requests = person.getPunctualRoomsOccupationRequestsOrderByMoreRecentComment();		
	CollectionPager<DomainObjectActionLog> collectionPager = new CollectionPager<DomainObjectActionLog>(requests != null ? requests : new ArrayList(), 10);
	
	final String pageNumberString = request.getParameter("pageNumber");
	final Integer pageNumber = !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1); 
	
	request.setAttribute("pageNumber", pageNumber);	
	request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));	
	request.setAttribute("requests", collectionPager.getPage(pageNumber));
	return requests;
    }
    
    private Set<InfoGenericEvent> getInfoGenericEvents(List<PunctualRoomsOccupationRequest> requests) {
	Set<InfoGenericEvent> result = new HashSet<InfoGenericEvent>();
	for (PunctualRoomsOccupationRequest occupationRequest: requests) {
	    result.addAll(occupationRequest.getActiveGenericEvents());
	}
	return result;
    }
        
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
    
    private ActionForward createNewRoomsReserveComment(ActionMapping mapping, HttpServletRequest request, boolean reOpen, boolean resolveRequest)    
    	throws FenixFilterException, FenixServiceException {
	
	IViewState viewState = RenderUtils.getViewState("roomsReserveBeanWithNewComment");
	RoomsReserveBean bean = (RoomsReserveBean) viewState.getMetaObject().getObject();
	
	try {	    
	    executeService("CreateNewRoomsReserveComment", new Object[] {bean, reOpen, resolveRequest});	    
	} catch (DomainException e) {
	    saveMessages(request, e);	    
	}
	
	RenderUtils.invalidateViewState("roomsReserveNewComment");
	
	bean.setDescription(null);	
	request.setAttribute("roomsReserveBean", bean);	
	return mapping.findForward("seeSpecifiedRoomsReserve");
    }
    
    private PunctualRoomsOccupationRequest getPunctualRoomsReserve(final HttpServletRequest request) {
	final String punctualReserveIDString = request.getParameter("punctualReserveID");
	final Integer punctualReserveID = Integer.valueOf(punctualReserveIDString);
	return rootDomainObject.readPunctualRoomsOccupationRequestByOID(punctualReserveID);
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
}
