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
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.notAuthorizedActionDeleteException;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.TipoSala;

/**
 * @author Nuno Antão
 * @author João Pereira
 **/

public class ManipularSalasAction extends FenixAction {

	/**
	 * @returns the name of the selected sala.
	 **/
	private InfoRoom getSelectedSala(
		ActionForm form,
		HttpServletRequest request)
		throws FenixActionException {

		DynaActionForm posicaoSalaFormBean = (DynaActionForm) form;
		Integer salaSelecionada = (Integer) posicaoSalaFormBean.get("index");

		List listaSalasBean = getSalas(request);

		InfoRoom sala = null;
		if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
			Collections.sort(listaSalasBean);
			sala = (InfoRoom) listaSalasBean.get(salaSelecionada.intValue());
		}
		return sala;
	}

	/**
	 * @returns the name of the selected sala.
	 **/
	private List getSalas(HttpServletRequest request)
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

		List listaSalasBean = null;
		try {
			listaSalasBean =
				(List) gestor.executar(null, "SelectRooms", argsSelectRooms);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (listaSalasBean != null && !listaSalasBean.isEmpty()) {
			Collections.sort(listaSalasBean);
		}
		return listaSalasBean;
	}

	/**
	 * Executes the selected action, depending on the pressed button.
	 * The action depends on the value of the "operation" parameter.
	 * It can be prepare the information about the selected sala, to show
	 * or edit it, or delete the selected sala.
	 **/
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		String parameter = request.getParameter("operation");

		if (parameter.equals("Ver Sala")) {
			return prepararVerSala(mapping, form, request, response);
		}
		if (parameter.equals("Editar Sala")) {
			return prepararEditarSala(mapping, form, request, response);
		}
		if (parameter.equals("Apagar Sala")) {
			return apagarSala(mapping, form, request, response);
		}

		return (mapping.findForward("Voltar"));
	}

	/**
	 * Prepares the right information about the selected sala so that it
	 * can be shown to the user. Places a java bean object with
	 * information about the selected sala in the attribute
	 * "salaFormBean" of the session and fowards to the mapping
	 * "VerSala".
	 **/
	public ActionForward prepararVerSala(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = getSession(request);
		InfoRoom salaBean = getSelectedSala(form, request);
		request.removeAttribute(mapping.getAttribute());

		request.setAttribute("salaFormBean", salaBean);
		request.setAttribute("publico.infoRoom", salaBean);
		return (mapping.findForward("VerSala"));
	}

	/**
	 * Prepares the information about the selected sala so that it can
	 * be shown to the user. Places a java bean object with information
	 * about the selected sala in the attribute "salaFormBean" of the
	 * session and forwards to the mapping "EditarSala".
	 **/
	public ActionForward prepararEditarSala(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = getSession(request);
		InfoRoom salaBean = getSelectedSala(form, request);

		List edificios = Util.readExistingBuldings(null, null);
		List tipos = Util.readTypesOfRooms(null, null);

		request.setAttribute("publico.buildings", edificios);
		request.setAttribute("publico.types", tipos);
		request.setAttribute("previousNameSala", salaBean.getNome());

		// create the bean that holds the information about the sala to edit
		DynaActionFormClass cl;

		ModuleConfig moduleConfig = (ModuleConfig) mapping.getModuleConfig();

		FormBeanConfig formBeanConfig =
			moduleConfig.findFormBeanConfig("roomForm");
		cl = DynaActionFormClass.createDynaActionFormClass(formBeanConfig);

		DynaActionForm criarSalaForm = (DynaActionForm) cl.newInstance();

		criarSalaForm.set("name", salaBean.getNome());
		criarSalaForm.set("building", salaBean.getEdificio());
		criarSalaForm.set("floor", String.valueOf(salaBean.getPiso().intValue()));
		criarSalaForm.set("type", String.valueOf(salaBean.getTipo().getTipo()));
		criarSalaForm.set("capacityNormal", String.valueOf(salaBean.getCapacidadeNormal()));
		criarSalaForm.set("capacityExame", String.valueOf(salaBean.getCapacidadeExame()));

		request.setAttribute("criarSalaForm", criarSalaForm);

		return (mapping.findForward("EditarSala"));
	}

	/**
	 * Removes the selected sala from the database used by the application.
	 **/
	public ActionForward apagarSala(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = getSession(request);
		IUserView userView = (IUserView) session.getAttribute("UserView");

		ArrayList listaSalasBean = (ArrayList) getSalas(request);
		InfoRoom selectedSala = getSelectedSala(form, request);

		Object argsCriarSala[] = { new RoomKey(selectedSala.getNome())};
		try {
			GestorServicos.manager().executar(
				userView,
				"ApagarSala",
				argsCriarSala);
		} catch (notAuthorizedServiceDeleteException e) {
			Object[] values = { "a sala", "as aulas" };
			throw new notAuthorizedActionDeleteException(
				"errors.invalid.delete.with.objects",
				values);
		}

		/* Actualiza a lista de salas no "scope" de request */
		listaSalasBean.remove(selectedSala);
		request.removeAttribute(mapping.getAttribute());

		if (listaSalasBean.isEmpty()) {
			request.removeAttribute("publico.infoRooms");
		} else {
			request.setAttribute("publico.infoRooms", listaSalasBean);
		}

		return mapping.findForward("SalaApagada");
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
