/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExam;
import DataBeans.InfoRoom;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRoomsDA extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
		GestorServicos gestor = GestorServicos.manager();
		DynaActionForm editExamRoomsForm = (DynaActionForm) form;

		InfoExam infoExam =
			((InfoViewExamByDayAndShift) session
				.getAttribute(SessionConstants.INFO_EXAMS_KEY))
				.getInfoExam();

		List examRooms = infoExam.getAssociatedRooms();

		if (examRooms != null) {
			String[] rooms = new String[examRooms.size()];
			for (int i = 0; i < examRooms.size(); i++) {
				rooms[i] = ((InfoRoom) examRooms.get(i)).getIdInternal().toString();
			}
			editExamRoomsForm.set("selectedRooms", rooms);
		} else {
			editExamRoomsForm.set("selectedRooms", null);
		}

		Object[] args = { infoExam };
		List availableRooms =
			(List) gestor.executar(userView, "ReadEmptyRoomsForExam", args);

		Hashtable roomsHashTable = new Hashtable();
		for (int i = 0; i < availableRooms.size(); i++) {
			InfoRoom infoRoom = (InfoRoom) availableRooms.get(i);
			List roomsOfBuilding =
				(List) roomsHashTable.get(infoRoom.getEdificio());
			if (roomsOfBuilding == null) {
				roomsOfBuilding = new ArrayList();
				roomsHashTable.put(infoRoom.getEdificio(), roomsOfBuilding);
			}
			roomsOfBuilding.add(infoRoom);
		}

		List sortedRooms = new ArrayList();
		Enumeration keyBuildings = roomsHashTable.keys();
		while (keyBuildings.hasMoreElements()) {
			sortedRooms.add(roomsHashTable.get(keyBuildings.nextElement()));
		}

		request.setAttribute(SessionConstants.AVAILABLE_ROOMS, sortedRooms);

		return mapping.findForward("ViewSelectRoomsForm");
	}

	public ActionForward select(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
		GestorServicos gestor = GestorServicos.manager();
		DynaActionForm editExamRoomsForm = (DynaActionForm) form;

		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			(InfoViewExamByDayAndShift) session.getAttribute(
				SessionConstants.INFO_EXAMS_KEY);
		InfoExam infoExam = infoViewExamByDayAndShift.getInfoExam();

		String[] rooms = (String[]) editExamRoomsForm.get("selectedRooms");
		List roomsToSet = new ArrayList();
		for (int i = 0; i < rooms.length; i++) {
			roomsToSet.add(new Integer(rooms[i]));
		}

		Object[] args = { infoExam, roomsToSet };
		infoExam = (InfoExam) gestor.executar(userView, "EditExamRooms", args);

		infoViewExamByDayAndShift.setInfoExam(infoExam);
		session.removeAttribute(SessionConstants.INFO_EXAMS_KEY);
		session.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

		return mapping.findForward("ViewEditExamForm");
	}

}