package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class AcademicCalendarsManagementDA extends FenixDispatchAction {

    public ActionForward prepareCreateAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
	Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());
	
	CalendarEntryBean bean = CalendarEntryBean.createAcademicCalendarBean(begin, end);	
	request.setAttribute("parentEntryBean", bean);
	
	return mapping.findForward("prepareCreateCalendarEntry");	
    }
    
    public ActionForward prepareChooseCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<AcademicCalendarRootEntry> academicCalendars = rootDomainObject.getAcademicCalendars();
	request.setAttribute("academicCalendars", academicCalendars);
	
	return mapping.findForward("prepareChooseCalendar");
    }
         
    public ActionForward prepareViewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendar = getAcademicCalendarEntryFromParameter(request);
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
	Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());

	CalendarEntryBean bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry((AcademicCalendarRootEntry) academicCalendar, academicCalendar, begin, end);
	
	return generateGanttDiagram(mapping, request, bean);	
    }

    public ActionForward viewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("datesToDisplayID");
	
	return generateGanttDiagram(mapping, request, bean);			
    }    
      
    public ActionForward viewAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
	AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);
	
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);

	CalendarEntryBean bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entry, beginPartial, endPartial);
		
	return generateGanttDiagram(mapping, request, bean);
    }    
        
    public ActionForward chooseCalendarEntryTypePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("calendarEntryBeanWithType");		
	request.setAttribute("parentEntryBean", bean);
	
	return mapping.findForward("prepareCreateCalendarEntry");
    }
    
    public ActionForward prepareCreateEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry parentEntry = getAcademicCalendarEntryFromParameter(request);	
	AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);
	
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);	
	
	request.setAttribute("parentEntryBean", CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, parentEntry, beginPartial, endPartial));
	
	return mapping.findForward("prepareCreateCalendarEntry");
    }        
    
    public ActionForward createEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
    
	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("createdEntryBeanID");
		
	AcademicCalendarEntry entry = null; 
	try {
	    entry = (AcademicCalendarEntry) executeService("CreateAcademicCalendarEntry", new Object[] {bean, true});
	    
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("parentEntryBean", bean);	
	    return mapping.findForward("prepareCreateCalendarEntry");
	}
	
	return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(entry.getRootEntry(), entry, bean.getBeginDateToDisplay(), bean.getEndDateToDisplay()));	
    }
    
    public ActionForward prepareEditEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
	AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);
	
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);		
	
	request.setAttribute("entryBean", CalendarEntryBean.createCalendarEntryBeanToEditEntry(rootEntry, entry, beginPartial, endPartial));
	
	return mapping.findForward("prepareCreateCalendarEntry");
    } 
    
    public ActionForward editEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
    
	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("editedEntryBeanID");
	
	AcademicCalendarEntry entry = null;
	try {
	    entry = (AcademicCalendarEntry) executeService("CreateAcademicCalendarEntry", new Object[] {bean, false});
	    
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("entryBean", bean);		
	    return mapping.findForward("prepareCreateCalendarEntry");
	}
	
	return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(entry.getRootEntry(), entry, bean.getBeginDateToDisplay(), bean.getEndDateToDisplay()));
    }
                  
    public ActionForward deleteEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);	
	AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);
	
	AcademicCalendarRootEntry entryRootEntry = entry.getRootEntry();
	AcademicCalendarEntry entryParentEntry = entry.getParentEntry();
	
	boolean deletedRootEntry = entry.isRootEntry();
	
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);
	
	try {
	    executeService("DeleteAcademicCalendarEntry", new Object[] {entry});

	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entry, beginPartial, endPartial));
	}
	
	if(deletedRootEntry) {
	    return prepareChooseCalendar(mapping, actionForm, request, response);    
	 
	} else if(entryParentEntry != null) {	    	    
	    return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entryParentEntry, beginPartial, endPartial));
	    
	} else {	  	   
	    return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entryRootEntry, beginPartial, endPartial));
	}		
    }
         
    // Private Methods

    private ActionForward generateGanttDiagram(ActionMapping mapping, HttpServletRequest request, CalendarEntryBean bean) throws InvalidArgumentException {		
	
	YearMonthDay beginDate = bean.getBeginDateToDisplayInYearMonthDayFormat();
	YearMonthDay endDate = bean.getEndDateToDisplayInYearMonthDayFormat();

	List<GanttDiagramEvent> newEntries = generateEntriesTree(request, bean.getRootEntry(), beginDate, endDate);	
	GanttDiagram diagram = GanttDiagram.getNewTotalGanttDiagram(newEntries, beginDate, endDate);

	request.setAttribute("entryBean", bean);
	request.setAttribute("ganttDiagram", diagram);	

	return mapping.findForward("viewAcademicCalendar");
    }

    private List<GanttDiagramEvent> generateEntriesTree(HttpServletRequest request, AcademicCalendarRootEntry academicCalendar, YearMonthDay begin, YearMonthDay end) {

	DateTime beginDateTime = begin.toDateTimeAtMidnight();
	DateTime endDateTime = end.toDateTimeAtMidnight();

	List<GanttDiagramEvent> result = new ArrayList<GanttDiagramEvent>();
	for (AcademicCalendarEntry entry : academicCalendar.getChildEntriesWithTemplateEntriesOrderByDate(beginDateTime, endDateTime)) {
	    if(!result.contains(entry)) {
		getSubEntriesTree(entry, result, beginDateTime, endDateTime);
	    }
	}
	return result;
    }

    private void getSubEntriesTree(AcademicCalendarEntry entry, List<GanttDiagramEvent> result, DateTime begin, DateTime end) {			
	result.add(entry);
	for (AcademicCalendarEntry subEntry : entry.getChildEntriesWithTemplateEntriesOrderByDate(begin, end)) {
	    getSubEntriesTree(subEntry, result, begin, end);
	}
    }

    private AcademicCalendarEntry getAcademicCalendarEntryFromParameter(final HttpServletRequest request) {
	final String calendarIDString = request.getParameter("entryID");
	final Integer calendarID = Integer.valueOf(calendarIDString);
	return rootDomainObject.readAcademicCalendarEntryByOID(calendarID);
    } 

    private AcademicCalendarRootEntry getAcademicCalendarRootEntryFromParameter(final HttpServletRequest request) {
	final String calendarIDString = request.getParameter("rootEntryID");
	final Integer calendarID = Integer.valueOf(calendarIDString);
	return (AcademicCalendarRootEntry) rootDomainObject.readAcademicCalendarEntryByOID(calendarID);
    } 
    
    private Partial getBeginFromParameter(final HttpServletRequest request) {
	final String date = request.getParameter("begin");
	return CalendarEntryBean.getPartialFromString(date);
    } 

    private Partial getEndFromParameter(final HttpServletRequest request) {
	final String date = request.getParameter("end");
	return CalendarEntryBean.getPartialFromString(date);
    } 
}
