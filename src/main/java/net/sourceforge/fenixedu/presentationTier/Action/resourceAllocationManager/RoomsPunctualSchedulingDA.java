package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.DeleteGenericEvent;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.CreateRoomsPunctualScheduling;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.EditRoomsPunctualScheduling;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingBean.PeriodType;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomsPunctualSchedulingHistoryBean;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.renderer.GanttDiagram;
import net.sourceforge.fenixedu.util.renderer.GanttDiagramEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "resourceAllocationManager", path = "/roomsPunctualScheduling", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "seeHistory", path = "see-rooms-punctual-scheduling-history"),
        @Forward(name = "prepareFinalizeCreation", path = "prepare-finalize-creation-of-rooms-punctual-scheduling"),
        @Forward(name = "prepareViewRoomsPunctualScheduling", path = "prepare-view-rooms-punctual-scheduling"),
        @Forward(name = "prepareCreateNewRoomPunctualScheduling", path = "prepare-create-room-punctual-scheduling"),
        @Forward(name = "prepareRoomsPunctualScheduling", path = "prepare-rooms-punctual-scheduling") })
public class RoomsPunctualSchedulingDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws InvalidArgumentException {
        Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();
        if (!genericEvents.isEmpty()) {

            RoomsPunctualSchedulingBean bean = null;
            IViewState viewState = RenderUtils.getViewState();
            if (viewState == null) {
                bean = new RoomsPunctualSchedulingBean();
            } else {
                bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
            }

            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram =
                    GanttDiagram.getNewWeeklyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);
            request.setAttribute("ganttDiagram", diagram);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
        }
        return mapping.findForward("prepareRoomsPunctualScheduling");
    }

    public ActionForward prepareViewDailyView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {
        Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();
        if (!genericEvents.isEmpty()) {
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram =
                    GanttDiagram.getNewDailyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);
            RoomsPunctualSchedulingBean bean = new RoomsPunctualSchedulingBean();
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            request.setAttribute("ganttDiagram", diagram);
        }
        return mapping.findForward("prepareRoomsPunctualScheduling");
    }

    public ActionForward prepareViewMonthlyView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {
        Set<GenericEvent> genericEvents = GenericEvent.getActiveGenericEventsForRoomOccupations();
        if (!genericEvents.isEmpty()) {
            YearMonthDay firstDay = getFirstDay(request);
            GanttDiagram diagram =
                    GanttDiagram.getNewMonthlyGanttDiagram(new ArrayList<GanttDiagramEvent>(genericEvents), firstDay);
            RoomsPunctualSchedulingBean bean = new RoomsPunctualSchedulingBean();
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            request.setAttribute("ganttDiagram", diagram);
        }
        return mapping.findForward("prepareRoomsPunctualScheduling");
    }

    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException {
        GenericEvent genericEvent = getGenericEventFromParameter(request);
        request.setAttribute("genericEvent", genericEvent);
        request.setAttribute("roomsPunctualSchedulingBean", new RoomsPunctualSchedulingBean(genericEvent));
        return mapping.findForward("prepareViewRoomsPunctualScheduling");
    }

    public ActionForward deleteRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws InvalidArgumentException,  FenixServiceException {

        GenericEvent genericEventFromParameter = getGenericEventFromParameter(request);
        try {
            DeleteGenericEvent.run(genericEventFromParameter);
        } catch (DomainException e) {
            saveMessages(request, e);
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException, InvalidArgumentException {

        RoomsPunctualSchedulingBean bean = null;
        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithPeriodType");
        if (viewState == null) {
            viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithInfo");
        }

        if (viewState == null) {
            bean = new RoomsPunctualSchedulingBean();
        } else {
            bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
        }

        if (bean.getPeriodType() != null && bean.getFrequency() != null && bean.getPeriodType().equals(PeriodType.WITH_FREQUENCY)) {
            RenderUtils.invalidateViewState("roomsPunctualSchedulingWithInfo");
        }

        request.setAttribute("roomsPunctualSchedulingBean", bean);
        return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
    }

    public ActionForward prepareFinalizeCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithInfo");
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
        request.setAttribute("roomsPunctualSchedulingBean", bean);

        final YearMonthDay begin = bean.getBegin();
        final YearMonthDay end = bean.getEnd();
        final Partial beginTime = bean.getBeginTime();
        final Partial endTime = bean.getEndTime();

        if (begin == null || end == null) {
            saveMessages(request, "error.occupationPeriod.invalid.dates");
            return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
        }

        if (beginTime == null || endTime == null) {
            saveMessages(request, "error.beginTime.after.or.equal.then.endTime");
            return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
        }

        final DateTime b =
                new DateTime(begin.getYear(), begin.getMonthOfYear(), begin.getDayOfMonth(), beginTime.get(DateTimeFieldType
                        .hourOfDay()), beginTime.get(DateTimeFieldType.minuteOfHour()), 0, 0);
        final DateTime e =
                new DateTime(end.getYear(), end.getMonthOfYear(), end.getDayOfMonth(),
                        endTime.get(DateTimeFieldType.hourOfDay()), endTime.get(DateTimeFieldType.minuteOfHour()), 0, 0);

        if (!b.isBefore(e)) {
            saveMessages(request, "error.beginTime.after.or.equal.then.endTime");
            return mapping.findForward("prepareCreateNewRoomPunctualScheduling");
        }

        return mapping.findForward("prepareFinalizeCreation");
    }

    public ActionForward associateNewRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithNewRoom");
        if (viewState == null) {
            viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        }

        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
        bean.addNewRoom(bean.getSelectedRoom());
        bean.setSelectedRoom(null);

        request.setAttribute("roomsPunctualSchedulingBean", bean);
        RenderUtils.invalidateViewState("roomsPunctualSchedulingWithNewRoom");
        return mapping.findForward("prepareFinalizeCreation");
    }

    public ActionForward removeRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingBeanHidden");
        if (viewState == null) {
            viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        }

        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();
        String[] selectedRooms = request.getParameterValues("selectedRoom");
        if (selectedRooms != null && selectedRooms.length > 0) {
            for (String selectedRoom : selectedRooms) {
                Integer roomIdInternal = Integer.valueOf(selectedRoom);
                AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(roomIdInternal);
                bean.removeRoom(room);
            }
        }

        request.setAttribute("roomsPunctualSchedulingBean", bean);
        return mapping.findForward("prepareFinalizeCreation");
    }

    public ActionForward associateNewRoomToEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        associateNewRoom(mapping, form, request, response);
        return mapping.findForward("prepareViewRoomsPunctualScheduling");
    }

    public ActionForward removeRoomToEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        removeRoom(mapping, form, request, response);
        return mapping.findForward("prepareViewRoomsPunctualScheduling");
    }

    public ActionForward createRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException,  InvalidArgumentException {

        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();

        try {
            CreateRoomsPunctualScheduling.run(bean);

        } catch (DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            return mapping.findForward("prepareFinalizeCreation");
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward editRoomsPunctualScheduling(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException,  InvalidArgumentException {

        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingWithDescriptions");
        RoomsPunctualSchedulingBean bean = (RoomsPunctualSchedulingBean) viewState.getMetaObject().getObject();

        try {
            EditRoomsPunctualScheduling.run(bean);

        } catch (DomainException domainException) {
            saveMessages(request, domainException);
            request.setAttribute("roomsPunctualSchedulingBean", bean);
            return mapping.findForward("prepareFinalizeCreation");
        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward seeRoomsPunctualSchedulingHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException,  InvalidArgumentException {

        RoomsPunctualSchedulingHistoryBean bean = null;
        IViewState viewState = RenderUtils.getViewState("roomsPunctualSchedulingHistoryWithYearAndMonth");
        if (viewState != null) {
            bean = (RoomsPunctualSchedulingHistoryBean) viewState.getMetaObject().getObject();
        } else {
            bean = new RoomsPunctualSchedulingHistoryBean();
        }

        if (bean.getYear() != null && bean.getMonth() != null) {
            DateTime firstDayOfMonth =
                    new DateTime(bean.getYear().get(DateTimeFieldType.year()), bean.getMonth().get(
                            DateTimeFieldType.monthOfYear()), 1, 0, 0, 0, 0);
            DateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1);
            Set<GenericEvent> events =
                    GenericEvent.getAllGenericEvents(firstDayOfMonth, lastDayOfMonth, bean.getAllocatableSpace());
            request.setAttribute("events", events);
        }

        request.setAttribute("roomsPunctualSchedulingHistoryBean", bean);
        return mapping.findForward("seeHistory");
    }

    // Private Methods

    private YearMonthDay getFirstDay(HttpServletRequest request) {
        YearMonthDay firstDay = getFirstDayFromParameter(request);
        if (firstDay == null) {
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

    protected GenericEvent getGenericEventFromParameter(final HttpServletRequest request) {
        final String genericEventIDString = request.getParameter("genericEventID");
        final Integer genericEventID = Integer.valueOf(genericEventIDString);
        return rootDomainObject.readGenericEventByOID(genericEventID);
    }
}