package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.utl.fenix.utils.Pair;

public class AcademicCalendarsManagementDA extends FenixDispatchAction {

    public ActionForward prepareChooseCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<AcademicCalendar> academicCalendars = rootDomainObject.getAcademicCalendars();
	request.setAttribute("academicCalendars", academicCalendars);
	return mapping.findForward("prepareChooseCalendar");
    }
   
    public ActionForward prepareCreateEntryForAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	request.setAttribute("calendarEntryBean", new CalendarEntryBean(academicCalendar));
	return mapping.findForward("prepareCreateCalendarEntry");
    }

    public ActionForward prepareCreateEntryForAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	request.setAttribute("calendarEntryBean", new CalendarEntryBean(academicCalendarEntry));
	return mapping.findForward("prepareCreateCalendarEntry");
    }
    
    public ActionForward chooseCalendarEntryTypePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	IViewState viewState = RenderUtils.getViewState();	
	CalendarEntryBean bean = (CalendarEntryBean) viewState.getMetaObject().getObject();		
	request.setAttribute("calendarEntryBean", bean);
	return mapping.findForward("prepareCreateCalendarEntry");
    }
   
    public ActionForward viewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	return viewAcademicCalendarForAcademicCalendar(mapping, request, academicCalendar);
    }
    
    public ActionForward viewAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	return viewAcademicCalendarForEntries(mapping, request, academicCalendarEntry);
    }
    
    public ActionForward prepareEditAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	request.setAttribute("academicCalendar", academicCalendar);			
	return mapping.findForward("prepareEditAcademicCalendar");
    } 
    
    public ActionForward prepareEditAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	request.setAttribute("calendarEntry", academicCalendarEntry);			
	return mapping.findForward("prepareCreateCalendarEntry");
    } 
    
    public ActionForward deleteAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);	
	AcademicCalendar academicCalendar = academicCalendarEntry.getAcademicCalendar();
	AcademicCalendarEntry parentEntry = academicCalendarEntry.getParentEntry();
	
	try {
	    executeService("DeleteAcademicCalendarEntry", new Object[] {academicCalendarEntry});
	    
	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    return viewAcademicCalendarForEntries(mapping, request, academicCalendarEntry);
	}
	
	if(parentEntry != null) {
	    return viewAcademicCalendarForEntries(mapping, request, parentEntry);
	} else {
	    return viewAcademicCalendarForAcademicCalendar(mapping, request, academicCalendar);
	}	
    }
    
    public ActionForward deleteAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);	
	try {
	    executeService("DeleteAcademicCalendar", new Object[] {academicCalendar});
	    
	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    return viewAcademicCalendarForAcademicCalendar(mapping, request, academicCalendar);
	}	
	
	return prepareChooseCalendar(mapping, actionForm, request, response);
    }
    
    // Private Methods
    
    private ActionForward viewAcademicCalendarForAcademicCalendar(ActionMapping mapping, HttpServletRequest request, AcademicCalendar academicCalendar) {
	List<AcademicCalendarEntry> entries = academicCalendar.getEntriesOrderByDate();
	request.setAttribute("academicCalendar", academicCalendar);
	generateTimeLine(request, entries);
	
	return mapping.findForward("viewAcademicCalendar");
    }

    private ActionForward viewAcademicCalendarForEntries(ActionMapping mapping, HttpServletRequest request, AcademicCalendarEntry academicCalendarEntry) {
	List<AcademicCalendarEntry> entries = academicCalendarEntry.getAcademicCalendar().getEntriesOrderByDate();
	request.setAttribute("calendarEntry", academicCalendarEntry);
	generateTimeLine(request, entries);
	
	return mapping.findForward("viewAcademicCalendar");
    } 
    
    private void generateTimeLine(HttpServletRequest request, List<AcademicCalendarEntry> entries) {
	generateEntriesTree(request, entries);
	generateMonths(request, entries);	
    }

    private void generateEntriesTree(HttpServletRequest request, List<AcademicCalendarEntry> entries) {
	List<Pair<Integer, AcademicCalendarEntry>> result = new ArrayList<Pair<Integer, AcademicCalendarEntry>>();
	for (AcademicCalendarEntry entry : entries) {
	    getSubEntriesTree(entry, result, -1);
	}
	request.setAttribute("entries", result);
    }

    private void generateMonths(HttpServletRequest request, List<AcademicCalendarEntry> entries) {
	if (!entries.isEmpty()) {
	    
	    DateTime firstMonthDateTime = entries.get(0).getBegin().withDayOfMonth(1);
	    DateTime lastMontDateTime = entries.get(entries.size() - 1).getEnd().withDayOfMonth(1);
	    List<DateTime> months = new ArrayList<DateTime>();
	    	    	      	   	  
	    while ((firstMonthDateTime.getYear() < lastMontDateTime.getYear())
		    || (firstMonthDateTime.getYear() == lastMontDateTime.getYear() && firstMonthDateTime
			    .getMonthOfYear() <= lastMontDateTime.getMonthOfYear())) {
		
		months.add(firstMonthDateTime);
		firstMonthDateTime = firstMonthDateTime.plusMonths(1);
	    }
	    
	    request.setAttribute("months", months);
	}
    }
    
    private List<Pair<Integer, AcademicCalendarEntry>> getSubEntriesTree(AcademicCalendarEntry entry,
	    List<Pair<Integer, AcademicCalendarEntry>> result, int order) {
	Integer newOrder = order + 1;
	result.add(new Pair<Integer, AcademicCalendarEntry>(newOrder, entry));
	for (AcademicCalendarEntry subEntry : entry.getSubEntriesOrderByDate()) {
	    getSubEntriesTree(subEntry, result, newOrder.intValue());
	}
	return result;
    }

    private AcademicCalendar getAcademicCalendarFromParameter(final HttpServletRequest request) {
	final String calendarIDString = request.getParameter("academicCalendarID");
	final Integer calendarID = Integer.valueOf(calendarIDString);
	return rootDomainObject.readAcademicCalendarByOID(calendarID);
    }
    
    private AcademicCalendarEntry getAcademicCalendarEntryFromParameter(final HttpServletRequest request) {
	final String calendarIDString = request.getParameter("academicCalendarEntryID");
	final Integer calendarID = Integer.valueOf(calendarIDString);
	return rootDomainObject.readAcademicCalendarEntryByOID(calendarID);
    }    
}
