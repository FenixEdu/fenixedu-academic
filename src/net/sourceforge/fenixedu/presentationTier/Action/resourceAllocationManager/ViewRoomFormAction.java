package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.RoomOccupationWeekBean;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixSelectedRoomsAndSelectedRoomIndexContextAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixSelectedRoomsAndSelectedRoomIndexContextAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixServiceException, FenixFilterException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		RoomOccupationWeekBean roomOccupationWeekBean = getRoomOccupationWeekBean();
		if (roomOccupationWeekBean == null) {
			roomOccupationWeekBean = new RoomOccupationWeekBean();
		}
		request.setAttribute("roomOccupationWeekBean", roomOccupationWeekBean);

		DynaActionForm indexForm = (DynaActionForm) form;
		List<InfoRoom> infoRooms = (List<InfoRoom>) request.getAttribute(PresentationConstants.SELECTED_ROOMS);
		InfoRoom infoRoom = infoRooms.get(((Integer) indexForm.get("index")).intValue());
		request.setAttribute(PresentationConstants.ROOM, infoRoom);
		request.setAttribute(PresentationConstants.ROOM_OID, infoRoom.getIdInternal());

		final AllocatableSpace room = (AllocatableSpace) rootDomainObject.readResourceByOID(infoRoom.getIdInternal());
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

		return mapping.findForward("Sucess");
	}

	private RoomOccupationWeekBean getRoomOccupationWeekBean() {
		if (RenderUtils.getViewState() == null) {
			return null;
		}

		RoomOccupationWeekBean roomOccupationWeekBean =
				(RoomOccupationWeekBean) RenderUtils.getViewState().getMetaObject().getObject();
		return roomOccupationWeekBean;
	}

}