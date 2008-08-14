package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.Util;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.joda.time.YearMonthDay;

/**
 * @author jpvl
 */
public class SearchEmptyRoomsDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	request.setAttribute("minutes", Util.getMinutes());
	request.setAttribute("hours", Util.getHours());
	request.setAttribute("weekDays", Util.getDaysOfWeek());

	return mapping.findForward("searchPage");
    }

    public ActionForward doSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	try {
	    DynaValidatorForm searchForm = (DynaValidatorForm) form;
	    Integer normalCapacity = null;
	    try {
		normalCapacity = new Integer((String) searchForm.get("normalCapacity"));

	    } catch (NumberFormatException e) {
		// ignored
	    }

	    Integer weekDayInteger = new Integer((String) searchForm.get("weekDay"));
	    DiaSemana weekDay = new DiaSemana(weekDayInteger);

	    int startHour = Integer.parseInt((String) searchForm.get("startHour"));
	    int startMinute = Integer.parseInt((String) searchForm.get("startMinutes"));
	    HourMinuteSecond startTime = new HourMinuteSecond(startHour, startMinute, 0);

	    int endHour = Integer.parseInt((String) searchForm.get("endHour"));
	    int endMinute = Integer.parseInt((String) searchForm.get("endMinutes"));
	    HourMinuteSecond endTime = new HourMinuteSecond(endHour, endMinute, 0);

	    if (startTime.isAfter(endTime)) {
		ActionError actionError = new ActionError("error.timeSwitched");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add("error.timeSwitched", actionError);
		saveErrors(request, actionErrors);
		return prepare(mapping, form, request, response);
	    }

	    int startDay = Integer.parseInt((String) searchForm.get("startDay"));
	    int startMonth = Integer.parseInt((String) searchForm.get("startMonth"));
	    int startYear = Integer.parseInt((String) searchForm.get("startYear"));
	    YearMonthDay startDate = new YearMonthDay(startYear, startMonth, startDay);

	    int endDay = Integer.parseInt((String) searchForm.get("endDay"));
	    int endMonth = Integer.parseInt((String) searchForm.get("endMonth"));
	    int endYear = Integer.parseInt((String) searchForm.get("endYear"));
	    YearMonthDay endDate = new YearMonthDay(endYear, endMonth, endDay);

	    if (startDate.isAfter(endDate)) {
		ActionError actionError = new ActionError("error.dateSwitched");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add("error.dateSwitched", actionError);
		saveErrors(request, actionErrors);
		return prepare(mapping, form, request, response);
	    }

	    startDate = getBeginDateInSpecificWeekDay(weekDay, startDate);
	    if (startDate.isAfter(endDate)) {
		ActionError actionError = new ActionError("error.invalid.weekDay");
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add("error.invalid.weekDay", actionError);
		saveErrors(request, actionErrors);
		return prepare(mapping, form, request, response);
	    }

	    Object args[] = { startDate, endDate, startTime, endTime, weekDay, normalCapacity, FrequencyType.WEEKLY, Boolean.TRUE };
	    List<InfoRoom> emptyRoomsList = (List<InfoRoom>) ServiceUtils.executeService("ReadAvailableRoomsForExam", args);
	    Collections.sort(emptyRoomsList);

	    if (emptyRoomsList == null || emptyRoomsList.isEmpty()) {
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add("search.empty.rooms.no.rooms", new ActionError("search.empty.rooms.no.rooms"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    }

	    request.setAttribute("weekDay", weekDay);
	    request.setAttribute("intervalStart", startTime.toString("HH:mm"));
	    request.setAttribute("intervalEnd", endTime.toString("HH:mm"));
	    request.setAttribute("minimumCapacity", normalCapacity);
	    request.setAttribute("startDate", startDate.toString("dd/MM/yyyy"));
	    request.setAttribute("endDate", endDate.toString("dd/MM/yyyy"));
	    request.setAttribute("roomList", emptyRoomsList);

	    return mapping.findForward("showSearchResult");

	} catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
	}
    }

    private YearMonthDay getBeginDateInSpecificWeekDay(DiaSemana diaSemana, YearMonthDay begin) {
	if (diaSemana != null) {
	    YearMonthDay newBegin = begin.toDateTimeAtMidnight().withDayOfWeek(diaSemana.getDiaSemanaInDayOfWeekJodaFormat())
		    .toYearMonthDay();
	    if (newBegin.isBefore(begin)) {
		begin = newBegin.plusDays(Lesson.NUMBER_OF_DAYS_IN_WEEK);
	    } else {
		begin = newBegin;
	    }
	}
	return begin;
    }
}