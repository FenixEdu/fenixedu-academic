/*
 * Created on Oct 4, 2003
 */
package ServidorApresentacao.Action.sop.utils;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import DataBeans.InfoViewExam;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;

/**
 * @author Luis Cruz
 *          
 */
public class RequestContextUtil {

	/**
	 * @param request
	 * @param examDateAndTime
	 */
	public static void setExamDateAndTimeContext(HttpServletRequest request) {
		Calendar examDateAndTime = Calendar.getInstance();
		Long dateAndTime = null;
		try {
			dateAndTime =
				new Long(
					request.getParameter(SessionConstants.EXAM_DATEANDTIME));
		} catch (NumberFormatException ex) {
			examDateAndTime = (Calendar) request.getAttribute(SessionConstants.EXAM_DATEANDTIME);
			if (examDateAndTime != null) {
				System.out.println("Restting EXAM_DATEANDTIME= " + examDateAndTime);	
				request.setAttribute(SessionConstants.EXAM_DATEANDTIME, examDateAndTime);
			} else {
				request.removeAttribute(SessionConstants.EXAM_DATEANDTIME);
				request.setAttribute(SessionConstants.EXAM_DATEANDTIME, null);
				System.out.println("Removing EXAM_DATEANDTIME= " + examDateAndTime);
			}
		}

		if (dateAndTime != null) {
			examDateAndTime.setTimeInMillis(dateAndTime.longValue());
			System.out.println("Setting EXAM_DATEANDTIME= " + examDateAndTime);
			request.setAttribute(
				SessionConstants.EXAM_DATEANDTIME,
				examDateAndTime);
		}
	}

	/**
	 * @param request
	 * @param examDateAndTime
	 */
	public static void setExamDateAndTimeContext(
		HttpServletRequest request,
		Calendar examDateAndTime) {
		request.setAttribute(
			SessionConstants.EXAM_DATEANDTIME,
			examDateAndTime);
	}

	/**
	 * @param request
	 * @return
	 */
	public static Calendar getExamDateAndTimeContext(HttpServletRequest request) {
		return (Calendar) request.getAttribute(
			SessionConstants.EXAM_DATEANDTIME);
	}

	/**
	 * @param examDateAndTime
	 * @return
	 */
	public static InfoViewExam getInfoViewExams(
		IUserView userView,
		Calendar examDateAndTime)
		throws FenixServiceException {
		Calendar examDate = Calendar.getInstance();
		Calendar examTime = Calendar.getInstance();

		examDate.set(
			Calendar.DAY_OF_MONTH,
			examDateAndTime.get(Calendar.DAY_OF_MONTH));
		examDate.set(Calendar.MONTH, examDateAndTime.get(Calendar.MONTH));
		examDate.set(Calendar.YEAR, examDateAndTime.get(Calendar.YEAR));
		examDate.set(Calendar.HOUR_OF_DAY, 0);
		examDate.set(Calendar.MINUTE, 0);
		examDate.set(Calendar.SECOND, 0);

		examTime.set(Calendar.DAY_OF_MONTH, 1);
		examTime.set(Calendar.MONTH, 1);
		examTime.set(Calendar.YEAR, 1970);
		examTime.set(
			Calendar.HOUR_OF_DAY,
			examDateAndTime.get(Calendar.HOUR_OF_DAY));
		examTime.set(Calendar.MINUTE, 0);
		examTime.set(Calendar.SECOND, 0);

		Object args[] = { examDate, examTime };
		InfoViewExam infoViewExams =
			(InfoViewExam) ServiceUtils.executeService(
				userView,
				"ReadExamsByDayAndBeginning",
				args);

		return infoViewExams;
	}

}
