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
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegree;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorApresentacao.Action.commons.TransactionalDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.OutOfCurricularEnrolmentPeriodActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CurricularCourseType;

/**
 * @author David Santos
 *
 */

public class CurricularCourseEnrolmentManagerDispatchAction2 extends TransactionalDispatchAction {

	private final String[] forwards = { "showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment" };

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.createToken(request);

		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { userView };

		InfoEnrolmentContext infoEnrolmentContext = null;
		try {
			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCourses", args);
		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			throw new OutOfCurricularEnrolmentPeriodActionException(e.getMessageKey(), e.getStartDate(), e.getEndDate(), mapping.findForward("globalOutOfPeriod"));
		}

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		this.initializeForm(infoEnrolmentContext, (DynaActionForm) form);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward verifyEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		DynaActionForm enrolmentForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = processEnrolment(request, enrolmentForm, session);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ValidateActualEnrolmentWithRules", args);

		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

		ActionForward nextForward = null;
		if(!infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			nextForward = getBeforeForward(request, mapping);
		} else {
			nextForward = getNextForward(request, mapping);
		}
		return nextForward;
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		if (isCancelled(request)) {
			return getBeforeForward(request, mapping);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ConfirmActualEnrolmentWithRules", args);

		if (infoEnrolmentContext.getEnrolmentValidationResult().isSucess()) {
			session.removeAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
			return getNextForward(request, mapping);
		} else {
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward(forwards[0]);
		}

	}

	public ActionForward startEnrolmentInOptional(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(optionalCurricularCourseIndex.intValue());
		infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoCurricularCourseScope);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableDegreesForOptionWithRules", args);

		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		ActionForward forward = null;
		if(infoDegreeList.size() == 1) {
			infoEnrolmentContext.setChosenOptionalInfoDegree((InfoDegree) infoDegreeList.get(0));

			args[0] = infoEnrolmentContext;

			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCoursesForOptionWithRules", args);
			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
			enrolmentForm.set("optionalCurricularCourse", null);
			forward = mapping.findForward("concreteOptionalList");
		} else {
			forward = mapping.findForward("searchOptionalCurricularCourses");
		}

		return forward;
	}

	public ActionForward showOptionalCurricularCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if(isCancelled(request)) {
			if(enrolmentForm.get("curricularCourses") == null) {
				enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
			}
			Integer[] curricularCoursesIndexes = (Integer[]) enrolmentForm.get("curricularCourses");
			Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
			curricularCoursesIndexes[optionalCurricularCourseIndex.intValue()] = null;
			enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
			return mapping.findForward(forwards[0]);
		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		enrolmentForm.set("optionalCurricularCourse", null);
		Integer infoDegreeInternalId = (Integer) enrolmentForm.get("infoDegree");
		List infoDegreeList = infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses();
		
		Iterator iterator = infoDegreeList.iterator();
		InfoDegree infoDegree = null;
		while(iterator.hasNext()) {
			InfoDegree infoDegree2 = (InfoDegree) iterator.next();
			if(infoDegree2.getIdInternal().equals(infoDegreeInternalId)) {
				infoDegree = infoDegree2;
				break;
			}
		}
		infoEnrolmentContext.setChosenOptionalInfoDegree(infoDegree);

		Object args[] = { infoEnrolmentContext };

		infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "ShowAvailableCurricularCoursesForOptionWithoutRules", args);
		session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);
		return mapping.findForward("concreteOptionalList");
	}

	public ActionForward chooseOptionalCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, form, mapping, "error.transaction.enrolment");

		DynaValidatorForm enrolmentForm = (DynaValidatorForm) form;
		HttpSession session = request.getSession();

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

