/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author jpvl
 */
public class CurricularCourseEnrolmentManagerDispatchAction
	extends DispatchAction {
	public ActionForward start(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
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

		return mapping.findForward("showAvailableCurricularCourses");
	}
}
