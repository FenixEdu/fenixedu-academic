package ServidorApresentacao.Action.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoShiftEnrolment;
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
/**
 * @author tfc130
 *
 */
public class ChooseOtherCourseAction extends Action {

	public ActionForward execute(ActionMapping mapping,
	                             ActionForm form,
	                             HttpServletRequest request,
	                             HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		HttpSession session = request.getSession(false);
		
		if (session != null) {
		
			InfoStudent infoStudent = (InfoStudent) session.getAttribute("infoStudent");

			Object argsReadOtherCoursesWithShifts[] = { infoStudent };

			List otherCourses =
					(List) ServiceUtils.executeService(
						userView,
						"ReadOtherCoursesWithShifts",
						argsReadOtherCoursesWithShifts);

			InfoShiftEnrolment iSE = new InfoShiftEnrolment();
			iSE.setInfoEnrolmentWithShift(otherCourses);

			// Place results in session
			session.removeAttribute("infoShiftEnrolment");
			session.removeAttribute("index");
			if (otherCourses != null && !otherCourses.isEmpty()) {
				session.setAttribute("infoShiftEnrolment", iSE);
			}

		}

		return mapping.findForward("sucess");

	}

}