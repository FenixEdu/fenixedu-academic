/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.academicAdministration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.dto.manager.academicCalendarManagement.CalendarEntryBean;
import org.fenixedu.academic.service.services.manager.academicCalendarManagement.CreateAcademicCalendarEntry;
import org.fenixedu.academic.service.services.manager.academicCalendarManagement.DeleteAcademicCalendarEntry;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminCalendarsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.renderer.GanttDiagram;
import org.fenixedu.academic.util.renderer.GanttDiagramEvent;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AcademicAdminCalendarsApp.class, path = "manage", titleKey = "title.academic.calendars.management", accessGroup = "#managers")
@Mapping(module = "academicAdministration", path = "/academicCalendarsManagement", input = "/index.do", formBean = "academicCalendarsManagementForm")
@Forwards(value = {
		@Forward(name = "viewAcademicCalendar", path = "/academicAdministration/academicCalendarsManagement/viewAcademicCalendar.jsp"),
		@Forward(name = "prepareCreateCalendarEntry", path = "/academicAdministration/academicCalendarsManagement/createCalendarEntry.jsp"),
		@Forward(name = "prepareChooseCalendar", path = "/academicAdministration/academicCalendarsManagement/chooseCalendar.jsp") })
public class AcademicCalendarsManagementDA extends FenixDispatchAction {
	public ActionForward prepareCreateAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(null);
		Partial begin;
		Partial end;
		if (currentExecutionYear != null) {
			begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
			end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());
		} else {
			begin = CalendarEntryBean.getPartialFromYearMonthDay(new YearMonthDay());
			end = CalendarEntryBean.getPartialFromYearMonthDay(new YearMonthDay().plusMonths(3));
		}

		CalendarEntryBean bean = CalendarEntryBean.createAcademicCalendarBean(begin, end);
		request.setAttribute("parentEntryBean", bean);

		return mapping.findForward("prepareCreateCalendarEntry");
	}

	@EntryPoint
	public ActionForward prepareChooseCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Collection<AcademicCalendarRootEntry> academicCalendars = rootDomainObject.getAcademicCalendarsSet();
		request.setAttribute("academicCalendars", academicCalendars);

		return mapping.findForward("prepareChooseCalendar");
	}

	public ActionForward prepareViewAcademicCalendar(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		AcademicCalendarEntry academicCalendar = getAcademicCalendarEntryFromParameter(request);
		ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(null);

		Partial begin;
		Partial end;
		if (currentExecutionYear != null) {
			begin = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
			end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());
		} else {
			begin = CalendarEntryBean.getPartialFromYearMonthDay(new YearMonthDay().minusMonths(12));
			end = CalendarEntryBean.getPartialFromYearMonthDay(new YearMonthDay().plusMonths(12));
		}

		CalendarEntryBean bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry(
				(AcademicCalendarRootEntry) academicCalendar, academicCalendar, begin, end);

		return generateGanttDiagram(mapping, request, bean);
	}

	public ActionForward viewAcademicCalendar(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CalendarEntryBean bean = getRenderedObject("datesToDisplayID");

		YearMonthDay beginDate = bean.getBeginDateToDisplayInYearMonthDayFormat();
		YearMonthDay endDate = bean.getEndDateToDisplayInYearMonthDayFormat();

		if (beginDate.isAfter(endDate)) {
			addActionMessage(request, "error.begin.after.end");
			ExecutionYear currentExecutionYear = ExecutionYear.findCurrent(null);
			Partial begin = CalendarEntryBean
					.getPartialFromYearMonthDay(currentExecutionYear.getBeginDateYearMonthDay());
			Partial end = CalendarEntryBean.getPartialFromYearMonthDay(currentExecutionYear.getEndDateYearMonthDay());
			bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry(bean.getRootEntry(), bean.getRootEntry(),
					begin, end);
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

	public ActionForward viewAcademicCalendarEntry(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		AcademicCalendarEntry entry = getAcademicCalendarEntryFromParameter(request);
		AcademicCalendarRootEntry rootEntry = getAcademicCalendarRootEntryFromParameter(request);

		Partial beginPartial = getBeginFromParameter(request);
		Partial endPartial = getEndFromParameter(request);

		CalendarEntryBean bean = CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry, entry, beginPartial,
				endPartial);

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

		request.setAttribute("parentEntryBean", CalendarEntryBean.createCalendarEntryBeanToCreateEntry(rootEntry,
				parentEntry, beginPartial, endPartial));

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
			return generateGanttDiagram(mapping, request, CalendarEntryBean
					.createCalendarEntryBeanToCreateEntry(rootEntry, entryParentEntry, beginPartial, endPartial));

		} else {
			return generateGanttDiagram(mapping, request, CalendarEntryBean
					.createCalendarEntryBeanToCreateEntry(rootEntry, entryRootEntry, beginPartial, endPartial));
		}
	}

	// Private Methods

	private ActionForward generateGanttDiagram(ActionMapping mapping, HttpServletRequest request,
			CalendarEntryBean bean) {

		YearMonthDay beginDate = bean.getBeginDateToDisplayInYearMonthDayFormat();
		YearMonthDay endDate = bean.getEndDateToDisplayInYearMonthDayFormat();

		endDate = endDate.plusMonths(1).withDayOfMonth(1).minusDays(1);

		List<GanttDiagramEvent> newEntries = generateEntriesTree(request, bean.getRootEntry(), beginDate, endDate);
		GanttDiagram diagram = GanttDiagram.getNewTotalGanttDiagram(newEntries, beginDate, endDate);

		request.setAttribute("entryBean", bean);
		request.setAttribute("ganttDiagram", diagram);

		return mapping.findForward("viewAcademicCalendar");
	}

	private List<GanttDiagramEvent> generateEntriesTree(HttpServletRequest request,
			AcademicCalendarRootEntry academicCalendar, YearMonthDay begin, YearMonthDay end) {

		DateTime beginDateTime = begin.toDateTimeAtMidnight();
		DateTime endDateTime = end.toDateTimeAtMidnight();

		List<GanttDiagramEvent> result = new ArrayList<GanttDiagramEvent>();
		for (AcademicCalendarEntry entry : academicCalendar.getChildEntriesWithTemplateEntriesOrderByDate(beginDateTime,
				endDateTime)) {
			getSubEntriesTree(entry, result, beginDateTime, endDateTime);
		}
		return result;
	}

	private void getSubEntriesTree(AcademicCalendarEntry entry, List<GanttDiagramEvent> result, DateTime begin,
			DateTime end) {
		result.add(entry);
		for (AcademicCalendarEntry subEntry : entry.getChildEntriesWithTemplateEntriesOrderByDate(begin, end)) {
			getSubEntriesTree(subEntry, result, begin, end);
		}
	}

	private AcademicCalendarEntry getAcademicCalendarEntryFromParameter(final HttpServletRequest request) {
		final String calendarIDString = request.getParameter("entryID");
		return FenixFramework.getDomainObject(calendarIDString);
	}

	private AcademicCalendarRootEntry getAcademicCalendarRootEntryFromParameter(final HttpServletRequest request) {
		final String calendarIDString = request.getParameter("rootEntryID");
		return (AcademicCalendarRootEntry) FenixFramework.getDomainObject(calendarIDString);
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
