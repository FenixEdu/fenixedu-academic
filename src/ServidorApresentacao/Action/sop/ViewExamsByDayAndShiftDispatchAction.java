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

		Calendar examDate = Calendar.getInstance();
		Calendar examTime = Calendar.getInstance();
		
		examDate.set(Calendar.DAY_OF_MONTH, examDateAndTime.get(Calendar.DAY_OF_MONTH));
		examDate.set(Calendar.MONTH, examDateAndTime.get(Calendar.MONTH));
		examDate.set(Calendar.YEAR, examDateAndTime.get(Calendar.YEAR));
		examDate.set(Calendar.HOUR_OF_DAY, 0);
		examDate.set(Calendar.MINUTE, 0);
		examDate.set(Calendar.SECOND, 0);

		examTime.set(Calendar.DAY_OF_MONTH, 1);
		examTime.set(Calendar.MONTH, 1);
		examTime.set(Calendar.YEAR, 1970);		
		examTime.set(Calendar.HOUR_OF_DAY, examDateAndTime.get(Calendar.HOUR_OF_DAY));
		examTime.set(Calendar.MINUTE, 0);
		examTime.set(Calendar.SECOND, 0);

		Object args[] = { examDate, examTime };
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

		Object args[] = { (InfoViewExamByDayAndShift) infoExams.get(indexExam.intValue()) };
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
		
		DynaValidatorForm editExamForm = (DynaValidatorForm) form;
	
		List infoExams = (List) session.getAttribute(SessionConstants.LIST_EXAMSANDINFO);

		Integer indexExam = new Integer(request.getParameter("indexExam"));

		InfoViewExamByDayAndShift infoViewExam = (InfoViewExamByDayAndShift) infoExams.get(indexExam.intValue());

		ArrayList horas = Util.getExamShifts();
		session.setAttribute(SessionConstants.LABLELIST_HOURS, horas);

		ArrayList daysOfMonth = Util.getDaysOfMonth();
		session.setAttribute(SessionConstants.LABLELIST_DAYSOFMONTH, daysOfMonth);

		ArrayList monthsOfYear = Util.getMonthsOfYear();
		session.setAttribute(SessionConstants.LABLELIST_MONTHSOFYEAR, monthsOfYear);

		ArrayList examSeasons = Util.getExamSeasons();
		session.setAttribute(SessionConstants.LABLELIST_SEASONS, examSeasons);

		Calendar date = Calendar.getInstance();
		date = infoViewExam.getInfoExam().getDay();

		editExamForm.set("day", new Integer(date.get(Calendar.DAY_OF_MONTH)).toString());
		editExamForm.set("month", new Integer(date.get(Calendar.MONTH)).toString());
		editExamForm.set("year", new Integer(date.get(Calendar.YEAR)).toString());
		if (infoViewExam.getInfoExam().getBeginning() != null) {
			editExamForm.set("beginning", new Integer(infoViewExam.getInfoExam().getBeginning().get(Calendar.HOUR_OF_DAY)).toString());
		}
		editExamForm.set("season", infoViewExam.getInfoExam().getSeason().getseason().toString());

		session.setAttribute("input", "viewExamsByDayAndShift");
		
		session.setAttribute(SessionConstants.INFO_EXAMS_KEY, infoViewExam);

		return mapping.findForward("Edit Exam");
	}

	public ActionForward addExecutionCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
	
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