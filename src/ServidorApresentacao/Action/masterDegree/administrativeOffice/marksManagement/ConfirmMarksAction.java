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

import DataBeans.InfoEnrolmentEvaluation;
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

	public ActionForward prepareMarksConfirmation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		Integer scopeCode = getScopeCodeFromRequestAndSetOther(request);

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
		} catch (InvalidSituationServiceException e) {
			sendErrors(request, "invalidSituation", "error.masterDegree.studentsWithoutGrade");
			return mapping.findForward("ShowMarksManagementMenu");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Collections.sort(
			infoSiteEnrolmentEvaluation.getEnrolmentEvaluations(),
			new BeanComparator("infoEnrolment.infoStudentCurricularPlan.infoStudent.number"));

		request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluation);

		String forward = findForward(request, infoSiteEnrolmentEvaluation);
		return mapping.findForward(forward);

	}

	private String findForward(HttpServletRequest request, InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation) {
		String useCase = (String) getFromRequest("useCase", request);
		String forward = new String("MarksConfirmationMenu");
		if (useCase != null && useCase.equals("confirm")) {
			forward = "MarksConfirmation";
		} else if (useCase != null && useCase.equals("print")) {
			String degreeName =
				((InfoEnrolmentEvaluation) (infoSiteEnrolmentEvaluation.getEnrolmentEvaluations().get(0)))
					.getInfoEnrolment()
					.getInfoStudentCurricularPlan()
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getNome();
			request.setAttribute("degreeName", degreeName);
			forward = "MarksPrint";
		}
		return forward;
	}

	private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
		ActionErrors errors = new ActionErrors();
		errors.add(arg0, new ActionError(arg1));
		saveErrors(request, errors);
	}

	private Integer getScopeCodeFromRequestAndSetOther(HttpServletRequest request) {
		String executionYear = (String) getFromRequest("executionYear", request);
		String degree = (String) getFromRequest("degree", request);
		String curricularCourse = (String) getFromRequest("curricularCourse", request);
		Integer scopeCode = new Integer((String) getFromRequest("scopeCode", request));

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
		Boolean result = null;
		try {
			result = (Boolean) serviceManager.executar(userView, "ConfirmStudentsFinalEvaluation", args);
		} catch (NonExistingServiceException e) {
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return mapping.findForward("ShowMarksManagementMenu");
	}

	private Object getFromRequest(String parameter, HttpServletRequest request) {
		Object parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (Object) request.getAttribute(parameter);
		}
		return parameterString;
	}
}