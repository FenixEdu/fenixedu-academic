package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolment;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 *
 */

public class CurricularCourseEnrolmentWithoutRulesManagerDispatchAction extends DispatchAction {

	private final String[] forwards = { "showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment", "cancel", "home" };

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[4]);
		}

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		this.initializeForm(infoEnrolmentContext, enrolmentForm);

		session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);

		return mapping.findForward(forwards[0]);
	}

	public ActionForward verifyEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[4]);
		}

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		if (isCancelled(request)) {
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			request.setAttribute("degreeType", infoEnrolmentContext.getChosenOptionalInfoDegree().getTipoCurso().getTipoCurso());
			request.setAttribute("studentNumber", infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoStudent().getNumber());
			return mapping.findForward(forwards[3]);
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = this.processEnrolment(request, enrolmentForm, session);
		List erolmentsToRemoveList = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);

		Object args[] = { infoEnrolmentContext, erolmentsToRemoveList };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ValidateActualEnrolmentWithoutRules", args);
		ActionForward nextForward = null;
		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		if (!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			this.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			nextForward = mapping.findForward(forwards[0]);
		} else {
			nextForward = mapping.findForward(forwards[1]);
		}
		return nextForward;
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[4]);
		}

		if (isCancelled(request)) {
			return mapping.findForward(forwards[0]);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		// This next line is here, and not in method "processEnrolment", because I only want to add the
		// curricular courses scopes in witch the student is already enrolled or temporarily enrolled, at this point.
		infoEnrolmentContext.getActualEnrolment().addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		Integer semester = (Integer) session.getAttribute(SessionConstants.ENROLMENT_SEMESTER_KEY);
		Integer year = (Integer) session.getAttribute(SessionConstants.ENROLMENT_YEAR_KEY);

		Object args[] = { infoEnrolmentContext, semester, year };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolmentWithoutRules", args);
		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
//			session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_STUDENT_NUMBER_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_SEMESTER_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_YEAR_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_DEGREE_NAME_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
			session.removeAttribute(SessionConstants.ENROLMENT_CAN_BE_REMOVED_KEY);
			return mapping.findForward(forwards[2]);
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			this.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[1]);
		}
	}

	public ActionForward unEnroll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(!this.isSessionAttributesValid(request)) {
			return mapping.findForward(forwards[4]);
		}

		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		Integer erolmentToRemoveIndex = new Integer((String) request.getParameter("enrolmentToRemoveIndex"));
		
		List erolmentsToRemoveList = (List) session.getAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY);
		if(erolmentsToRemoveList == null) {
			erolmentsToRemoveList = new ArrayList();
		}

		try {
			InfoEnrolment infoEnrolment = (InfoEnrolment) infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent().get(erolmentToRemoveIndex.intValue());
			erolmentsToRemoveList.add(infoEnrolment);
			if(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().contains(infoEnrolment.getInfoCurricularCourseScope())) {
				infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().remove(infoEnrolment.getInfoCurricularCourseScope());
			}
			infoEnrolmentContext.getInfoEnrolmentsAprovedByStudent().remove(erolmentToRemoveIndex.intValue());
		} catch (IndexOutOfBoundsException e) {
			// FIXME DAVID-RICARDO: O que fazer neste caso?
			throw new FenixActionException(e);
		}

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		session.setAttribute(SessionConstants.ENROLMENT_TO_REMOVE_LIST_KEY, erolmentsToRemoveList);

		return mapping.findForward(forwards[0]);
	}

	private InfoEnrolmentContext processEnrolment(HttpServletRequest request, DynaActionForm enrolmentForm, HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if (request.getParameter("curricularCourses") == null) {
			enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
		}

		Integer[] curricularCourses = (Integer[]) enrolmentForm.get("curricularCourses");

		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

		actualEnrolment.clear();
//		infoEnrolmentContext.getActualEnrolment().addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		List curricularCourseScopesToBeEnrolled = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		if (curricularCourses != null) {
			for (int i = 0; i < curricularCourses.length; i++) {
				Integer curricularCourseIndex = curricularCourses[i];
				if (curricularCourseIndex != null) {
					InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled.get(curricularCourseIndex.intValue());
						actualEnrolment.add(curricularCourseScope);
				}
			}
		}

		return infoEnrolmentContext;
	}

	private void saveErrorsFromInfoEnrolmentContext(HttpServletRequest request, InfoEnrolmentContext infoEnrolmentContext) {
		ActionErrors actionErrors = new ActionErrors();

		EnrolmentValidationResult enrolmentValidationResult = infoEnrolmentContext.getEnrolmentValidationResult();

		Map messages = enrolmentValidationResult.getMessage();

		Iterator messagesIterator = messages.keySet().iterator();
		ActionError actionError;
		while (messagesIterator.hasNext()) {
			String message = (String) messagesIterator.next();
			List messageArguments = (List) messages.get(message);
			actionError = new ActionError(message, messageArguments.toArray());
			actionErrors.add(message, actionError);
		}
		saveErrors(request, actionErrors);
	}

	private void initializeForm(InfoEnrolmentContext infoEnrolmentContext, DynaActionForm enrolmentForm) {
		List infoFinalSpan = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		Integer[] curricularCoursesIndexes = new Integer[infoFinalSpan.size()];

		for (int i = 0; i < infoFinalSpan.size(); i++) {
			curricularCoursesIndexes[i] = null;
		}
		enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
	}

	private boolean isSessionAttributesValid(HttpServletRequest request) {
		boolean result = true;
		HttpSession session = request.getSession();
		
		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		Integer studentNumber = (Integer) session.getAttribute(SessionConstants.ENROLMENT_STUDENT_NUMBER_KEY);
		Integer semester = (Integer) session.getAttribute(SessionConstants.ENROLMENT_SEMESTER_KEY);
		Integer year = (Integer) session.getAttribute(SessionConstants.ENROLMENT_YEAR_KEY);
		String degreeName = (String) session.getAttribute(SessionConstants.ENROLMENT_DEGREE_NAME_KEY);
		Integer size = (Integer) session.getAttribute(SessionConstants.ENROLMENT_CAN_BE_REMOVED_KEY);

		if( (infoEnrolmentContext == null) || (studentNumber == null) || (semester == null) || (year == null) || (degreeName == null) || (size == null) ) {
			result = false;
		}

		return result;
	}
}