package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.AuthorizationUtils;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.commons.GeneralCurricularCourseEnrolmentManagerDispatchAction;
import ServidorApresentacao.Action.equivalence.ManualEquivalenceManagerDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.RoleType;
import Util.TipoCurso;

/**
 * @author David Santos
 *
 */

public class CurricularCourseEnrolmentWithoutRulesManagerDispatchAction extends GeneralCurricularCourseEnrolmentManagerDispatchAction {

	private final String[] forwards = { "showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment", "error" };

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		super.createToken(request);

		session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
		session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer studentOID = (Integer) request.getAttribute("studentOID");
		enrolmentForm.set("studentOID", studentOID);

		InfoStudent infoStudent = super.getInfoStudent(request, form, userView);

		List listOfChosenCurricularYears = (List) request.getAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY);
//		List listOfChosenCurricularSemesters = (List) request.getAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY);
		InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) request.getAttribute(SessionConstants.DEGREE);

//		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		Integer executionPeriodOID = (Integer) request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID);
		InfoExecutionPeriod infoExecutionPeriod = PrepareStudentDataForEnrolmentWithoutRulesDispatchAction.getExecutionPeriod(request, executionPeriodOID);

		Object args[] = { infoStudent, infoExecutionPeriod, infoExecutionDegree/*, listOfChosenCurricularSemesters*/, listOfChosenCurricularYears };

		InfoEnrolmentContext infoEnrolmentContext = null;
		try {
			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "PrepareEnrolmentContext", args);
			ManualEquivalenceManagerDispatchAction.sort(infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent(), infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled());
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

		super.initializeForm(infoEnrolmentContext, enrolmentForm);

		super.initializeRemovedCurricularCourseScopesList(request, infoEnrolmentContext);

		return mapping.findForward(forwards[0]);
	}

	public ActionForward verifyEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		HttpSession session = request.getSession();
		DynaActionForm enrolmentForm = (DynaActionForm) form;

		if (isCancelled(request)) {
			return this.error(mapping, form, request, response);
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = super.processEnrolment(request, enrolmentForm, session);
		List erolmentsToRemoveList = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);

		Object args[] = { infoEnrolmentContext, erolmentsToRemoveList };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ValidateActualEnrolmentWithoutRules", args);

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

		if (!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			this.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		} else {
			return mapping.findForward(forwards[1]);
		}
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		if (isCancelled(request)) {
			return mapping.findForward(forwards[0]);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolmentWithoutRules", args);

		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
			session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
			return mapping.findForward(forwards[2]);
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			super.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}
	}

	public ActionForward error(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		if(AuthorizationUtils.containsRole(userView.getRoles(), RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE)) {
			request.setAttribute("degreeType", TipoCurso.MESTRADO_OBJ.getTipoCurso());
		} else if(AuthorizationUtils.containsRole(userView.getRoles(), RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {
			request.setAttribute("degreeType", TipoCurso.LICENCIATURA_OBJ.getTipoCurso());
		} else {
			request.setAttribute("degreeType", infoEnrolmentContext.getChosenOptionalInfoDegree().getTipoCurso().getTipoCurso());
		}
		
		request.setAttribute("studentNumber", infoEnrolmentContext.getInfoStudent().getNumber());
		request.setAttribute("executionPeriodOID", infoEnrolmentContext.getInfoExecutionPeriod().getIdInternal());
		session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
		session.removeAttribute(SessionConstants.ACTUAL_ENROLMENT_KEY);
		return mapping.findForward(forwards[3]);
	}
}