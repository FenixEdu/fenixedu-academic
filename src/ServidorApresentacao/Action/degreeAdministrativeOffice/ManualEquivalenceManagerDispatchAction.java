package ServidorApresentacao.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.degreeAdministrativeOffice.InfoEquivalenceContext;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ManualEquivalenceManagerDispatchAction extends DispatchAction {
	
	private final String[] forwards = { "showCurricularCoursesForEquivalence", "begin", "home" };

	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		IUserView actor = (IUserView) session.getAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
//		InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) session.getServletContext().getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		Object args[] = { actor };

		InfoEquivalenceContext infoEquivalenceContext = (InfoEquivalenceContext) ServiceUtils.executeService(userView, "GetListsOfCurricularCoursesForEquivalence", args);

//		session.removeAttribute(SessionConstants.ENROLMENT_ACTOR_KEY);
		request.setAttribute(SessionConstants.EQUIVALENCE_CONTEXT_KEY, infoEquivalenceContext);
		this.initializeForm(infoEquivalenceContext, (DynaActionForm) form);
		return mapping.findForward(forwards[0]);
	}

	public ActionForward verify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
		return mapping.findForward(forwards[1]);
	}

	public ActionForward accept(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List degreeTypeList = TipoCurso.toLabelValueBeanList();
		request.setAttribute(SessionConstants.DEGREE_TYPE, degreeTypeList);
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

}