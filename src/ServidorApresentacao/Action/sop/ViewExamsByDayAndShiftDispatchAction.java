/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2003/03/21
 *
 */
package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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

	// -------------------------------------------------------------------------
	//    FIXME : Instead should forward to view... for now just repeat the code.
	// -------------------------------------------------------------------------

//		Calendar examDateAndTime =
//			(Calendar) session.getAttribute(SessionConstants.EXAM_DATEANDTIME);
//
//		Object args2[] = { examDateAndTime.getTime(), examDateAndTime };
//		InfoViewExam infoViewExams = 
//			(InfoViewExam) ServiceUtils.executeService(
//				userView,
//				"ReadExamsByDayAndBeginning",
//				args2);
//
//		infoExams = infoViewExams.getInfoViewExamsByDayAndShift();
//
//		if (infoExams != null && infoExams.size() == 0)
//			infoExams = null;
//
//		session.setAttribute(SessionConstants.AVAILABLE_ROOM_OCCUPATION, infoViewExams.getAvailableRoomOccupation());
//		session.removeAttribute(SessionConstants.LIST_EXAMSANDINFO);
//		session.setAttribute(SessionConstants.LIST_EXAMSANDINFO, infoExams);
//
//		return mapping.findForward("View Exams");
		
	}

}