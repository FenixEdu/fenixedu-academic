/*
 * Created on 28/Mai/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Nunes
 *
 */
public class ShowExamsAction extends FenixAction {

	public ActionForward showExams(
		ActionMapping mapping,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);
		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse = infoSite.getInfoExecutionCourse();

		Object args[] = { infoExecutionCourse };

		List infoExamList =
			(List) ServiceUtils.executeService(userView, "ReadExams", args);

		request.setAttribute("infoExamList", infoExamList);
		return mapping.findForward("showExams");

	}
}
