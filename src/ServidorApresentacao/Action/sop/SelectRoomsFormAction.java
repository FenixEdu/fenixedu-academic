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
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
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
				sessao.removeAttribute("publico.infoRooms");
				sessao.setAttribute("publico.infoRooms", infoRooms);
				sessao.removeAttribute("name");
				sessao.setAttribute("name", readFormValue(roomForm, "name"));
				sessao.removeAttribute("building");
				sessao.setAttribute(
					"building",
					readFormValue(roomForm, "building"));
				sessao.removeAttribute("floor");
				sessao.setAttribute("floor", readFormValue(roomForm, "floor"));
				sessao.removeAttribute("type");
				sessao.setAttribute("type", readFormValue(roomForm, "type"));
				sessao.removeAttribute("capacityNormal");
				sessao.setAttribute(
					"capacityNormal",
					readFormValue(roomForm, "capacityNormal"));
				sessao.removeAttribute("capacityExame");
				sessao.setAttribute(
					"capacityExame",
					readFormValue(roomForm, "capacityExame"));
			} else {
				sessao.removeAttribute("publico.infoRooms");
				sessao.removeAttribute("name");
				sessao.removeAttribute("building");
				sessao.removeAttribute("floor");
				sessao.removeAttribute("type");
				sessao.removeAttribute("capacityNormal");
				sessao.removeAttribute("capacityExame");
			}

			InfoExecutionPeriod executionPeriod;
			Object args[] = {
			};
			try {
				executionPeriod =
					(InfoExecutionPeriod) gestor.executar(
						null,
						"ReadActualExecutionPeriod",
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