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

import DataBeans.SiteView;
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

	public ActionForward prepareEditExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

	
		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal = new Integer(request.getParameter("examCode"));
		Integer disciplinaExecucaoIdInternal =
			new Integer(request.getParameter("objectCode"));

		Object args[] = { disciplinaExecucaoIdInternal, examIdInternal };

		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadExamEnrollment",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("examCode", examIdInternal);
		System.out.println("objectCode->" + disciplinaExecucaoIdInternal);
		request.setAttribute("objectCode", disciplinaExecucaoIdInternal);

		request.setAttribute("siteView", siteView);
		return mapping.findForward("prepareEditExamEnrollment");

	}

	public ActionForward insertExamEnrollment(
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

		Integer beginDay =
			new Integer((String) examEnrollmentForm.get("beginDay"));
		Integer beginMonth =
			new Integer((String) examEnrollmentForm.get("beginMonth"));
		Integer beginYear =
			new Integer((String) examEnrollmentForm.get("beginYear"));
		Integer beginHour =
			new Integer((String) examEnrollmentForm.get("beginHour"));
		Integer beginMinutes =
			new Integer((String) examEnrollmentForm.get("beginMinutes"));

		Integer endDay = new Integer((String) examEnrollmentForm.get("endDay"));
		Integer endMonth =
			new Integer((String) examEnrollmentForm.get("endMonth"));
		Integer endYear =
			new Integer((String) examEnrollmentForm.get("endYear"));
		Integer endHour =
			new Integer((String) examEnrollmentForm.get("endHour"));
		Integer endMinutes =
			new Integer((String) examEnrollmentForm.get("endMinutes"));

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(
			beginYear.intValue(),
			beginMonth.intValue() - 1,
			beginDay.intValue(),
			beginHour.intValue(),
			beginMinutes.intValue());

		Calendar endDate = Calendar.getInstance();
		endDate.set(
			endYear.intValue(),
			endMonth.intValue() - 1,
			endDay.intValue(),
			endHour.intValue(),
			endMinutes.intValue());

		if (!verifyDates(beginDate, endDate)) {
			setErrorMessage(request, "error.endDate.sooner.beginDate");
			return mapping.getInputForward();
		}

		Object args[] =
			{
				disciplinaExecucaoIdInternal,
				examIdInternal,
				beginDate,
				endDate };

		try {
			ServiceUtils.executeService(userView, "InsertExamEnrollment", args);
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

		

		Integer beginDay =
			new Integer((String) examEnrollmentForm.get("beginDay"));
		Integer beginMonth =
			new Integer((String) examEnrollmentForm.get("beginMonth"));
		Integer beginYear =
			new Integer((String) examEnrollmentForm.get("beginYear"));
		Integer beginHour =
			new Integer((String) examEnrollmentForm.get("beginHour"));
		Integer beginMinutes =
			new Integer((String) examEnrollmentForm.get("beginMinutes"));

		Integer endDay = new Integer((String) examEnrollmentForm.get("endDay"));
		Integer endMonth =
			new Integer((String) examEnrollmentForm.get("endMonth"));
		Integer endYear =
			new Integer((String) examEnrollmentForm.get("endYear"));
		Integer endHour =
			new Integer((String) examEnrollmentForm.get("endHour"));
		Integer endMinutes =
			new Integer((String) examEnrollmentForm.get("endMinutes"));

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(
			beginYear.intValue(),
			beginMonth.intValue() - 1,
			beginDay.intValue(),
			beginHour.intValue(),
			beginMinutes.intValue());

		Calendar endDate = Calendar.getInstance();
		endDate.set(
			endYear.intValue(),
			endMonth.intValue() - 1,
			endDay.intValue(),
			endHour.intValue(),
			endMinutes.intValue());

		if (!verifyDates(beginDate, endDate)) {
			setErrorMessage(request, "error.endDate.sooner.beginDate");
			return mapping.getInputForward();
		}

		Object args[] =
			{
				disciplinaExecucaoIdInternal,
				examIdInternal,
				beginDate,
				endDate };

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

	private boolean verifyDates(Calendar begin, Calendar end) {

		return begin.getTimeInMillis() < end.getTimeInMillis();
	}

	private void setErrorMessage(HttpServletRequest request, String message) {
		ActionErrors actionErrors = new ActionErrors();
		ActionError actionError = new ActionError(message);
		actionErrors.add(message, actionError);
		saveErrors(request, actionErrors);
	}
}
