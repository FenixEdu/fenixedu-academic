package ServidorApresentacao.Action.publico;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
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

		//		if (sessao != null) {

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
				(List) ServiceUtils.executeService(
					null,
					"SelectRooms",
					argsSelectRooms);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		InfoExecutionPeriod infoExecutionPeriod = null;
		try {
			infoExecutionPeriod = setExecutionContext(request);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		Object args[] = {
//		};
//		try {
//			infoExecutionPeriod =
//				(InfoExecutionPeriod) ServiceUtils.executeService(
//					null,
//					"ReadCurrentExecutionPeriod",
//					args);
//		} catch (FenixServiceException e1) {
//			throw new FenixActionException(e1);
//		}

		request.setAttribute("objectCode", infoExecutionPeriod.getIdInternal());

		ActionForward forward = mapping.getInputForward();
		if (infoRooms == null || infoRooms.isEmpty()) {
			ActionErrors errors = new ActionErrors();
			errors.add(
				"not.found.rooms",
				new ActionError("message.public.notfound.rooms"));
			saveErrors(request, errors);
		} else if (infoRooms.size() == 1) {
			InfoRoom infoRoom = (InfoRoom) infoRooms.get(0);

//			request.setAttribute("publico.infoRoom", infoRoom);
			
			request.setAttribute("objectCode",infoExecutionPeriod.getIdInternal().toString());
			request.setAttribute("roomName", infoRoom.getNome());

			forward = mapping.findForward("one");
		} else {
			Collections.sort(infoRooms);
			request.setAttribute("publico.infoRooms", infoRooms);
			request.setAttribute("name", readFormValue(roomForm, "name"));
			request.setAttribute(
				"building",
				readFormValue(roomForm, "building"));
			request.setAttribute("floor", readFormValue(roomForm, "floor"));
			request.setAttribute("type", readFormValue(roomForm, "type"));
			request.setAttribute(
				"capacityNormal",
				readFormValue(roomForm, "capacityNormal"));
			request.setAttribute(
				"capacityExame",
				readFormValue(roomForm, "capacityExame"));

			forward = mapping.findForward("list");
		}
		return forward;
		//		} else
		//			throw new FenixActionException();
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

	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {

		HttpSession session = request.getSession(false);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = SessionUtils.getUserView(request);
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					userView,
					"ReadCurrentExecutionPeriod",
					new Object[0]);

			session.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}

}