package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		System.out.println("### ViewRoomFormAction - IN");
		HttpSession session = request.getSession();
		DynaActionForm indexForm = (DynaActionForm) form;
		request.removeAttribute(SessionConstants.INFO_SECTION);
		if (session != null) {
			IUserView userView = (IUserView) session.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();

			List infoRooms = getSelectedRooms(request);
			InfoRoom infoRoom =
				(InfoRoom) infoRooms.get(
					((Integer) indexForm.get("index")).intValue());
			request.setAttribute(SessionConstants.ROOM, infoRoom);
			request.setAttribute(
				SessionConstants.ROOM_OID,
				infoRoom.getIdInternal());
			System.out.println("#### infoRoom " + infoRoom);
			if (infoRoom != null)
				System.out.println(
					"#### infoRoomOID " + infoRoom.getIdInternal());

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.EXECUTION_PERIOD);

			Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

			try {
				List lessons;
				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);

				if (lessons != null) {
					request.setAttribute(
						SessionConstants.LESSON_LIST_ATT,
						lessons);
				}

			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}

			// Escolha de periodo execucao
			Object argsReadExecutionPeriods[] = {
			};
			ArrayList executionPeriods;
			try {
				executionPeriods =
					(ArrayList) gestor.executar(
						userView,
						"ReadExecutionPeriods",
						argsReadExecutionPeriods);
			} catch (FenixServiceException e1) {
				throw new FenixActionException();
			}

			//			// if executionPeriod was previously selected,form has that
			//			// value as default
			//			InfoExecutionPeriod selectedExecutionPeriod =
			//				(InfoExecutionPeriod) session.getAttribute(
			//					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			//			DynaActionForm indexFormForExecutionPeriod = new DynaActionForm();
			//			if (selectedExecutionPeriod != null) {
			//				indexFormForExecutionPeriod.set(
			//					"index",
			//					new Integer(
			//						executionPeriods.indexOf(selectedExecutionPeriod)));
			//			}
			//			request.setAttribute("pagedIndexForm", indexFormForExecutionPeriod);
			//----------------------------------------------

			ArrayList executionPeriodsLabelValueList = new ArrayList();
			for (int i = 0; i < executionPeriods.size(); i++) {
				infoExecutionPeriod =
					(InfoExecutionPeriod) executionPeriods.get(i);
				executionPeriodsLabelValueList.add(
					new LabelValueBean(
						infoExecutionPeriod.getName()
							+ " - "
							+ infoExecutionPeriod
								.getInfoExecutionYear()
								.getYear(),
						"" + i));
			}

			//			request.setAttribute(
			//				SessionConstants.LIST_INFOEXECUTIONPERIOD,
			//				executionPeriods);

			request.setAttribute(
				SessionConstants.LABELLIST_EXECUTIONPERIOD,
				executionPeriodsLabelValueList);
			//--------------------
			System.out.println("### ViewRoomFormAction - OUT");
			return mapping.findForward("Sucess");

		} else {
			throw new FenixActionException();
		}

	}





	private void setRoomContext(HttpServletRequest request)
		throws FenixActionException {
		Integer selectedRoomIndex =
			readIntegerRequestValue(request, "selectedRoomIndex");
		if (selectedRoomIndex != null)
			request.setAttribute(selectedRoomIndex.toString(),"selectedRoomIndex");

		request.setAttribute("name", readRequestValue(request, "name"));
		request.setAttribute("building", readRequestValue(request, "building"));
		request.setAttribute("floor", readRequestValue(request, "floor"));
		request.setAttribute("type", readRequestValue(request, "type"));
		request.setAttribute("capacityNormal",readRequestValue(request, "capacityNormal"));
		request.setAttribute("capacityExame",readRequestValue(request, "capacityExame"));

		InfoRoom selectedRoom = getSelectedRoom(selectedRoomIndex, request);
		request.setAttribute(SessionConstants.ROOM, selectedRoom);
		request.setAttribute(SessionConstants.ROOM_OID, selectedRoom.getIdInternal());

	}

	/**
	 * @returns the name of the selected sala.
	 **/
	private InfoRoom getSelectedRoom(
		Integer index,
		HttpServletRequest request)
		throws FenixActionException {

		List selectedRooms = getSelectedRooms(request);

		InfoRoom selectedRoom = null;
		if (selectedRooms != null && !selectedRooms.isEmpty()) {
			Collections.sort(selectedRooms);
			selectedRoom = (InfoRoom) selectedRooms.get(index.intValue());
		}
		return selectedRoom;
	}

	private List getSelectedRooms(HttpServletRequest request)
		throws FenixActionException {

		GestorServicos gestor = GestorServicos.manager();
		Object argsSelectRooms[] =
			{
				 new InfoRoom(
					readRequestValue(request, "name"),
					readRequestValue(request, "building"),
					readIntegerRequestValue(request, "floor"),
					readTypeRoomRequestValue(request, "type"),
					readIntegerRequestValue(request, "capacityNormal"),
					readIntegerRequestValue(request, "capacityExame"))};

		List selectedRooms = null;
		try {
			selectedRooms =
				(List) gestor.executar(null, "SelectRooms", argsSelectRooms);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (selectedRooms != null && !selectedRooms.isEmpty()) {
			Collections.sort(selectedRooms);
		}
		return selectedRooms;
	}

	private String readRequestValue(HttpServletRequest request, String name) {
		String obj = null;
		if (((String) request.getAttribute(name)) != null
			&& !((String) request.getAttribute(name)).equals(""))
			obj = (String) request.getAttribute(name);
		else if (
			request.getParameter(name) != null
				&& !request.getParameter(name).equals("")
				&& !request.getParameter(name).equals("null"))
			obj = (String) request.getParameter(name);
		return obj;
	}

	private Integer readIntegerRequestValue(
		HttpServletRequest request,
		String name) {
		String obj = readRequestValue(request, name);
		if (obj != null)
			return new Integer(obj);
		else
			return null;
	}

	private TipoSala readTypeRoomRequestValue(
		HttpServletRequest request,
		String name) {
		Integer obj = readIntegerRequestValue(request, name);
		if (obj != null)
			return new TipoSala(obj);
		else
			return null;
	}

}