/*
 * Created on 28/Mai/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidTimeIntervalServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Nunes
 *
 */
public class ExamEnrollmentDispatchAction extends FenixDispatchAction {

	public ActionForward editExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		DynaActionForm examEnrollmentForm = (DynaActionForm) form;

		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal = new Integer(request.getParameter("examCode"));
		Integer disciplinaExecucaoIdInternal =
			new Integer(request.getParameter("objectCode"));

		String enrollmentBeginDay =
			(String) examEnrollmentForm.get("enrollmentBeginDayFormatted");
		String enrollmentBeginHour =
			(String) examEnrollmentForm.get("enrollmentBeginTimeFormatted");
		String enrollmentEndDay =
			(String) examEnrollmentForm.get("enrollmentEndDayFormatted");
		String enrollmentEndHour =
			(String) examEnrollmentForm.get("enrollmentEndTimeFormatted");

		String[] enrollmentBeginDayArray = enrollmentBeginDay.split("/");
		String[] enrollmentBeginHourArray = enrollmentBeginHour.split(":");
		String[] enrollmentEndDayArray = enrollmentEndDay.split("/");
		String[] enrollmentEndHourArray = enrollmentEndHour.split(":");
		Calendar beginDate = Calendar.getInstance();
		beginDate.set(
			Calendar.YEAR,
			new Integer(enrollmentBeginDayArray[2]).intValue());
		beginDate.set(
			Calendar.MONTH,
			new Integer(enrollmentBeginDayArray[1]).intValue()-1);
		beginDate.set(
			Calendar.DAY_OF_MONTH,
			new Integer(enrollmentBeginDayArray[0]).intValue());

		Calendar beginTime = Calendar.getInstance();
		beginTime.set(
			Calendar.HOUR_OF_DAY,
			new Integer(enrollmentBeginHourArray[0]).intValue());
		beginTime.set(
			Calendar.MINUTE,
			new Integer(enrollmentBeginHourArray[1]).intValue());

		Calendar endDate = Calendar.getInstance();

		endDate.set(
			Calendar.YEAR,
			new Integer(enrollmentEndDayArray[2]).intValue());
		endDate.set(
			Calendar.MONTH,
			new Integer(enrollmentEndDayArray[1]).intValue()-1);
		endDate.set(
			Calendar.DAY_OF_MONTH,
			new Integer(enrollmentEndDayArray[0]).intValue());

		Calendar endTime = Calendar.getInstance();
		endTime.set(
			Calendar.HOUR_OF_DAY,
			new Integer(enrollmentEndHourArray[0]).intValue());
		endTime.set(
			Calendar.MINUTE,
			new Integer(enrollmentEndHourArray[1]).intValue());

		if (!verifyDates(beginDate, beginTime, endDate, endTime)) {
			setErrorMessage(request, "error.endDate.sooner.beginDate");
			return mapping.getInputForward();
		}

		Object args[] =
			{
				disciplinaExecucaoIdInternal,
				examIdInternal,
				beginDate,
				endDate,
				beginTime,
				endTime };

		try {
			ServiceUtils.executeService(userView, "EditExamEnrollment", args);
		} catch (InvalidTimeIntervalServiceException e) {
			setErrorMessage(request, "error.endDate.sooner.examDate");
			return mapping.getInputForward();
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("examCode", examIdInternal);
		request.setAttribute("objectCode", disciplinaExecucaoIdInternal);

		return mapping.findForward("viewExams");
	}

	private boolean verifyDates(
		Calendar beginDay,
		Calendar beginTime,
		Calendar endDay,
		Calendar endTime) {
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.YEAR, beginDay.get(Calendar.YEAR));
		begin.set(Calendar.MONTH, beginDay.get(Calendar.MONTH));
		begin.set(Calendar.DAY_OF_MONTH, beginDay.get(Calendar.DAY_OF_MONTH));
		begin.set(Calendar.HOUR_OF_DAY, beginTime.get(Calendar.HOUR_OF_DAY));
		begin.set(Calendar.MINUTE, beginTime.get(Calendar.MINUTE));

		Calendar end = Calendar.getInstance();
		end.set(Calendar.YEAR, endDay.get(Calendar.YEAR));
		end.set(Calendar.MONTH, endDay.get(Calendar.MONTH));
		end.set(Calendar.DAY_OF_MONTH, endDay.get(Calendar.DAY_OF_MONTH));
		end.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		end.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));

		return  begin.getTimeInMillis() < end.getTimeInMillis();
	}

	private void setErrorMessage(HttpServletRequest request, String message) {
		ActionErrors actionErrors = new ActionErrors();
		ActionError actionError = new ActionError(message);
		actionErrors.add(message, actionError);
		saveErrors(request, actionErrors);
	}
}
