package ServidorApresentacao.Action.sop;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class SelectRoomsFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm roomForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(true);
		if (sessao != null) {
			GestorServicos gestor = GestorServicos.manager();

			Object argsSelectRooms[] =
				{
					 new InfoRoom(
						readFormValue(roomForm, "name"),
						readFormValue(roomForm, "building"),
						readIntegerFormValue(roomForm, "floor"),
						readTypeRoomFormValue(roomForm, "type"),
						readIntegerFormValue(roomForm, "capacityNormal"),
						readIntegerFormValue(roomForm, "capacityExame"))};
			List infoRooms;
			try {
				infoRooms =
					(List) gestor.executar(
						null,
						"SelectRooms",
						argsSelectRooms);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			if (infoRooms != null && !infoRooms.isEmpty()) {
				Collections.sort(infoRooms);
				request.removeAttribute("publico.infoRooms");
				request.setAttribute("publico.infoRooms", infoRooms);
				request.removeAttribute("selectRoomsName");
				request.setAttribute("selectRoomsName", readFormValue(roomForm, "name"));
				request.removeAttribute("selectRoomsBuilding");
				request.setAttribute(
					"selectRoomsBuilding",
					readFormValue(roomForm, "building"));
				request.removeAttribute("selectRoomsFloor");
				request.setAttribute("selectRoomsFloor", readFormValue(roomForm, "floor"));
				request.removeAttribute("selectRoomsType");
				request.setAttribute("selectRoomsType", readFormValue(roomForm, "type"));
				request.removeAttribute("selectRoomsCapacityNormal");
				request.setAttribute("selectRoomsCapacityNormal",readFormValue(roomForm, "capacityNormal"));
				request.removeAttribute("selectRoomsCapacityExame");
				request.setAttribute("selectRoomsCapacityExame",readFormValue(roomForm, "capacityExame"));
			} else {
				request.removeAttribute("publico.infoRooms");
				request.removeAttribute("selectRoomsName");
				request.removeAttribute("selectRoomsBuilding");
				request.removeAttribute("selectRoomsFloor");
				request.removeAttribute("selectRoomsType");
				request.removeAttribute("selectRoomsCapacityNormal");
				request.removeAttribute("selectRoomsCapacityExame");
			}
			System.out.println("### SelectRoomsFormAction");
			System.out.println("## SelectRooms-name:"+readRequestValue(request, "selectRoomsName"));
			System.out.println("## SelectRooms-building-"+readRequestValue(request, "selectRoomsBuilding"));
			System.out.println("## SelectRooms-floor-"+readIntegerRequestValue(request, "selectRoomsFloor"));
			System.out.println("## SelectRooms-type-"+readTypeRoomRequestValue(request, "selectRoomsType"));
			System.out.println("## SelectRooms-capacityExam-"+readIntegerRequestValue(request, "selectRoomsCapacityNormal"));
			System.out.println("## SelectRooms-capacityNormal-"+readIntegerRequestValue(request, "selectRoomsCapacityExame") );

//			InfoExecutionPeriod executionPeriod;
//			Object args[] = {
//			};
//			try {
//				executionPeriod =
//					(InfoExecutionPeriod) gestor.executar(
//						null,
//						"ReadCurrentExecutionPeriod",
//						args);
//			} catch (FenixServiceException e1) {
//				throw new FenixActionException(e1);
//			}
//
//			RequestUtils.setExecutionPeriodToRequest(request, executionPeriod);
			return mapping.findForward("Sucess");
		} else
			throw new FenixActionException();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}

	private String readFormValue(DynaActionForm roomForm, String name) {
		String obj = null;
		if (roomForm.get(name) != null
			&& !((String) roomForm.get(name)).equals(""))
			obj = (String) roomForm.get(name);
		return obj;
	}

	private Integer readIntegerFormValue(
		DynaActionForm roomForm,
		String name) {
		String obj = readFormValue(roomForm, name);
		if (obj != null)
			return new Integer(obj);
		else
			return null;
	}

	private TipoSala readTypeRoomFormValue(
		DynaActionForm roomForm,
		String name) {
		Integer obj = readIntegerFormValue(roomForm, name);
		if (obj != null)
			return new TipoSala(obj);
		else
			return null;
	}

	private String readRequestValue(HttpServletRequest request, String name) {
		String obj = null;
		if (request.getAttribute(name) != null
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