package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomOccupationWeekBean;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author tfc130
 */
public class ViewRoomFormActionPostBack extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {
        RoomOccupationWeekBean roomOccupationWeekBean = new RoomOccupationWeekBean();

        DynaActionForm indexForm = (DynaActionForm) form;
        List<InfoRoom> infoRooms = (List<InfoRoom>) request.getAttribute(PresentationConstants.SELECTED_ROOMS);
        InfoRoom infoRoom = infoRooms.get(((Integer) indexForm.get("index")).intValue());
        roomOccupationWeekBean.setRoom(infoRoom);
        fillRequestData(roomOccupationWeekBean, request);
        return mapping.findForward("Sucess");
    }

    public ActionForward academicIntervalPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        RoomOccupationWeekBean roomOccupationWeekBean = getRenderedObject("roomOccupationWeekBean");
        RenderUtils.invalidateViewState();
        roomOccupationWeekBean.setWeekBean(null);
        fillRequestData(roomOccupationWeekBean, request);
        return mapping.findForward("Sucess");
    }

    public ActionForward weekPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        RoomOccupationWeekBean roomOccupationWeekBean = getRenderedObject("roomOccupationWeekBean");
        RenderUtils.invalidateViewState();
        fillRequestData(roomOccupationWeekBean, request);
        return mapping.findForward("Sucess");
    }

    public void fillRequestData(RoomOccupationWeekBean roomOccupationWeekBean, HttpServletRequest request)
            throws FenixServiceException {
        request.setAttribute(PresentationConstants.ROOM, roomOccupationWeekBean.getRoom());
        request.setAttribute(PresentationConstants.ROOM_OID, roomOccupationWeekBean.getRoom().getExternalId());
        final AllocatableSpace room =
                (AllocatableSpace) FenixFramework.getDomainObject(roomOccupationWeekBean.getRoom().getExternalId());
        Calendar calendar = Calendar.getInstance();
        if (roomOccupationWeekBean.getWeekBean() == null) {
            calendar.setTime(roomOccupationWeekBean.getAcademicInterval().getStart().toDate());
        } else {
            calendar.setTime(roomOccupationWeekBean.getWeekBean().getWeek().toDateMidnight().toDate());
        }
        List<InfoObject> showOccupations =
                ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom.run(room, YearMonthDay.fromCalendarFields(calendar));
        if (showOccupations != null) {
            request.setAttribute(PresentationConstants.LESSON_LIST_ATT, showOccupations);
        }
        request.setAttribute("roomOccupationWeekBean", roomOccupationWeekBean);
    }
}