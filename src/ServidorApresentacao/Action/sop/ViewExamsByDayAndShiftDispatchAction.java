/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
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
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoExam;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ViewExamsByDayAndShiftDispatchAction extends DispatchAction {

	public ActionForward view(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		Calendar examDateAndTime =
			(Calendar) session.getAttribute(SessionConstants.EXAM_DATEANDTIME);

		Object args[] = { examDateAndTime.getTime(), examDateAndTime };
		InfoViewExam infoViewExams = 
			(InfoViewExam) ServiceUtils.executeService(
				userView,
				"ReadExamsByDayAndBeginning",
				args);

		List infoExams = infoViewExams.getInfoViewExamsByDayAndShift();

		if (infoExams != null && infoExams.size() == 0)
			infoExams = null;

		session.setAttribute(SessionConstants.AVAILABLE_ROOM_OCCUPATION, infoViewExams.getAvailableRoomOccupation());
		session.removeAttribute(SessionConstants.LIST_EXAMSANDINFO);
		session.setAttribute(SessionConstants.LIST_EXAMSANDINFO, infoExams);

		return mapping.findForward("View Exams");
	}

	public ActionForward delete(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
	
		List infoExams = (List) session.getAttribute(SessionConstants.LIST_EXAMSANDINFO);

		Integer indexExam = new Integer(request.getParameter("indexExam"));

		Object args[] = { ((InfoViewExamByDayAndShift) infoExams.get(indexExam.intValue())).getInfoExam() };
		ServiceUtils.executeService(userView, "DeleteExam",	args);
		
		return mapping.findForward("Deleted Exam");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaValidatorForm editExamForm = (DynaValidatorForm) form;
	
		List infoExams = (List) session.getAttribute(SessionConstants.LIST_EXAMSANDINFO);

		Integer indexExam = new Integer(request.getParameter("indexExam"));

		InfoExam infoExam = ((InfoViewExamByDayAndShift) infoExams.get(indexExam.intValue())).getInfoExam();

		ArrayList horas = Util.getExamShifts();
		session.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

		ArrayList daysOfMonth = Util.getDaysOfMonth();
		session.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

		ArrayList monthsOfYear = Util.getMonthsOfYear();
		session.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

		ArrayList examSeasons = Util.getExamSeasons();
		session.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

		Calendar date = Calendar.getInstance();
		date.setTime(infoExam.getDay());

		editExamForm.set("day", new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
		editExamForm.set("month", new Integer(date.get(Calendar.MONTH)).toString());
		editExamForm.set("year", new Integer(date.get(Calendar.YEAR)).toString());
		if (infoExam.getBeginning() != null) {
			editExamForm.set("beginning", new Integer(infoExam.getBeginning().get(Calendar.HOUR_OF_DAY)).toString());
		}
		editExamForm.set("season", infoExam.getSeason().getseason().toString());

		session.setAttribute(SessionConstants.EXECUTION_COURSE_KEY, infoExam.getInfoExecutionCourse());

		session.setAttribute("input", "viewExamsByDayAndShift");
		
		session.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoExam);

		return mapping.findForward("Edit Exam");
	}

	public ActionForward addExecutionCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);
	
		List infoViewExams = (List) session.getAttribute(SessionConstants.LIST_EXAMSANDINFO);

		Integer indexExam = new Integer(request.getParameter("indexExam"));

		session.setAttribute(
			SessionConstants.INFO_VIEW_EXAM,
			((InfoViewExamByDayAndShift) infoViewExams
				.get(indexExam.intValue())));

		session.setAttribute("input", "viewExamsByDayAndShift");
		
		return mapping.findForward("Add Execution Course");
	}

}