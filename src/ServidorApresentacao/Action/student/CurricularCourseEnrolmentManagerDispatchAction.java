/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.student;

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
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.exceptions.OutOfCurricularEnrolmentPeriodActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author jpvl
 */
public class CurricularCourseEnrolmentManagerDispatchAction
	extends DispatchAction {
	private final String[] forwards =
		{
			"showAvailableCurricularCourses",
			"verifyEnrolment",
			"acceptEnrolment" };
	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		createToken(request);

		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { userView };

		InfoEnrolmentContext infoEnrolmentContext = null;
		try {
			infoEnrolmentContext =
				(InfoEnrolmentContext) ServiceUtils.executeService(
					userView,
					"ShowAvailableCurricularCourses",
					args);
		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			throw new OutOfCurricularEnrolmentPeriodActionException(e.getMessageKey(), e.getStartDate(), e.getEndDate(), mapping.findForward("globalOutOfPeriod"));
		}

		session.setAttribute(
			SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
			infoEnrolmentContext);
		initializeForm(infoEnrolmentContext, (DynaActionForm) form);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward verifyEnrolment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		validateToken(request, form, mapping);

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoEnrolmentContext infoEnrolmentContext =
			processEnrolment(request, enrolmentForm, session);


		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext =
			(InfoEnrolmentContext) ServiceUtils.executeService(
				userView,
				"ValidateActualEnrolment",
				args);
		ActionForward nextForward = null;
		session.setAttribute(
			SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
			infoEnrolmentContext);
		if (!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			nextForward = getBeforeForward(request, mapping);
		} else {
			nextForward = getNextForward(request, mapping);
		}
		return nextForward;
	}

	public ActionForward accept(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		if (isCancelled(request)) {
			return getBeforeForward(request, mapping);
		}
		validateToken(request, form, mapping);

		HttpSession session = request.getSession();

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolment", args);
		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()){
			return getNextForward(request, mapping); 
		}else{
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}
		
	}

	public ActionForward startEnrolmentInOptional(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		if (isCancelled(request)) {
			return mapping.findForward("showAvailableCourses");
		}
		validateToken(request, form, mapping);

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		enrolmentForm.set("optionalCurricularCourse", null);
		
		
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Integer optionalCurricularCourseIndex =
			(Integer) enrolmentForm.get("optionalCourseIndex");

		InfoCurricularCourseScope infoCurricularCourseScope =
			(InfoCurricularCourseScope) infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled()
				.get(optionalCurricularCourseIndex.intValue());

		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(
			infoCurricularCourseScope);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext =
			(InfoEnrolmentContext) ServiceUtils.executeService(
				userView,
				"ShowAvailableDegreesForOption",
				args);

		List infoDegreeList =
			infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		ActionForward forward = null;
		if (infoDegreeList.size() == 1) {
			infoEnrolmentContext.setChosenOptionalInfoDegree(
				(InfoDegree) infoDegreeList.get(0));

			args[0] = infoEnrolmentContext;

			infoEnrolmentContext =
				(InfoEnrolmentContext) ServiceUtils.executeService(
					userView,
					"ShowAvailableCurricularCoursesForOption",
					args);
			session.setAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
				infoEnrolmentContext);
			forward = mapping.findForward("concreteOptionalList");
		} else {
			forward = mapping.findForward("searchOptionalCurricularCourses");
		}

		return forward;
	}
	public ActionForward chooseOptionalCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		if (isCancelled(request)) {
			return mapping.findForward("showAvailableCourses");
		}
		validateToken(request, form, mapping);

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Integer optionalCurricularCourseIndex =
			(Integer) enrolmentForm.get("optionalCourseIndex");

		InfoCurricularCourseScope infoCurricularCourseScope =
			(InfoCurricularCourseScope) infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled()
				.get(optionalCurricularCourseIndex.intValue());

		Integer optionalCourseChoosenIndex =
			(Integer) enrolmentForm.get("optionalCurricularCourse");

		InfoCurricularCourse infoCurricularCourseOptional =
			(InfoCurricularCourse) infoEnrolmentContext
				.getOptionalInfoCurricularCoursesToChooseFromDegree()
				.get(optionalCourseChoosenIndex.intValue());

		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(
			infoCurricularCourseScope);

		Object args[] = { infoEnrolmentContext, infoCurricularCourseOptional };

		System.out.println(
			"Escolhida:" + infoCurricularCourseOptional.toString());

		infoEnrolmentContext =
			(InfoEnrolmentContext) ServiceUtils.executeService(
				userView,
				"SelectOptionalCurricularCourse",
				args);
		session.setAttribute(
			SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
			infoEnrolmentContext);

		return mapping.findForward(forwards[0]);
	}

	/**
	 * @param request	
	 */
	private void validateToken(
		HttpServletRequest request,
		ActionForm form,
		ActionMapping mapping)
		throws FenixTransactionException {

		if (!isTokenValid(request)) {
			form.reset(mapping, request);
			throw new FenixTransactionException("error.transaction.enrolment");
		} else {
			createToken(request);
		}
	}

	/**
	 * @param request
	 */
	private void createToken(HttpServletRequest request) {
		generateToken(request);
		saveToken(request);
	}

	private void initializeForm(
		InfoEnrolmentContext infoEnrolmentContext,
		DynaActionForm enrolmentForm) {
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		List infoFinalSpan =
			infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		Integer[] curricularCoursesIndexes = new Integer[infoFinalSpan.size()];

		for (int i = 0; i < infoFinalSpan.size(); i++) {
			InfoCurricularCourseScope infoCurricularCourseScope =
				(InfoCurricularCourseScope) infoFinalSpan.get(i);
			InfoCurricularCourse infoCurricularCourse =
				infoCurricularCourseScope.getInfoCurricularCourse();

			if (infoCurricularCourse
				.getType()
				.equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
				List optionalEnrolments =
					infoEnrolmentContext
						.getInfoOptionalCurricularCoursesEnrolments();
				Iterator optionalEnrolmentsIterator =
					optionalEnrolments.iterator();
				while (optionalEnrolmentsIterator.hasNext()) {
					InfoEnrolmentInOptionalCurricularCourse optionalEnrolment =
						(InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator
							.next();
					if (optionalEnrolment.getInfoCurricularCourseScope().getInfoCurricularCourse().equals(infoCurricularCourse)) {
						curricularCoursesIndexes[i] = new Integer(i);
						break;
					}
				}
			} else {
				if (actualEnrolment.contains(infoCurricularCourseScope)) {
					curricularCoursesIndexes[i] = new Integer(i);
				} else {
					curricularCoursesIndexes[i] = null;
				}
			}
		}
		enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
	}

	/**
	 * @param enrolmentForm
	 * @param session
	 * @return
	 */
	private InfoEnrolmentContext processEnrolment(
		HttpServletRequest request,
		DynaActionForm enrolmentForm,
		HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if (request.getParameter("curricularCourses") == null)
			enrolmentForm.set(
				"curricularCourses",
				new Integer[infoEnrolmentContext
					.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled()
					.size()]);

		Integer[] curricularCourses =
			(Integer[]) enrolmentForm.get("curricularCourses");

		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

		actualEnrolment.clear();
		actualEnrolment.addAll(
			infoEnrolmentContext
				.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		List curricularCourseScopesToBeEnrolled =
			infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		List optionalCurricularCoursesChoosen = new ArrayList();
		if (curricularCourses != null) {
			for (int i = 0; i < curricularCourses.length; i++) {
				Integer curricularCourseIndex = curricularCourses[i];
				if (curricularCourseIndex != null) {
					InfoCurricularCourseScope curricularCourseScope =
						(
							InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled
								.get(
							curricularCourseIndex.intValue());
					if (!curricularCourseScope
						.getInfoCurricularCourse()
						.getType()
						.equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
						actualEnrolment.add(curricularCourseScope);
					} else {
						optionalCurricularCoursesChoosen.add(
							curricularCourseScope.getInfoCurricularCourse());
					}
				}
			}
		}

		List enrolmentsInOptionalCourses =
			infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();

		if (enrolmentsInOptionalCourses.size()
			!= optionalCurricularCoursesChoosen.size()) {
			Iterator optionalEnrolmentsIterator =
				enrolmentsInOptionalCourses.iterator();
			while (optionalEnrolmentsIterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse =
					(InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator
						.next();
				InfoCurricularCourse optionalCurricularCourse =
					infoEnrolmentInOptionalCurricularCourse
						.getInfoCurricularCourseScope().getInfoCurricularCourse();
				if (!optionalCurricularCoursesChoosen
					.contains(optionalCurricularCourse)) {
					optionalEnrolmentsIterator.remove();
				}
			}
		}
		return infoEnrolmentContext;
	}

	/**
	 * @param form
	 * @param mapping
	 * @return
	 */
	private ActionForward getBeforeForward(
		HttpServletRequest request,
		ActionMapping mapping)
		throws Exception {
		int step = 0;
		try {
			step = Integer.parseInt(request.getParameter("step"));
		} catch (NumberFormatException e) {
		}

		if (step < 0 && step >= forwards.length)
			throw new IllegalArgumentException("Illegal step!");

		if (step != 0)
			step -= 1;

		return mapping.findForward(forwards[step]);
	}

	/**
	 * @param form
	 * @return
	 */
	private ActionForward getNextForward(
		HttpServletRequest request,
		ActionMapping mapping)
		throws Exception {
		int step = 0;
		try {
			step = Integer.parseInt(request.getParameter("step"));
		} catch (NumberFormatException e) {
		}

		step = step < 0 ? 0 : step;
		step = step > forwards.length ? forwards.length - 2 : step;
		return mapping.findForward(forwards[step + 1]);
	}

	/**
	 * @param request
	 * @param infoEnrolmentContext
	 */
	private void saveErrorsFromInfoEnrolmentContext(
		HttpServletRequest request,
		InfoEnrolmentContext infoEnrolmentContext) {
		ActionErrors actionErrors = new ActionErrors();

		EnrolmentValidationResult enrolmentValidationResult =
			infoEnrolmentContext.getEnrolmentValidationResult();

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
