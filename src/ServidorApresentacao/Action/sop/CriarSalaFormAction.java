package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoRoom;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoSala;

/**
 * @author tfc130
 */
public class CriarSalaFormAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);	
		DynaActionForm criarSalaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			IUserView userView =
				(IUserView) sessao.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();
			Object argsCriarSala[] =
				{
					 new InfoRoom(
						(String) criarSalaForm.get("name"),
						(String) criarSalaForm.get("building"),
						new Integer((String) criarSalaForm.get("floor")),
						new TipoSala(
							new Integer((String) criarSalaForm.get("type"))),
						new Integer(
							(String) criarSalaForm.get("capacityNormal")),
						new Integer(
							(String) criarSalaForm.get("capacityExame")))};

			try {
				gestor.executar(userView, "CriarSala", argsCriarSala);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException("A Sala", e);
			} 
			return mapping.findForward("Sucesso");
		} else
		throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
