package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;

import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.exceptions.notAuthorizedActionDeleteException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Nuno Antão
 * @author João Pereira
 **/

public class ManipularSalasAction extends FenixAction {

	/**
	 * @returns the name of the selected sala.
	 **/
	private InfoRoom getSelectedSala(HttpSession session) {
		DynaActionForm posicaoSalaFormBean =
			(DynaActionForm) session.getAttribute("posicaoFormBean");
		Integer salaSelecionada = (Integer) posicaoSalaFormBean.get("posicao");
		ArrayList listaSalasBean =
			(ArrayList) session.getAttribute("publico.infoRooms");
		InfoRoom sala =
			(InfoRoom) listaSalasBean.get(salaSelecionada.intValue());

		return sala;
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
			SessionUtils.validSessionVerification(request, mapping);
		HttpSession sessao = getSession(request);
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
		InfoRoom salaBean = getSelectedSala(session);
		session.removeAttribute("posicaoFormBean");

		request.setAttribute("salaFormBean", salaBean);
		session.setAttribute("publico.infoRoom", salaBean);
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
		InfoRoom salaBean = getSelectedSala(session);

		List edificios = Util.readExistingBuldings(null, null);
		List tipos = Util.readTypesOfRooms(null, null);

		session.setAttribute("publico.buildings", edificios);
		session.setAttribute("publico.types", tipos);
		session.setAttribute("previousNameSala", salaBean.getNome());

		// create the bean that holds the information about the sala to edit
		DynaActionFormClass cl;

		cl = DynaActionFormClass.getDynaActionFormClass("roomForm");
		DynaActionForm criarSalaForm = (DynaActionForm) cl.newInstance();

		criarSalaForm.set("name", salaBean.getNome());
		criarSalaForm.set("building", salaBean.getEdificio());
		criarSalaForm.set(
			"floor",
			String.valueOf(salaBean.getPiso().intValue()));
		criarSalaForm.set("type", String.valueOf(salaBean.getTipo().getTipo()));
		criarSalaForm.set(
			"capacityNormal",
			String.valueOf(salaBean.getCapacidadeNormal()));
		criarSalaForm.set(
			"capacityExame",
			String.valueOf(salaBean.getCapacidadeExame()));

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
		ArrayList listaSalasBean =
			(ArrayList) session.getAttribute("publico.infoRooms");
		IUserView userView = (IUserView) session.getAttribute("UserView");
		InfoRoom selectedSala = getSelectedSala(session);

		Object argsCriarSala[] = { new RoomKey(selectedSala.getNome())};
		try {
			GestorServicos.manager().executar(
				userView,
				"ApagarSala",
				argsCriarSala);
		} 
		catch (notAuthorizedServiceDeleteException e) {
			Object[] values = {"a sala","as aulas"};
			throw new notAuthorizedActionDeleteException("errors.invalid.delete.with.objects",values);}


		/* Actualiza a lista de salas no "scope" de sessao */
		listaSalasBean.remove(selectedSala);
		session.removeAttribute("posicaoFormBean");

		if (listaSalasBean.isEmpty())
			session.removeAttribute("publico.infoRooms");

		return mapping.findForward("SalaApagada");
	}

}
