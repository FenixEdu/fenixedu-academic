package ServidorApresentacao.Action.degreeAdministrativeOffice;

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
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 *
 */

public class CurricularCourseEnrolmentWithoutRulesManagerDispatchAction extends DispatchAction {

	private final String[] forwards = { "showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment", "back" };

	public ActionForward verifyEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		if (isCancelled(request)) {
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			request.setAttribute("degreeType", infoEnrolmentContext.getChosenOptionalInfoDegree().getTipoCurso());
			request.setAttribute("studentNumber", infoEnrolmentContext.getInfoStudentActiveCurricularPlan().getInfoStudent().getNumber());
			return mapping.findForward(forwards[3]);
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = this.processEnrolment(request, enrolmentForm, session);

		Object args[] = { infoEnrolmentContext };

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

		if (isCancelled(request)) {
			return mapping.findForward(forwards[0]);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolmentWithoutRules", args);
		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			return mapping.findForward(forwards[2]);
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			this.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}
	}

	private InfoEnrolmentContext processEnrolment(HttpServletRequest request, DynaActionForm enrolmentForm, HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if (request.getParameter("curricularCourses") == null) {
			enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
		}

		Integer[] curricularCourses = (Integer[]) enrolmentForm.get("curricularCourses");

		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

		actualEnrolment.clear();
		actualEnrolment.addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		List curricularCourseScopesToBeEnrolled = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
//		List optionalCurricularCoursesChoosen = new ArrayList();
		if (curricularCourses != null) {
			for (int i = 0; i < curricularCourses.length; i++) {
				Integer curricularCourseIndex = curricularCourses[i];
				if (curricularCourseIndex != null) {
					InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled.get(curricularCourseIndex.intValue());
//					if (!curricularCourseScope.getInfoCurricularCourse().getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
						actualEnrolment.add(curricularCourseScope);
//					} else {
//						optionalCurricularCoursesChoosen.add(curricularCourseScope.getInfoCurricularCourse());
//					}
				}
			}
		}

//		List enrolmentsInOptionalCourses = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
//
//		if (enrolmentsInOptionalCourses.size() != optionalCurricularCoursesChoosen.size()) {
//			Iterator optionalEnrolmentsIterator = enrolmentsInOptionalCourses.iterator();
//			while (optionalEnrolmentsIterator.hasNext()) {
//				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
//				InfoCurricularCourse optionalCurricularCourse = infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().getInfoCurricularCourse();
//				if (!optionalCurricularCoursesChoosen.contains(optionalCurricularCourse)) {
//					optionalEnrolmentsIterator.remove();
//				}
//			}
//		}
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
}