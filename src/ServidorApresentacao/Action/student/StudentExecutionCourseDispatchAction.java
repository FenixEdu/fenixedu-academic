/*
 * Created on 28/Ago/2003
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

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Susana Fernandes
 */
public class StudentExecutionCourseDispatchAction extends FenixDispatchAction {

	public ActionForward viewStudentExecutionCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		List studentExecutionCoursesList = null;
		try {
			studentExecutionCoursesList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadExecutionCoursesByStudent",
					new Object[] { userView.getUtilizador()});
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute(
			"studentExecutionCoursesList",
			studentExecutionCoursesList);
		return mapping.findForward("viewStudentExecutionCourses");
	}

	public ActionForward executionCoursePage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			
		String objectCode =	request.getParameter("objectCode");
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("studentExecutionCoursePage");
	}
}
