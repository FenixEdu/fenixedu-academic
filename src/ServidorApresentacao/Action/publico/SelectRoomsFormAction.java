package ServidorApresentacao.Action.publico;

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
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
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
		throws Exception {
		
					

		DynaActionForm roomForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession();
		sessao.removeAttribute(SessionConstants.INFO_SECTION);
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

			List infoRooms =
				(List) gestor.executar(null, "SelectRooms", argsSelectRooms);
			
			sessao.removeAttribute("publico.infoRooms");
			if (infoRooms != null && !infoRooms.isEmpty()) {
				Collections.sort(infoRooms);
				sessao.setAttribute("publico.infoRooms", infoRooms);
			}
			return mapping.findForward("Sucess");
		} else
			throw new Exception();
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