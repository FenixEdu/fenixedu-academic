/*
 * AutenticacaoSOPFormAction.java
 *
 * Created on 15 de Novembro de 2002, 15:17
 */

package ServidorApresentacao.Action.sop;

/**
 *
 * @author tfc130
 **/
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


// FIXME : MT IMPORTANTE !! da maneira como esta para o SOP se autenticar e verificada a password e o username
// na tabela Pessoa. Isto implica que qualquer pessoa possa entrar no portal do SOP ... alunos, funcionarios, docentes ...

public class AutenticacaoSOPFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		DynaActionForm autenticacaoForm = (DynaActionForm) form;

		GestorServicos gestor = GestorServicos.manager();
		Object argsAutenticacao[] =
			{
				autenticacaoForm.get("utilizador"),
				autenticacaoForm.get("password")};

		IUserView userView = null;
		try {
			userView =
				(IUserView) gestor.executar(
					null,
					"Autenticacao",
					argsAutenticacao);
		} catch (Exception e) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"invalidAuthentication",
				new ActionError("errors.invalidAuthentication"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();

		}

		// Invalidate existing session if it exists
		HttpSession sessao = request.getSession(true);
//		if (sessao != null) {
//			sessao.invalidate();
//		}

		// Create a new session for this user
//		sessao = request.getSession(true);

		// Store the UserView into the session and return
		sessao.setAttribute(SessionConstants.U_VIEW, userView);
		sessao.setAttribute(SessionConstants.SESSION_IS_VALID, new Boolean(true));
		return mapping.findForward("SOP");
	}
}
