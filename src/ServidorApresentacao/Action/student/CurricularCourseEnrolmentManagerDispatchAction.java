/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoCurricularCourseScope;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorApresentacao.Action.exceptions.FenixTransactionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jpvl
 */
public class CurricularCourseEnrolmentManagerDispatchAction
	extends DispatchAction {
	private final String []forwards = {"showAvailableCurricularCourses", "verifyEnrolment", "acceptEnrolment"};
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

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) ServiceUtils.executeService(
				userView,
				"ShowAvailableCurricularCourses",
				args);

		session.setAttribute(
			SessionConstants.INFO_ENROLMENT_CONTEXT_KEY,
			infoEnrolmentContext);

		return mapping.findForward(forwards[0]);
	}

	/**
	 * @param form
	 * @param mapping
	 * @return
	 */
	private ActionForward getBeforeForward(HttpServletRequest request, ActionMapping mapping) throws Exception{
		int step = 0;
		try{
			step = Integer.parseInt(request.getParameter("step"));
		}catch (NumberFormatException e){
		}
		
		if (step < 0 && step >= forwards.length)
			throw new IllegalArgumentException("Illegal step!");
		
		if (step != 0) step -=1; 
		
		return mapping.findForward(forwards[step]);
	}

	/**
	 * @param form
	 * @return
	 */
	private ActionForward getNextForward(HttpServletRequest request, ActionMapping mapping) throws Exception {
		int step = 0;
		try{
			step = Integer.parseInt(request.getParameter("step"));
		}catch (NumberFormatException e){
		}
		
		step = step < 0 ? 0 : step;
		step = step > forwards.length ? forwards.length - 2 : step;
		return mapping.findForward(forwards[step + 1]);
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
			processEnrolment(enrolmentForm, session);

		Object args[] = { infoEnrolmentContext };

		ServiceUtils.executeService(userView, "ValidateActualEnrolment", args);

		return getNextForward(request, mapping);
	}

	public ActionForward accept(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		if (isCancelled(request)){
			return getBeforeForward(request, mapping);
		}
		validateToken(request, form, mapping);
		
		return getNextForward(request, mapping);
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
			throw new FenixTransactionException(
				"error.transaction.enrolment");
		} else {
			createToken (request);
		}
	}

	/**
	 * @param request
	 */
	private void createToken(HttpServletRequest request) {
		generateToken(request);
		saveToken(request);
	}

	/**
	 * @param enrolmentForm
	 * @param session
	 * @return
	 */
	private InfoEnrolmentContext processEnrolment(
		DynaActionForm enrolmentForm,
		HttpSession session) {

		InfoEnrolmentContext infoEnrolmentContext =
			(InfoEnrolmentContext) session.getAttribute(
				SessionConstants.INFO_ENROLMENT_CONTEXT_KEY);
		Integer[] curricularCourses =
			(Integer[]) enrolmentForm.get("curricularCourses");

		List curricularCourseScopesToBeEnrolled =
			infoEnrolmentContext
				.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled();
		List actualEnrolment = infoEnrolmentContext.getActualEnrolment();
		for (int i = 0; i < curricularCourses.length; i++) {
			Integer curricularCourseIndex = curricularCourses[i];
			InfoCurricularCourseScope curricularCourseScope =
				(
					InfoCurricularCourseScope) curricularCourseScopesToBeEnrolled
						.get(
					curricularCourseIndex.intValue());

			if (!actualEnrolment.contains(curricularCourseScope)) {
				actualEnrolment.add(curricularCourseScope);
			}
		}
		return infoEnrolmentContext;
	}
}
