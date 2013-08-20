package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement.CreateAcademicCalendarEntry;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement.DeleteAcademicCalendarEntry;
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

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;


@Mapping(module = "academicAdministration", path = "/academicCalendarsManagement", input = "/index.do",
        attribute = "academicCalendarsManagementForm", formBean = "academicCalendarsManagementForm", scope = "request",
        parameter = "method")
@Forwards(
        value = {
                @Forward(name = "viewAcademicCalendar",
                        path = "/academicAdministration/academicCalendarsManagement/viewAcademicCalendar.jsp"),
                @Forward(name = "prepareCreateCalendarEntry",
                        path = "/academicAdministration/academicCalendarsManagement/createCalendarEntry.jsp"),
                @Forward(name = "prepareChooseCalendar",
                        path = "/academicAdministration/academicCalendarsManagement/chooseCalendar.jsp") })
public class AcademicCalendarsManagementDA extends FenixDispatchAction {
    public ActionForward prepareCreateAcademicCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
        Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());

        CalendarEntryBean bean = CalendarEntryBean.createAcademicCalendarBean(begin, end);
        request.setAttribute("parentEntryBean", bean);

        return mapping.findForward("prepareCreateCalendarEntry");
    }

    public ActionForward prepareChooseCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        List<AcademicCalendarRootEntry> academicCalendars = rootDomainObject.getAcademicCalendars();
        request.setAttribute("academicCalendars", academicCalendars);

        return mapping.findForward("prepareChooseCalendar");
    }

    public ActionForward prepareViewAcademicCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicCalendarEntry academicCalendar = getAcademicCalendarEntryFromParameter(request);
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
        Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());

        CalendarEntryBean bean =
                CalendarEntryBean.createCalendarEntryBeanToCreateEntry((AcademicCalendarRootEntry) academicCalendar,
                        academicCalendar, begin, end);

        return generateGanttDiagram(mapping, request, bean);
    }

    public ActionForward viewAcademicCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CalendarEntryBean bean = getRenderedObject("datesToDisplayID");

        YearMonthDay beginDate = bean.getBeginDateToDisplayInYearMonthDayFormat();
        YearMonthDay endDate = bean.getEndDateToDisplayInYearMonthDayFormat();

        if (beginDate.isAfter(endDate)) {
            addActionMessage(request, "error.begin.after.end");
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
            Partial begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
            Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());
            bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry(bean.getRootEntry(), bean.getRootEntry(), begin, end);
            RenderUtils.invalidateViewState("datesToDisplayID");
            return generateGanttDiagram(mapping, request, bean);
        }

        return generateGanttDiagram(mapping, request, bean);
    }

    public ActionForward gotBackToViewEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CalendarEntryBean bean = getRenderedObject();
        return generateGanttDiagram(mapping, request, bean);
    }

    public ActionForward viewAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
        AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);

        Partial beginPartial = getBeginFromParameter(request);
        Partial endPartial = getEndFromParameter(request);

        CalendarEntryBean bean =
                CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entry, beginPartial, endPartial);

        return generateGanttDiagram(mapping, request, bean);
    }

    public ActionForward chooseCalendarEntryTypePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        CalendarEntryBean bean = getRenderedObject("calendarEntryBeanWithType");
        if (bean == null) {
            bean = getRenderedObject("createdEntryBeanID");
        }

        request.setAttribute("parentEntryBean", bean);
        return mapping.findForward("prepareCreateCalendarEntry");
    }

    public ActionForward prepareCreateEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicCalendarEntry parentEntry = getAcademicCalendarEntryFromParameter(request);
        AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);

        Partial beginPartial = getBeginFromParameter(request);
        Partial endPartial = getEndFromParameter(request);

        request.setAttribute("parentEntryBean",
                CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, parentEntry, beginPartial, endPartial));

        return mapping.findForward("prepareCreateCalendarEntry");
    }

    public ActionForward createEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CalendarEntryBean bean = getRenderedObject("createdEntryBeanID");

        AcademicCalendarEntry entry = null;
        try {
            entry = CreateAcademicCalendarEntry.run(bean, true);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("parentEntryBean", bean);
            return mapping.findForward("prepareCreateCalendarEntry");
        }

        return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(
                entry.getRootEntry(), entry, bean.getBeginDateToDisplay(), bean.getEndDateToDisplay()));
    }

    public ActionForward prepareEditEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
        AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);

        Partial beginPartial = getBeginFromParameter(request);
        Partial endPartial = getEndFromParameter(request);

        request.setAttribute("entryBean",
                CalendarEntryBean.createCalendarEntryBeanToEditEntry(rootEntry, entry, beginPartial, endPartial));

        return mapping.findForward("prepareCreateCalendarEntry");
    }

    public ActionForward editEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        CalendarEntryBean bean = getRenderedObject("editedEntryBeanID");

        AcademicCalendarEntry entry = null;
        try {
            entry = CreateAcademicCalendarEntry.run(bean, false);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("entryBean", bean);
            return mapping.findForward("prepareCreateCalendarEntry");
        }

        return generateGanttDiagram(mapping, request, CalendarEntryBean.createCalendarEntryBeanToCreateEntry(
                entry.getRootEntry(), entry, bean.getBeginDateToDisplay(), bean.getEndDateToDisplay()));
    }

    public ActionForward deleteEntry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
        AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);

        AcademicCalendarRootEntry entryRootEntry = entry.getRootEntry();
        AcademicCalendarEntry entryParentEntry = entry.getParentEntry();

        boolean deletedRootEntry = entry.isRoot();

        Partial beginPartial = getBeginFromParameter(request);
        Partial endPartial = getEndFromParameter(request);

        try {
            DeleteAcademicCalendarEntry.run(entry, rootEntry);

        } catch (DomainException domainException) {
            addActionMessage(request, domainException.getMessage());
            return generateGanttDiagram(mapping, request,
                    CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entry, beginPartial, endPartial));
        }

        if (deletedRootEntry) {
            return prepareChooseCalendar(mapping, actionForm, request, response);

        } else if (entryParentEntry != null) {
            return generateGanttDiagram(mapping, request,
                    CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entryParentEntry, beginPartial, endPartial));

        } else {
            return generateGanttDiagram(mapping, request,
                    CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entryRootEntry, beginPartial, endPartial));
        }
    }

    // Private Methods

    private ActionForward generateGanttDiagram(ActionMapping mapping, HttpServletRequest request, CalendarEntryBean bean)
            throws InvalidArgumentException {

        YearMonthDay beginDate = bean.getBeginDateToDisplayInYearMonthDayFormat();
        YearMonthDay endDate = bean.getEndDateToDisplayInYearMonthDayFormat();

        endDate = endDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

        List<GanttDiagramEvent> newEntries = generateEntriesTree(request, bean.getRootEntry(), beginDate, endDate);
        GanttDiagram diagram = GanttDiagram.getNewTotalGanttDiagram(newEntries, beginDate, endDate);

        request.setAttribute("entryBean", bean);
        request.setAttribute("ganttDiagram", diagram);

        return mapping.findForward("viewAcademicCalendar");
    }

    private List<GanttDiagramEvent> generateEntriesTree(HttpServletRequest request, AcademicCalendarRootEntry academicCalendar,
            YearMonthDay begin, YearMonthDay end) {

        DateTime beginDateTime = begin.toDateTimeAtMidnight();
        DateTime endDateTime = end.toDateTimeAtMidnight();

        List<GanttDiagramEvent> result = new ArrayList<GanttDiagramEvent>();
        for (AcademicCalendarEntry entry : academicCalendar.getChildEntriesWithTemplateEntriesOrderByDate(beginDateTime,
                endDateTime)) {
            getSubEntriesTree(entry, result, beginDateTime, endDateTime);
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
        return AbstractDomainObject.fromExternalId(calendarIDString);
    }

    private AcademicCalendarRootEntry getAcademicCalendarRootEntryFromParameter(final HttpServletRequest request) {
        final String calendarIDString = request.getParameter("rootEntryID");
        return (AcademicCalendarRootEntry) AbstractDomainObject.fromExternalId(calendarIDString);
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
