package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import DataBeans.InfoExecutionPeriod;
import DataBeans.degreeAdministrativeOffice.InfoEquivalenceContext;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ManualEquivalenceManagerDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "showCurricularCoursesForEquivalence", "verifyCurricularCoursesForEquivalence", "acceptCurricularCoursesForEquivalence", "begin", "home" };

	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		IUserView actor = (IUserView) session.getAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		Object args[] = { actor, infoExecutionPeriod };

		InfoEquivalenceContext infoEquivalenceContext = (InfoEquivalenceContext) ServiceUtils.executeService(userView, "GetListsOfCurricularCoursesForEquivalence", args);

		session.removeAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);
		this.initializeForm(infoEquivalenceContext, (DynaActionForm) form);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward verify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (isCancelled(request)) {
			return mapping.findForward(forwards[3]);
		}

		DynaActionForm equivalenceForm = (DynaActionForm) form;
		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEquivalenceContext infoEquivalenceContext = this.processEquivalence(request, equivalenceForm, session);

		Object args[] = { infoEquivalenceContext };

		infoEquivalenceContext = (InfoEquivalenceContext) ServiceUtils.executeService(userView, "ValidateEquivalence", args);

		session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);

		if(infoEquivalenceContext.isSuccess()) {
			return mapping.findForward(forwards[1]);
		} else {
			this.saveErrorsFromInfoEquivalenceContext(request, infoEquivalenceContext);
			return mapping.findForward(forwards[0]);
		}
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (isCancelled(request)) {
			return mapping.findForward(forwards[0]);
		}

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoEquivalenceContext infoEquivalenceContext = (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

		Object args[] = { infoEquivalenceContext };

		infoEquivalenceContext = (InfoEquivalenceContext) ServiceUtils.executeService(userView, "ConfirmEquivalence", args);

		session.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);

		return mapping.findForward(forwards[2]);
	}

	private void initializeForm(InfoEquivalenceContext infoEquivalenceContext, DynaActionForm equivalenceForm) {

		List infoCurricularCourseScopesToGiveEquivalence = infoEquivalenceContext.getInfoCurricularCourseScopesToGiveEquivalence();
		List infoCurricularCourseScopesToGetEquivalence = infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence();

		Integer[] curricularCoursesToGiveEquivalence = new Integer[infoCurricularCourseScopesToGiveEquivalence.size()];
		Integer[] curricularCoursesToGetEquivalence = new Integer[infoCurricularCourseScopesToGetEquivalence.size()];

		for(int i = 0; i < infoCurricularCourseScopesToGiveEquivalence.size(); i++) {
			curricularCoursesToGiveEquivalence[i] = null;
		}

		for(int i = 0; i < infoCurricularCourseScopesToGetEquivalence.size(); i++) {
			curricularCoursesToGetEquivalence[i] = null;
		}

		equivalenceForm.set("curricularCoursesToGiveEquivalence", curricularCoursesToGiveEquivalence);
		equivalenceForm.set("curricularCoursesToGetEquivalence", curricularCoursesToGetEquivalence);
	}

	private InfoEquivalenceContext processEquivalence(HttpServletRequest request, DynaActionForm equivalenceForm, HttpSession session) {

		InfoEquivalenceContext infoEquivalenceContext = (InfoEquivalenceContext) session.getAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY);

		if(request.getParameter("curricularCoursesToGiveEquivalence") == null) {
			equivalenceForm.set("curricularCoursesToGiveEquivalence", new Integer[infoEquivalenceContext.getInfoCurricularCourseScopesToGiveEquivalence().size()]);
		}

		if(request.getParameter("curricularCoursesToGetEquivalence") == null) {
			equivalenceForm.set("curricularCoursesToGetEquivalence", new Integer[infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence().size()]);
		}

		Integer[] curricularCoursesToGiveEquivalence = (Integer[]) equivalenceForm.get("curricularCoursesToGiveEquivalence");
		Integer[] curricularCoursesToGetEquivalence = (Integer[]) equivalenceForm.get("curricularCoursesToGetEquivalence");

		List chosenInfoCurricularCourseScopesToGiveEquivalence = new ArrayList();
		List chosenInfoCurricularCourseScopesToGetEquivalence = new ArrayList();

		if(curricularCoursesToGiveEquivalence != null) {
			for(int i = 0; i < curricularCoursesToGiveEquivalence.length; i++) {
				Integer curricularCourseScopeIndex = curricularCoursesToGiveEquivalence[i];
				if(curricularCourseScopeIndex != null) {
					InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEquivalenceContext.getInfoCurricularCourseScopesToGiveEquivalence().get(curricularCourseScopeIndex.intValue());
					if(infoCurricularCourseScope != null) {
						chosenInfoCurricularCourseScopesToGiveEquivalence.add(infoCurricularCourseScope);
					}
				}
			}
		}

		if(curricularCoursesToGetEquivalence != null) {
			for(int i = 0; i < curricularCoursesToGetEquivalence.length; i++) {
				Integer curricularCourseScopeIndex = curricularCoursesToGetEquivalence[i];
				if(curricularCourseScopeIndex != null) {
					InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoEquivalenceContext.getInfoCurricularCourseScopesToGetEquivalence().get(curricularCourseScopeIndex.intValue());
					if(infoCurricularCourseScope != null) {
						chosenInfoCurricularCourseScopesToGetEquivalence.add(infoCurricularCourseScope);
					}
				}
			}
		}

		infoEquivalenceContext.setChosenInfoCurricularCourseScopesToGiveEquivalence(chosenInfoCurricularCourseScopesToGiveEquivalence);
		infoEquivalenceContext.setChosenInfoCurricularCourseScopesToGetEquivalence(chosenInfoCurricularCourseScopesToGetEquivalence);

		return infoEquivalenceContext;
	}

	private void saveErrorsFromInfoEquivalenceContext(HttpServletRequest request, InfoEquivalenceContext infoEquivalenceContext) {

		ActionErrors actionErrors = new ActionErrors();

		List errorMessages = infoEquivalenceContext.getErrorMessages();

		Iterator iterator = errorMessages.iterator();
		ActionError actionError = null;
		while (iterator.hasNext()) {
			String message = (String) iterator.next();
			actionError = new ActionError(message);
			actionErrors.add(message, actionError);
		}
		saveErrors(request, actionErrors);
	}

}