package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.base.FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class DefineExamCommentActionDA
	extends FenixCurricularYearsAndExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		////////////
		//InfoExamsMap infoExamsMap =
		//	(InfoExamsMap) request.getAttribute(
		//		SessionConstants.INFO_EXAMS_MAP);
		InfoExamsMap infoExamsMap = getExamsMap(request);
		request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
		System.out.println("infoExamsMap=" + infoExamsMap);

		Integer indexExecutionCourse =
			new Integer(request.getParameter("indexExecutionCourse"));

		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) infoExamsMap.getExecutionCourses().get(
				indexExecutionCourse.intValue());

		Integer curricularYear = infoExecutionCourse.getCurricularYear();

		request.setAttribute(
			SessionConstants.CURRICULAR_YEAR_KEY,
			curricularYear);

		request.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			infoExecutionCourse);

		///////////
		request.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoExamsMap);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);
		request.setAttribute(
			SessionConstants.EXECUTION_DEGREE_OID,
			infoExecutionDegree.getIdInternal().toString());

		request.setAttribute(
			SessionConstants.EXECUTION_PERIOD_OID,
			infoExecutionCourse
				.getInfoExecutionPeriod()
				.getIdInternal()
				.toString());

		request.setAttribute(
			SessionConstants.EXECUTION_COURSE,
			infoExecutionCourse);
		request.setAttribute(
			SessionConstants.EXECUTION_COURSE_OID,
			infoExecutionCourse.getIdInternal().toString());

		request.setAttribute(
			SessionConstants.CURRICULAR_YEAR_OID,
			curricularYear.toString());
		ContextUtils.setCurricularYearContext(request);
		///////////

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
		//		String executionPeriodName =
		//			((InfoExecutionPeriod) request.getAttribute(
		//				SessionConstants.INFO_EXECUTION_PERIOD_KEY)).getName();
		//			//request.getParameter("executionPeriodName");
		//		String executionYear = request.getParameter("executionYear");
		//
		//		InfoExecutionYear infoExecutionYear =
		//			new InfoExecutionYear(executionYear);
		//		InfoExecutionPeriod infoExecutionPeriod =
		//			new InfoExecutionPeriod(executionPeriodName, infoExecutionYear);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
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

	private InfoExamsMap getExamsMap(HttpServletRequest request)
		throws FenixActionException {
		IUserView userView =
			(IUserView) request.getSession().getAttribute(
				SessionConstants.U_VIEW);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) request.getAttribute(
				SessionConstants.EXECUTION_DEGREE);

		List curricularYears =
			(List) request.getAttribute(SessionConstants.CURRICULAR_YEARS_LIST);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);

		Object[] args =
			{ infoExecutionDegree, curricularYears, infoExecutionPeriod };
		InfoExamsMap infoExamsMap;
		try {
			infoExamsMap =
				(InfoExamsMap) ServiceManagerServiceFactory.executeService(userView, "ReadExamsMap", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return infoExamsMap;
	}

}
