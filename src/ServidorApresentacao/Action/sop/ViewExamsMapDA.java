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
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
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
		throws FenixActionException {
		HttpSession session = request.getSession(false);

		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			List curricularYears =
				(List) request.getAttribute(
					SessionConstants.CURRICULAR_YEARS_LIST);

			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) request.getAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD_KEY);
			InfoExecutionDegree infoExecutionDegree =
				(InfoExecutionDegree) request.getAttribute(
					SessionConstants.INFO_EXECUTION_DEGREE_KEY);

			Object[] args =
				{ infoExecutionDegree, curricularYears, infoExecutionPeriod };
			InfoExamsMap infoExamsMap;
			try {
				infoExamsMap =
					(InfoExamsMap) gestor.executar(
						userView,
						"ReadExamsMap",
						args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException(e);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			request.setAttribute(SessionConstants.INFO_EXAMS_MAP, infoExamsMap);
		} else {
			throw new FenixActionException();
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

		InfoExamsMap infoExamsMap =
			(InfoExamsMap) request.getAttribute(
				SessionConstants.INFO_EXAMS_MAP);

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

		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);

		String executionCourseInitials =
			request.getParameter("executionCourseInitials");
		Season season = new Season(new Integer(request.getParameter("season")));

		Object args[] =
			{ executionCourseInitials, season, infoExecutionPeriod };
		InfoViewExamByDayAndShift infoViewExamByDayAndShift =
			(InfoViewExamByDayAndShift) ServiceUtils.executeService(
				userView,
				"ReadExamsByExecutionCourseInitialsAndSeasonAndExecutionPeriod",
				args);

		ArrayList horas = Util.getExamShifts();
		request.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

		ArrayList daysOfMonth = Util.getDaysOfMonth();
		request.setAttribute(
			SessionConstants.LABLELIST_DAYSOFMONTH,
			daysOfMonth);

		ArrayList monthsOfYear = Util.getMonthsOfYear();
		request.setAttribute(
			SessionConstants.LABLELIST_MONTHSOFYEAR,
			monthsOfYear);

		ArrayList examSeasons = Util.getExamSeasons();
		request.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

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
			infoViewExamByDayAndShift
				.getInfoExam()
				.getSeason()
				.getseason()
				.toString());

		request.setAttribute(
			SessionConstants.INFO_EXAMS_KEY,
			infoViewExamByDayAndShift);

		request.setAttribute("input", "viewExamsMap");

		return mapping.findForward("editExam");
	}

	public ActionForward comment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		InfoExamsMap infoExamsMap =
			(InfoExamsMap) request.getAttribute(
				SessionConstants.INFO_EXAMS_MAP);

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

		return mapping.findForward("comment");
	}

}
