package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

/**
 * @author Pedro Santos e Rita Carvalho 22/Out/2004
 *  
 */
public class ViewStudentScheduleAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String studentUserName = request.getParameter("userName");
		
		Object[] args = {studentUserName};
		List infoLessons;
		
		infoLessons =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentTimeTable",
					args);
		
		request.setAttribute("infoLessons", infoLessons);

		return mapping.findForward("sucess");

	}

}