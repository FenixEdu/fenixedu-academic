package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

public class AcademicCalendarsManagementDA extends FenixDispatchAction {

    public ActionForward prepareChooseCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	List<AcademicCalendar> academicCalendars = rootDomainObject.getAcademicCalendars();
	request.setAttribute("academicCalendars", academicCalendars);
	return mapping.findForward("prepareChooseCalendar");
    }
    
    public ActionForward prepareViewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
	Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());

	CalendarEntryBean bean = new CalendarEntryBean(academicCalendar, begin, end);

	request.setAttribute("datesToDisplayBean", bean);
	return viewAcademicCalendarInInterval(mapping, request, bean.getAcademicCalendar(), bean.getBeginDateToDisplay(), bean.getEndDateToDisplay());	
    }

    public ActionForward viewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("datesToDisplay");
	if(bean == null) {
	    AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	    Partial beginPartial = getBeginFromParameter(request);
	    Partial endPartial = getEndFromParameter(request);
	    bean = new CalendarEntryBean(academicCalendar, beginPartial, endPartial);
	} 
	
	request.setAttribute("datesToDisplayBean", bean);	
	return viewAcademicCalendarInInterval(mapping, request, bean.getAcademicCalendar(), bean.getBeginDateToDisplay(), bean.getEndDateToDisplay());			
    }    

    public ActionForward viewAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);

	CalendarEntryBean bean = new CalendarEntryBean(academicCalendarEntry.getAcademicCalendar(), beginPartial, endPartial);
	request.setAttribute("datesToDisplayBean", bean);
	
	return viewAcademicCalendarEntryInInterval(mapping, request, beginPartial, endPartial, academicCalendarEntry);
    }

    public ActionForward chooseCalendarEntryTypePostBack(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	CalendarEntryBean bean = (CalendarEntryBean) getRenderedObject("calendarEntryBeanWithType");		
	request.setAttribute("calendarEntryBean", bean);
	return mapping.findForward("prepareCreateCalendarEntry");
    }
    
    public ActionForward prepareCreateEntryForAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);	
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);	
	request.setAttribute("calendarEntryBean", new CalendarEntryBean(academicCalendar, beginPartial, endPartial));
	return mapping.findForward("prepareCreateCalendarEntry");
    }            
    
    public ActionForward prepareCreateEntryForAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);
	request.setAttribute("calendarEntryBean", new CalendarEntryBean(academicCalendarEntry, beginPartial, endPartial));
	return mapping.findForward("prepareCreateCalendarEntry");
    }
        
    public ActionForward deleteAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);	
	AcademicCalendar academicCalendar = academicCalendarEntry.getAcademicCalendar();
	AcademicCalendarEntry parentEntry = academicCalendarEntry.getParentEntry();
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);

	CalendarEntryBean bean = new CalendarEntryBean(academicCalendar, beginPartial, endPartial);
	request.setAttribute("datesToDisplayBean", bean);
	
	try {
	    executeService("DeleteAcademicCalendarEntry", new Object[] {academicCalendarEntry});

	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    return viewAcademicCalendarEntryInInterval(mapping, request, beginPartial, endPartial, academicCalendarEntry);
	}
		
	if(parentEntry != null) {
	    request.setAttribute("academicCalendarEntryID", parentEntry.getIdInternal());
	    return viewAcademicCalendarEntryInInterval(mapping, request, beginPartial, endPartial, parentEntry);
	} else {
	    return viewAcademicCalendarInInterval(mapping, request, academicCalendar, beginPartial, endPartial);
	}	
    }
    
    public ActionForward deleteAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);

	try {
	    executeService("DeleteAcademicCalendar", new Object[] {academicCalendar});

	} catch(DomainException domainException) {
	    addActionMessage(request, domainException.getMessage());
	    CalendarEntryBean bean = new CalendarEntryBean(academicCalendar, beginPartial, endPartial);
	    request.setAttribute("datesToDisplayBean", bean);
	    return viewAcademicCalendarInInterval(mapping, request, academicCalendar, beginPartial, endPartial);
	}	

	return prepareChooseCalendar(mapping, actionForm, request, response);
    }    

    public ActionForward prepareEditAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendar academicCalendar = getAcademicCalendarFromParameter(request);
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);
	
	request.setAttribute("beginDate", CalendarEntryBean.getPartialString(beginPartial));
	request.setAttribute("endDate", CalendarEntryBean.getPartialString(endPartial));	
	request.setAttribute("academicCalendar", academicCalendar);			
	
	return mapping.findForward("prepareEditAcademicCalendar");
    } 

    public ActionForward prepareEditAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	AcademicCalendarEntry academicCalendarEntry = getAcademicCalendarEntryFromParameter(request);
	Partial beginPartial = getBeginFromParameter(request);
	Partial endPartial = getEndFromParameter(request);
	
	request.setAttribute("beginDate", CalendarEntryBean.getPartialString(beginPartial));
	request.setAttribute("endDate", CalendarEntryBean.getPartialString(endPartial));
	request.setAttribute("calendarEntry", academicCalendarEntry);			
	
	return mapping.findForward("prepareCreateCalendarEntry");
    } 

    // Private Methods

    private ActionForward viewAcademicCalendarInInterval(ActionMapping mapping, HttpServletRequest request, AcademicCalendar academicCalendar, Partial begin, Partial end) throws InvalidArgumentException {	

	YearMonthDay beginDate = CalendarEntryBean.getDateFromPartial(begin);
	YearMonthDay endDate = CalendarEntryBean.getDateFromPartial(end);

	List<GanttDiagramEvent> newEntries = generateEntriesTree(request, academicCalendar, beginDate, endDate);	
	GanttDiagram diagram = GanttDiagram.getNewTotalGanttDiagram(newEntries, beginDate, endDate);

	request.setAttribute("academicCalendar", academicCalendar);
	request.setAttribute("ganttDiagram", diagram);		

	return mapping.findForward("viewAcademicCalendar");
    }

    private ActionForward viewAcademicCalendarEntryInInterval(ActionMapping mapping, HttpServletRequest request, Partial begin, Partial end, AcademicCalendarEntry entry) throws InvalidArgumentException {		

	YearMonthDay beginDate = CalendarEntryBean.getDateFromPartial(begin);
	YearMonthDay endDate = CalendarEntryBean.getDateFromPartial(end);

	List<GanttDiagramEvent> newEntries = generateEntriesTree(request, entry.getAcademicCalendar(), beginDate, endDate);	
	GanttDiagram diagram = GanttDiagram.getNewTotalGanttDiagram(newEntries, beginDate, endDate);

	request.setAttribute("calendarEntry", entry);
	request.setAttribute("ganttDiagram", diagram);	

	return mapping.findForward("viewAcademicCalendar");
    }

    private List<GanttDiagramEvent> generateEntriesTree(HttpServletRequest request, AcademicCalendar academicCalendar, YearMonthDay begin, YearMonthDay end) {

	DateTime beginDateTime = begin.toDateTimeAtMidnight();
	DateTime endDateTime = end.toDateTimeAtMidnight();

	List<GanttDiagramEvent> result = new ArrayList<GanttDiagramEvent>();
	for (AcademicCalendarEntry entry : academicCalendar.getEntriesOrderByDate(beginDateTime, endDateTime)) {
	    if(!result.contains(entry)) {
		getSubEntriesTree(entry, result);
	    }
	}
	return result;
    }

    private void getSubEntriesTree(AcademicCalendarEntry entry, List<GanttDiagramEvent> result) {			
	result.add(entry);
	for (AcademicCalendarEntry subEntry : entry.getSubEntriesOrderByDate()) {
	    getSubEntriesTree(subEntry, result);
	}
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

    private Partial getBeginFromParameter(final HttpServletRequest request) {
	final String date = request.getParameter("begin");
	return CalendarEntryBean.getPartialFromString(date);
    } 

    private Partial getEndFromParameter(final HttpServletRequest request) {
	final String date = request.getParameter("end");
	return CalendarEntryBean.getPartialFromString(date);
    } 
}