//		if(isCancelled(request)) {
//			if(enrolmentForm.get("curricularCourses") == null) {
//				enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);
//			}
//			Integer[] curricularCoursesIndexes = (Integer[]) enrolmentForm.get("curricularCourses");
//			Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
//			curricularCoursesIndexes[optionalCurricularCourseIndex.intValue()] = null;
//			enrolmentForm.set("curricularCourses", curricularCoursesIndexes);
//			return mapping.findForward(forwards[0]);
//		}

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer optionalCurricularCourseIndex = (Integer) enrolmentForm.get("optionalCourseIndex");
		
		InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().get(optionalCurricularCourseIndex.intValue());

		Integer optionalCourseChoosenIndex = (Integer) enrolmentForm.get("optionalCurricularCourse");

		if(optionalCourseChoosenIndex == null) {
			infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage("error.choose.one.optional.curricular.course");
			this.saveErrorsFromInfoEnrolmentContext(request, infoEnrolmentContext);
			return mapping.findForward("concreteOptionalList");
		} else {
			InfoCurricularCourse infoCurricularCourseOptional = (InfoCurricularCourse) infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().get(optionalCourseChoosenIndex.intValue());

			infoEnrolmentContext.setInfoChosenOptionalCurricularCourseScope(infoCurricularCourseScope);

			Object args[] = { infoEnrolmentContext, infoCurricularCourseOptional };

			infoEnrolmentContext = (InfoEnrolmentContext) ServiceUtils.executeService(userView, "SelectOptionalCurricularCourseWithoutRules", args);

			session.setAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY, infoEnrolmentContext);

			return mapping.findForward(forwards[0]);
		}
	}

	private void initializeForm(InfoEnrolmentContext infoEnrolmentContext, DynaActionForm enrolmentForm) {
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		List infoFinalSpan = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		Integer[] curricularCoursesIndexes = new Integer[infoFinalSpan.size()];

		for (int i = 0; i < infoFinalSpan.size(); i++) {
			InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoFinalSpan.get(i);
			InfoCurricularCourse infoCurricularCourse = infoCurricularCourseScope.getInfoCurricularCourse();

			if (infoCurricularCourse.getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
				List optionalEnrolments = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
				Iterator optionalEnrolmentsIterator = optionalEnrolments.iterator();
				while (optionalEnrolmentsIterator.hasNext()) {
					InfoEnrolmentInOptionalCurricularCourse optionalEnrolment = (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
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
	private InfoEnrolmentContext processEnrolment(HttpServletRequest request, DynaActionForm enrolmentForm, HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) session.getAttribute(SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);

		if (request.getParameter("curricularCourses") == null)
			enrolmentForm.set("curricularCourses", new Integer[infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().size()]);

		Integer[] curricularCourses = (Integer[]) enrolmentForm.get("curricularCourses");

		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();

		actualEnrolment.clear();
		actualEnrolment.addAll(infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled());

		List curricularCourseScopesToBeEnrolled = infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		List optionalCurricularCoursesChoosen = new ArrayList();
		if (curricularCourses != null) {
			for (int i = 0; i < curricularCourses.length; i++) {
				Integer curricularCourseIndex = curricularCourses[i];
				if (curricularCourseIndex != null) {
					InfoCurricularCourseScope curricularCourseScope = (InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled.get(curricularCourseIndex.intValue());
					if (!curricularCourseScope.getInfoCurricularCourse().getType().equals(CurricularCourseType.OPTIONAL_COURSE_OBJ)) {
						actualEnrolment.add(curricularCourseScope);
					} else {
						optionalCurricularCoursesChoosen.add(curricularCourseScope.getInfoCurricularCourse());
					}
				}
			}
		}

		List enrolmentsInOptionalCourses = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();

		if (enrolmentsInOptionalCourses.size() != optionalCurricularCoursesChoosen.size()) {
			Iterator optionalEnrolmentsIterator = enrolmentsInOptionalCourses.iterator();
			while (optionalEnrolmentsIterator.hasNext()) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) optionalEnrolmentsIterator.next();
				InfoCurricularCourse optionalCurricularCourse = infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().getInfoCurricularCourse();
				if (!optionalCurricularCoursesChoosen.contains(optionalCurricularCourse)) {
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
	private ActionForward getBeforeForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
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
	private ActionForward getNextForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
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

	private InfoStudent getInfoStudent(HttpServletRequest request, ActionForm form, IUserView userView) throws FenixActionException {
		DynaActionForm enrolmentForm = (DynaActionForm) form;
		InfoStudent infoStudent = (InfoStudent) request.getAttribute(SessionConstants.STUDENT);
		if(infoStudent == null) {
			Integer infoStudentOID = (Integer) enrolmentForm.get("studentOID");
			try {
				Object args[] = { infoStudentOID };
				infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "GetStudentByOID", args);
			} catch(FenixServiceException e) {
				throw new FenixActionException(e);
			}
		} else {
			enrolmentForm.set("studentOID", infoStudent.getIdInternal());
		}
		return infoStudent;
	}
}