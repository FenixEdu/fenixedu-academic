package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class AddExecutionCourseToExamAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		InfoExecutionCourse executionCourse =
			(InfoExecutionCourse) request.getAttribute(
				SessionConstants.EXECUTION_COURSE_KEY);

		InfoViewExamByDayAndShift infoViewExams =
			(InfoViewExamByDayAndShift) request.getAttribute(
				SessionConstants.INFO_VIEW_EXAM);

		// Create new association between exam and executionCourse
		Object argsCreateExam[] = { infoViewExams, executionCourse };
		try {
			ServiceUtils.executeService(
				userView,
				"AssociateExecutionCourseToExam",
				argsCreateExam);
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException(
				"Para a disciplina escolhida, o exame de "
					+ infoViewExams.getInfoExam().getSeason(),
				ex);
		}

		return mapping.findForward("Sucess");
	}

}
