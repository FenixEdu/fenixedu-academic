/*
 * Created on 28/Mai/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExamEnrollment;
import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
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

		HttpSession session = request.getSession(false);
		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal = new Integer( request.getParameter("examCode"));
		Integer disciplinaExecucaoIdInternal =
			(Integer) request.getAttribute("objectCode");
		
		Object args[] = { disciplinaExecucaoIdInternal, examIdInternal };

		SiteView siteView= null;
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
		System.out.println("objectCode->"+ disciplinaExecucaoIdInternal);
		request.setAttribute("objectCode", disciplinaExecucaoIdInternal);
		
		request.setAttribute("siteView",siteView);
		return mapping.findForward("prepareEditExamEnrollment");

	}

	public ActionForward insertExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm examEnrollmentForm = (DynaActionForm) form;

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal =
			(Integer) request.getAttribute("examIdInternal");
		Integer disciplinaExecucaoIdInternal =
			(Integer) request.getAttribute("objectCode");

		Integer examEnrollment =
			(Integer) examEnrollmentForm.get("examEnrollmentIdInternal");

		Integer beginDay = (Integer) examEnrollmentForm.get("beginDay");
		Integer beginMonth = (Integer) examEnrollmentForm.get("beginMonth");
		Integer beginYear = (Integer) examEnrollmentForm.get("beginYear");
		Integer beginHour = (Integer) examEnrollmentForm.get("beginHour");
		Integer beginMinutes = (Integer) examEnrollmentForm.get("beginMinutes");

		Integer endDay = (Integer) examEnrollmentForm.get("endDay");
		Integer endMonth = (Integer) examEnrollmentForm.get("endMonth");
		Integer endYear = (Integer) examEnrollmentForm.get("endYear");
		Integer endHour = (Integer) examEnrollmentForm.get("endHour");
		Integer endMinutes = (Integer) examEnrollmentForm.get("endMinutes");

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(
			beginYear.intValue(),
			beginMonth.intValue(),
			beginDay.intValue(),
			beginHour.intValue(),
			beginMinutes.intValue());

		Calendar endDate = Calendar.getInstance();
		endDate.set(
			endYear.intValue(),
			endMonth.intValue(),
			endDay.intValue(),
			endHour.intValue(),
			endMinutes.intValue());

		Object args[] =
			{
				disciplinaExecucaoIdInternal,
				examIdInternal,
				beginDate,
				endDate };

		InfoExamEnrollment infoExamEnrollment =
			(InfoExamEnrollment) ServiceUtils.executeService(
				userView,
				"InsertExamEnrollment",
				args);

		request.setAttribute("examIdInternal", examIdInternal);
		request.setAttribute("objectCode", disciplinaExecucaoIdInternal);

		return mapping.findForward("sucess");
	}

	public ActionForward editExamEnrollment(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm examEnrollmentForm = (DynaActionForm) form;

		HttpSession session = request.getSession();
		IUserView userView = SessionUtils.getUserView(request);

		Integer examIdInternal =
			(Integer) request.getAttribute("examCode");
		Integer disciplinaExecucaoIdInternal =
			(Integer) request.getAttribute("objectCode");

		Integer examEnrollment =
			(Integer) request.getAttribute("examEnrollmentCode");

		Integer beginDay = (Integer) examEnrollmentForm.get("beginDay");
		Integer beginMonth = (Integer) examEnrollmentForm.get("beginMonth");
		Integer beginYear = (Integer) examEnrollmentForm.get("beginYear");
		Integer beginHour = (Integer) examEnrollmentForm.get("beginHour");
		Integer beginMinutes = (Integer) examEnrollmentForm.get("beginMinutes");

		Integer endDay = (Integer) examEnrollmentForm.get("endDay");
		Integer endMonth = (Integer) examEnrollmentForm.get("endMonth");
		Integer endYear = (Integer) examEnrollmentForm.get("endYear");
		Integer endHour = (Integer) examEnrollmentForm.get("endHour");
		Integer endMinutes = (Integer) examEnrollmentForm.get("endMinutes");

		Calendar beginDate = Calendar.getInstance();
		beginDate.set(
			beginYear.intValue(),
			beginMonth.intValue(),
			beginDay.intValue(),
			beginHour.intValue(),
			beginMinutes.intValue());

		Calendar endDate = Calendar.getInstance();
		endDate.set(
			endYear.intValue(),
			endMonth.intValue(),
			endDay.intValue(),
			endHour.intValue(),
			endMinutes.intValue());

		Object args[] =
			{
				disciplinaExecucaoIdInternal,
				examIdInternal,
				beginDate,
				endDate };

		InfoExamEnrollment infoExamEnrollment =
			(InfoExamEnrollment) ServiceUtils.executeService(
				userView,
				"EditExamEnrollment",
				args);

		request.setAttribute("examIdInternal", examIdInternal);
		request.setAttribute("objectCode", disciplinaExecucaoIdInternal);

		return mapping.findForward("sucess");
	}

}
