/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.Util;
import Util.Season;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsMapDA extends FenixDispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);

		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List curricularYears =
				(List) session.getAttribute(
					SessionConstants.CURRICULAR_YEARS_LIST);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) session.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) session.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY);

			Object[] args =
				{ infoExecutionDegree, curricularYears, infoExecutionPeriod };
			InfoExamsMap infoExamsMap =
				(InfoExamsMap) gestor.executar(userView, "ReadExamsMap", args);

			session.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
		} else {
			throw new Exception();
		}

		return mapping.findForward("viewExamsMap");
	}

	public ActionForward create(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		DynaValidatorForm editExamForm = (DynaValidatorForm) form;

		GestorServicos gestor = GestorServicos.manager();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);

		InfoExecutionDegree infoExecutionDegree =
			(InfoExecutionDegree) session.getAttribute(
				SessionConstants.INFO_EXECUTION_DEGREE_KEY);

		InfoExamsMap infoExamsMap =
			(InfoExamsMap) session.getAttribute(
				SessionConstants.INFO_EXAMS_MAP);

		Integer indexExecutionCourse =
			new Integer(request.getParameter("indexExecutionCourse"));

		InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) infoExamsMap
				.getExecutionCourses()
				.get(indexExecutionCourse.intValue());

		Integer curricularYear = infoExecutionCourse.getCurricularYear();

		session.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, curricularYear);

		session.setAttribute(
			SessionConstants.EXECUTION_COURSE_KEY,
			infoExecutionCourse);

		return mapping.findForward("createExam");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		DynaValidatorForm editExamForm = (DynaValidatorForm) form;

		GestorServicos gestor = GestorServicos.manager();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) session.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);

		String executionCourseInitials = request.getParameter("executionCourseInitials");
		Season season = new Season(new Integer(request.getParameter("season")));

		Object args[] = { executionCourseInitials, season, infoExecutionPeriod };
		InfoViewExamByDayAndShift infoViewExamByDayAndShift = 
			(InfoViewExamByDayAndShift) ServiceUtils.executeService(
				userView,
				"ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod",
				args);

		ArrayList horas = Util.getExamShifts();
		session.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

		ArrayList daysOfMonth = Util.getDaysOfMonth();
		session.setAttribute(
			SessionConstants.LABLELIST_DAYSOFMONTH,
			daysOfMonth);

		ArrayList monthsOfYear = Util.getMonthsOfYear();
		session.setAttribute(
			SessionConstants.LABLELIST_MONTHSOFYEAR,
			monthsOfYear);

		ArrayList examSeasons = Util.getExamSeasons();
		session.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

		Calendar date = Calendar.getInstance();
		date = infoViewExamByDayAndShift.getInfoExam().getDay();

		editExamForm.set(
			"day",
			new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
		editExamForm.set(
			"month",
			new Integer(date.get(Calendar.MONTH)).toString());
		editExamForm.set(
			"year",
			new Integer(date.get(Calendar.YEAR)).toString());
		if (infoViewExamByDayAndShift.getInfoExam().getBeginning() != null) {
			editExamForm.set(
				"beginning",
				new Integer(
			infoViewExamByDayAndShift.getInfoExam().getBeginning().get(
						Calendar.HOUR_OF_DAY))
					.toString());
		}
		editExamForm.set(
			"season",
			infoViewExamByDayAndShift.getInfoExam().getSeason().getseason().toString());

		session.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExamByDayAndShift);

		session.setAttribute("input", "viewExamsMap");

		return mapping.findForward("editExam");
	}

}
