package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoAutenticacao;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.mapping.ActionMappingForAuthentication;

/**
 * @author jorge
 */
public class AuthenticationAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		IUserView userView = null;
		try {
			DynaActionForm authenticationForm = (DynaActionForm) form;
			ActionMappingForAuthentication authenticationMapping = (ActionMappingForAuthentication) mapping;

			GestorServicos gestor = GestorServicos.manager();
			Object argsAutenticacao[] =
				{
					authenticationForm.get("username"),
					authenticationForm.get("password"),
					authenticationMapping.getApplication()};

			userView =
				(IUserView) gestor.executar(
					null,
					"Autenticacao",
					argsAutenticacao);
		} catch (ExcepcaoAutenticacao e) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"invalidAuthentication",
				new ActionError("errors.invalidAuthentication"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		// Invalidate existing session if it exists
		HttpSession sessao = request.getSession(false);
		if (sessao != null) {
			sessao.invalidate();
		}

		// Create a new session for this user
		sessao = request.getSession(true);

		// Store the UserView into the session and return
		sessao.setAttribute(SessionConstants.U_VIEW, userView);
		sessao.setAttribute(
			SessionConstants.SESSION_IS_VALID,
			new Boolean(true));

		return mapping.findForward("sucess");
	}
}
