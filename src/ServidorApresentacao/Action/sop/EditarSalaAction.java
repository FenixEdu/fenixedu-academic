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

import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoSala;

/**
 * @author Nuno Antão
 * @author João Pereira
 **/

public class EditarSalaAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		ArrayList listaSalasBean = (ArrayList) getSelectedRooms(request);
		Integer index = readIntegerRequestValue(request,"selectedRoomIndex");
		InfoRoom roomToEdit = null;
		if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
			Collections.sort(listaSalasBean);
			roomToEdit = (InfoRoom) listaSalasBean.get(index.intValue());
		}
	
		//			(ArrayList) session.getAttribute("publico.infoRooms");
		//		DynaActionForm posicaoSalaFormBean =
		//			(DynaActionForm) session.getAttribute("indexForm");

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		DynaActionForm salaBean = (DynaActionForm) form;
System.out.println("## Form-name-"+salaBean.get("name"));
System.out.println("## Form-building-"+salaBean.get("building"));
System.out.println("## Form-floor-"+salaBean.get("floor"));
System.out.println("## Form-type-"+salaBean.get("type"));
System.out.println("## Form-capacityExam-"+salaBean.get("capacityExame"));
System.out.println("## Form-capacityNormal-"+salaBean.get("capacityNormal") );
		InfoRoom editedRoom =
			new InfoRoom(
				(String) salaBean.get("name"),
				(String) salaBean.get("building"),
				new Integer((String) salaBean.get("floor")),
				new TipoSala(new Integer((String) salaBean.get("type"))),
				new Integer((String) salaBean.get("capacityNormal")),
				new Integer((String) salaBean.get("capacityExame")));

		Object argsCriarSala[] =
			{ new RoomKey(roomToEdit.getNome()), editedRoom };

		try {
			GestorServicos.manager().executar(
				userView,
				"EditarSala",
				argsCriarSala);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("A Sala", e);
		}

		request.removeAttribute("name");

		/* Actualiza a lista de salas no "scope" de sessao */
		listaSalasBean.set(index.intValue(), editedRoom);
		if (listaSalasBean.isEmpty())
			session.removeAttribute("publico.infoRooms");

		request.removeAttribute("selectedRoomIndex");

		return mapping.findForward("Sucesso");
	}
	
//############################################################################
//############################################################################
//  COMMONS - COMMONS -COMMONS -COMMONS -COMMONS -COMMONS -COMMONS -COMMONS 
//############################################################################
//############################################################################	
	private List getSelectedRooms(HttpServletRequest request)
		throws FenixActionException {

		GestorServicos gestor = GestorServicos.manager();
		Object argsSelectRooms[] =
			{
				 new InfoRoom(
					readRequestValue(request, "selectRoomsName"),
					readRequestValue(request, "selectRoomsBuilding"),
					readIntegerRequestValue(request, "selectRoomsFloor"),
					readTypeRoomRequestValue(request, "selectRoomsType"),
					readIntegerRequestValue(request, "selectRoomsCapacityNormal"),
					readIntegerRequestValue(request, "selectRoomsCapacityExame"))};
System.out.println("### Editar Sala - prepareEditRoom");					
System.out.println("## SelectRooms-name:"+readRequestValue(request, "selectRoomsName"));
System.out.println("## SelectRooms-building-"+readRequestValue(request, "selectRoomsBuilding"));
System.out.println("## SelectRooms-floor-"+readIntegerRequestValue(request, "selectRoomsFloor"));
System.out.println("## SelectRooms-type-"+readTypeRoomRequestValue(request, "selectRoomsType"));
System.out.println("## SelectRooms-capacityExam-"+readIntegerRequestValue(request, "selectRoomsCapacityNormal"));
System.out.println("## SelectRooms-capacityNormal-"+readIntegerRequestValue(request, "selectRoomsCapacityExame") );
System.out.println("## SelectedRoomIndex-"+readIntegerRequestValue(request, "selectedRoomIndex") );
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
