package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoSiteEnrolmentEvaluation;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 10/07/2003
 * 
 */
public class ConfirmMarksAction extends DispatchAction {

	public ActionForward prepareMarksConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		Integer scopeCode = getScopeCodeFromRequestAndSetOther(request);
		String useCase = getFromRequest("useCase", request);

		// Get students final evaluation			
		Object args[] = { scopeCode };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
		try {
			infoSiteEnrolmentEvaluation =
				(InfoSiteEnrolmentEvaluation) serviceManager.executar(userView, "ReadStudentsFinalEvaluationForConfirmation", args);
		} catch (NonExistingServiceException e) {
			sendErrors(request, "nonExisting", "message.masterDegree.notfound.students");
			return mapping.getInputForward();
		} catch (ExistingServiceException e) {
			sendErrors(request, "existing", "message.masterDegree.evaluation.alreadyConfirmed");
			return mapping.findForward("ShowMarksManagementMenu");
		}catch (InvalidSituationServiceException e) {
		sendErrors(request, "invalidSituation", "error.masterDegree.studentsWithoutGrade");
		return mapping.getInputForward();
	} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Collections.sort(
			infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(),
			new BeanComparator("infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

		request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

		if(useCase != null){
			return mapping.findForward("MarksConfirmation");
		}
		return mapping.findForward("MarksConfirmationMenu");

	}

	private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
		ActionErrors errors = new ActionErrors();
		errors.add(arg0, new ActionError(arg1));
		saveErrors(request, errors);
	}

	private Integer getScopeCodeFromRequestAndSetOther(HttpServletRequest request) {
		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);
		String curricularCourse = getFromRequest("curricularCourse", request);
		Integer scopeCode = new Integer(getFromRequest("scopeCode", request));

		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("scopeCode", scopeCode);
		return scopeCode;
	}

	public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		Integer scopeCode = getScopeCodeFromRequestAndSetOther(request);

		//		set final evaluation to final state
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Object args[] = { scopeCode, userView };
		GestorServicos serviceManager = GestorServicos.manager();
		Boolean result = Boolean.FALSE;
		try {
			result = (Boolean) serviceManager.executar(userView, "ConfirmStudentsFinalEvaluation", args);
		} catch (NonExistingServiceException e) {
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("ShowMarksManagementMenu");
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}