package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadRoomByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams.ReadAvailableRoomsForExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.joda.time.YearMonthDay;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana & Ricardo
 */
@Mapping(module = "resourceAllocationManager", path = "/roomSearch", input = "/roomSearch.do?method=prepare&page=0",
        attribute = "roomSearchForm", formBean = "roomSearchForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showRooms", path = "df.page.showRooms"),
        @Forward(name = "roomSearch", path = "df.page.roomSearch") })
public class RoomSearchDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return mapping.findForward("roomSearch");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        DynaValidatorForm roomSearchForm = (DynaValidatorForm) form;

        // date
        Calendar searchDate = Calendar.getInstance();
        Integer day = new Integer((String) roomSearchForm.get("day"));
        Integer month = new Integer((String) roomSearchForm.get("month"));
        Integer year = new Integer((String) roomSearchForm.get("year"));
        request.setAttribute("day", day.toString());
        request.setAttribute("month", month.toString());
        request.setAttribute("year", year.toString());
        searchDate.set(Calendar.YEAR, year.intValue());
        searchDate.set(Calendar.MONTH, month.intValue() - 1);
        searchDate.set(Calendar.DAY_OF_MONTH, day.intValue());
        if (searchDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            ActionError actionError = new ActionError("error.sunday");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.sunday", actionError);
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        }

        // exam start time
        Calendar searchStartTime = Calendar.getInstance();
        Integer startHour = new Integer((String) roomSearchForm.get("beginningHour"));
        Integer startMinute = new Integer((String) roomSearchForm.get("beginningMinute"));
        request.setAttribute("beginningHour", startHour.toString());
        request.setAttribute("beginningMinute", startMinute.toString());
        searchStartTime.set(Calendar.HOUR_OF_DAY, startHour.intValue());
        searchStartTime.set(Calendar.MINUTE, startMinute.intValue());
        searchStartTime.set(Calendar.SECOND, 0);

        // exam end time
        Calendar searchEndTime = Calendar.getInstance();
        Integer endHour = new Integer((String) roomSearchForm.get("endHour"));
        Integer endMinute = new Integer((String) roomSearchForm.get("endMinute"));
        request.setAttribute("endHour", endHour.toString());
        request.setAttribute("endMinute", endMinute.toString());
        searchEndTime.set(Calendar.HOUR_OF_DAY, endHour.intValue());
        searchEndTime.set(Calendar.MINUTE, endMinute.intValue());
        searchEndTime.set(Calendar.SECOND, 0);

        if (searchStartTime.after(searchEndTime)) {
            ActionError actionError = new ActionError("error.timeSwitched");
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.timeSwitched", actionError);
            saveErrors(request, actionErrors);
            return prepare(mapping, form, request, response);
        }

        int dayOfWeekInt = searchDate.get(Calendar.DAY_OF_WEEK);
        DiaSemana dayOfWeek = new DiaSemana(dayOfWeekInt);

        List<InfoRoom> availableInfoRoom = null;
        availableInfoRoom =
                ReadAvailableRoomsForExam.run(YearMonthDay.fromCalendarFields(searchDate),
                        YearMonthDay.fromCalendarFields(searchDate), HourMinuteSecond.fromCalendarFields(searchStartTime),
                        HourMinuteSecond.fromCalendarFields(searchEndTime), dayOfWeek, null, null, Boolean.FALSE);
        String sdate = roomSearchForm.get("day") + "/" + roomSearchForm.get("month") + "/" + roomSearchForm.get("year");
        String startTime = roomSearchForm.get("beginningHour") + ":" + roomSearchForm.get("beginningMinute");
        String endTime = roomSearchForm.get("endHour") + ":" + roomSearchForm.get("endMinute");
        request.setAttribute(PresentationConstants.DATE, sdate);
        request.setAttribute(PresentationConstants.START_TIME, startTime);
        request.setAttribute(PresentationConstants.END_TIME, endTime);

        Integer exam = null;
        Integer normal = null;
        List<InfoRoom> newAvailableInfoRoom = new ArrayList<InfoRoom>();
        if (availableInfoRoom != null && !availableInfoRoom.isEmpty()) {
            try {
                exam = new Integer((String) roomSearchForm.get("exam"));
            } catch (NumberFormatException ex) {
                // the user didn't speciefy a exam minimum capacity
            }
            try {
                normal = new Integer((String) roomSearchForm.get("normal"));
            } catch (NumberFormatException ex) {
                // the user didn't speciefy a normal minimum capacity
            }
            if (normal != null || exam != null) {
                Iterator<InfoRoom> iter = availableInfoRoom.iterator();
                while (iter.hasNext()) {
                    InfoRoom elem = iter.next();
                    if (!((normal != null && elem.getCapacidadeNormal().intValue() < normal.intValue()) || (exam != null && elem
                            .getCapacidadeExame().intValue() < exam.intValue()))) {
                        newAvailableInfoRoom.add(elem);
                    }
                }
            } else {
                newAvailableInfoRoom = availableInfoRoom;
            }
        }
        if (newAvailableInfoRoom != null && !newAvailableInfoRoom.isEmpty()) {
            Collections.sort(newAvailableInfoRoom, new BeanComparator("nome"));
            String[] availableRoomId = new String[newAvailableInfoRoom.size()];
            Iterator<InfoRoom> iter = newAvailableInfoRoom.iterator();
            int i = 0;
            while (iter.hasNext()) {
                InfoRoom elem = iter.next();
                availableRoomId[i] = elem.getExternalId().toString();
            }
            request.setAttribute(PresentationConstants.AVAILABLE_ROOMS_ID, availableRoomId);

        }

        request.setAttribute(PresentationConstants.AVAILABLE_ROOMS, newAvailableInfoRoom);
        return mapping.findForward("showRooms");
    }

    public ActionForward sort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaValidatorForm roomSearchForm = (DynaValidatorForm) form;
        User userView = Authenticate.getUser();

        String[] availableRoomsId = (String[]) roomSearchForm.get("availableRoomsId");
        String sortParameter = request.getParameter("sortParameter");
        List<InfoRoom> availableRooms = new ArrayList<InfoRoom>();
        for (String element : availableRoomsId) {

            InfoRoom infoRoom = ReadRoomByOID.run(element);
            availableRooms.add(infoRoom);
        }
        if ((sortParameter != null) && (sortParameter.length() != 0)) {
            if (sortParameter.equals("name")) {
                Collections.sort(availableRooms, new BeanComparator("nome"));
            } else if (sortParameter.equals("type")) {
                Collections.sort(availableRooms, new BeanComparator("tipo"));
            } else if (sortParameter.equals("building")) {
                Collections.sort(availableRooms, new BeanComparator("edificio"));
            } else if (sortParameter.equals("floor")) {
                Collections.sort(availableRooms, new BeanComparator("piso"));
            } else if (sortParameter.equals("normal")) {
                Collections.sort(availableRooms, new ReverseComparator(new BeanComparator("capacidadeNormal")));
            } else if (sortParameter.equals("exam")) {
                Collections.sort(availableRooms, new ReverseComparator(new BeanComparator("capacidadeExame")));
            }
        } else {
            Collections.sort(availableRooms, new BeanComparator("nome"));
        }

        String sdate = roomSearchForm.get("day") + "/" + roomSearchForm.get("month") + "/" + roomSearchForm.get("year");
        String startTime = roomSearchForm.get("beginningHour") + ":" + roomSearchForm.get("beginningMinute");
        String endTime = roomSearchForm.get("endHour") + ":" + roomSearchForm.get("endMinute");
        request.setAttribute(PresentationConstants.DATE, sdate);
        request.setAttribute(PresentationConstants.START_TIME, startTime);
        request.setAttribute(PresentationConstants.END_TIME, endTime);
        request.setAttribute(PresentationConstants.AVAILABLE_ROOMS, availableRooms);
        request.setAttribute(PresentationConstants.AVAILABLE_ROOMS_ID, availableRoomsId);

        return mapping.findForward("showRooms");
    }

}