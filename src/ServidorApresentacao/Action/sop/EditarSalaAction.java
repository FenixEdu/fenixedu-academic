package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

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
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
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
			SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = getSession(request);
		ArrayList listaSalasBean =
			(ArrayList) session.getAttribute("publico.infoRooms");
		DynaActionForm posicaoSalaFormBean =
			(DynaActionForm) session.getAttribute("posicaoFormBean");
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm salaBean = (DynaActionForm) form;

		InfoRoom sala =
			new InfoRoom(
				(String) salaBean.get("name"),
				(String) salaBean.get("building"),
				new Integer((String) salaBean.get("floor")),
				new TipoSala(new Integer((String) salaBean.get("type"))),
				new Integer((String) salaBean.get("capacityNormal")),
				new Integer((String) salaBean.get("capacityExame")));

		Object argsCriarSala[] =
			{
				new RoomKey((String) session.getAttribute("previousNameSala")),
				sala };

		try {
			GestorServicos.manager().executar(
				userView,
				"EditarSala",
				argsCriarSala);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException("A Sala", e);
		}

		session.removeAttribute("previousNameSala");

		/* Actualiza a lista de salas no "scope" de sessao */
		Integer salaSelecionadaIdx =
			(Integer) posicaoSalaFormBean.get("posicao");
		listaSalasBean.set(salaSelecionadaIdx.intValue(), sala);

		if (listaSalasBean.isEmpty())
			session.removeAttribute("publico.infoRooms");

		session.removeAttribute("posicaoFormBean");

		return mapping.findForward("Sucesso");
	}
}
