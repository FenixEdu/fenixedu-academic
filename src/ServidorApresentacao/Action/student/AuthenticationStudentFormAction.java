/*
 * AutenticacaoSOPFormAction.java
 *
 * Created on December 16th, 2002, 00:57
 */

package ServidorApresentacao.Action.student;

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

import DataBeans.InfoStudent;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

public class AuthenticationStudentFormAction extends FenixAction {

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
				autenticacaoForm.get("username"),
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
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		// Create a new session for this user
		session = request.getSession(true);

		// Store the UserView into the session and return
		session.setAttribute(SessionConstants.U_VIEW, userView);

		Object argsReadStudent[] =
			{(String) (autenticacaoForm.get("username"))};
		InfoStudent infoStudent;
		try {
			infoStudent =
				(InfoStudent) gestor.executar(
					userView,
					"ReadStudentByUsername",
					argsReadStudent);
		} catch (Exception e) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
				"invalidAuthentication",
				new ActionError("errors.invalidAuthentication"));
			saveErrors(request, actionErrors);
			return mapping.getInputForward();
		}
		session.setAttribute("infoStudent", infoStudent);

		return mapping.findForward("Student");
	}
}
