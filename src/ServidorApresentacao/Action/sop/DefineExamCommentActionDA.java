package ServidorApresentacao.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class DefineExamCommentActionDA extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		System.out.println("############################################");

		HttpSession session = request.getSession(false);

		IUserView userView = SessionUtils.getUserView(request);

		DynaValidatorForm defineExamCommentForm = (DynaValidatorForm) form;

		InfoExamsMap infoExamsMap =
			(InfoExamsMap) session.getAttribute(
				SessionConstants.INFO_EXAMS_MAP);

		Integer indexExecutionCourse =
			new Integer(request.getParameter("indexExecutionCourse"));

		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) infoExamsMap.getExecutionCourses().get(
				indexExecutionCourse.intValue());

		Integer curricularYear = infoExecutionCourse.getCurricularYear();

		session.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			curricularYear);

		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			infoExecutionCourse);

		return mapping.findForward("defineExamComment");
	}

	public ActionForward define(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		IUserView userView = SessionUtils.getUserView(request);

		DynaValidatorForm defineExamCommentForm = (DynaValidatorForm) form;

		String comment = (String) defineExamCommentForm.get("comment");
		String executionCourseCode =
			request.getParameter("executionCourseCode");
		String executionPeriodName =
			request.getParameter("executionPeriodName");
		String executionYear = request.getParameter("executionYear");

		InfoExecutionYear infoExecutionYear =
			new InfoExecutionYear(executionYear);
		InfoExecutionPeriod infoExecutionPeriod =
			new InfoExecutionPeriod(executionPeriodName, infoExecutionYear);
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		infoExecutionCourse.setSigla(executionCourseCode);
		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

		// Define comment
		Object argsDefineComment[] = { infoExecutionCourse, comment };
		try {
			ServiceUtils.executeService(
				userView,
				"DefineExamComment",
				argsDefineComment);
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException("O comentario do exame", ex);
		}

		return mapping.findForward("showExamsMap");
	}

}
