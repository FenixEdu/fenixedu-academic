package ServidorApresentacao.Action.commons;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 03/07/2003
 * 
 */
public class ChooseMasterDegreeDispatchAction extends DispatchAction {

	public ActionForward prepareChooseMasterDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		String executionYear = getFromRequest("executionYear", request);

		request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
		request.setAttribute("executionYear", executionYear);

		// Get the Degree List			
		Object args[] = { executionYear };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		ArrayList degreeList = null;
		try {
			degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("message.public.notfound.degrees", executionYear));
			saveErrors(request, errors);
			return mapping.getInputForward();

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}

		request.setAttribute(SessionConstants.DEGREE_LIST, degreeList);

		return mapping.findForward("PrepareSuccess");
	}

	public ActionForward chooseMasterDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

			String executionYear = getFromRequest("executionYear", request);

			DynaValidatorForm masterDegreeForm = (DynaValidatorForm) form;
			String masterDegree = (String) masterDegreeForm.get("masterDegree");

			request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
			request.setAttribute("executionYear", getFromRequest("executionYear", request));
			request.setAttribute("degree", masterDegree);

			return mapping.findForward("ChooseSuccess");
	}
	
	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}