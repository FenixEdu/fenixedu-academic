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

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
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
				request.removeAttribute("name");
				request.setAttribute("name", readFormValue(roomForm, "name"));
				request.removeAttribute("building");
				request.setAttribute(
					"building",
					readFormValue(roomForm, "building"));
				request.removeAttribute("floor");
				request.setAttribute("floor", readFormValue(roomForm, "floor"));
				request.removeAttribute("type");
				request.setAttribute("type", readFormValue(roomForm, "type"));
				request.removeAttribute("capacityNormal");
				request.setAttribute(
					"capacityNormal",
					readFormValue(roomForm, "capacityNormal"));
				request.removeAttribute("capacityExame");
				request.setAttribute(
					"capacityExame",
					readFormValue(roomForm, "capacityExame"));
			} else {
				request.removeAttribute("publico.infoRooms");
				request.removeAttribute("name");
				request.removeAttribute("building");
				request.removeAttribute("floor");
				request.removeAttribute("type");
				request.removeAttribute("capacityNormal");
				request.removeAttribute("capacityExame");
			}

			InfoExecutionPeriod executionPeriod;
			Object args[] = {
			};
			try {
				executionPeriod =
					(InfoExecutionPeriod) gestor.executar(
						null,
						"ReadCurrentExecutionPeriod",
						args);
			} catch (FenixServiceException e1) {
				throw new FenixActionException(e1);
			}

			RequestUtils.setExecutionPeriodToRequest(request, executionPeriod);
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

}