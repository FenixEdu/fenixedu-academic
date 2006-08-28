/*
 * Created on 28/Mai/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Tânia Nunes
 *  
 */
public class ExamEnrollmentDispatchAction extends FenixDispatchAction {

    /**
     * @author Fernanda Quitério 15/07/2003
     *  
     */
    public ActionForward prepareEnrolmentManagement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        InfoEvaluation evaluationComponent = new InfoEvaluation();
        Object[] args = { executionCourseCode, commonComponent, evaluationComponent, null,
                evaluationCode, null };

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "TeacherAdministrationSiteComponentService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                .getExecutionCourse().getIdInternal());

        return mapping.findForward("viewEvaluationEnrolmentManagementMenu");
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            parameterCode = new Integer(parameterCodeString);
        }
        return parameterCode;

    }

    /**
     * @author Fernanda Quitério 15/07/2003
     *  
     */
    public ActionForward prepareEditEvaluationEnrolment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        Integer evaluationCode = getFromRequest("evaluationCode", request);

        Integer executionCourseCode = getFromRequest("objectCode", request);

        ISiteComponent commonComponent = new InfoSiteCommon();
        InfoEvaluation evaluationComponent = new InfoEvaluation();
        Object[] args = { executionCourseCode, commonComponent, evaluationComponent, null,
                evaluationCode, null };

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "TeacherAdministrationSiteComponentService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        InfoEvaluation infoEvaluation = (InfoEvaluation) siteView.getComponent();
        if (infoEvaluation != null) {
            if (infoEvaluation instanceof InfoExam) {
                InfoExam infoExam = (InfoExam) infoEvaluation;
                DynaValidatorForm periodForm = (DynaValidatorForm) form;
                periodForm.set("enrollmentBeginDayFormatted", infoExam.getEnrollmentBeginDayFormatted());
                periodForm.set("enrollmentBeginTimeFormatted", infoExam
                        .getEnrollmentBeginTimeFormatted());
                periodForm.set("enrollmentEndDayFormatted", infoExam.getEnrollmentEndDayFormatted());
                periodForm.set("enrollmentEndTimeFormatted", infoExam.getEnrollmentEndTimeFormatted());
            }
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                .getExecutionCourse().getIdInternal());
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("editEvaluationEnrolmentPeriod");
    }

    public ActionForward editExamEnrollment(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        DynaActionForm examEnrollmentForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        Integer examIdInternal = new Integer(request.getParameter("evaluationCode"));
        Integer disciplinaExecucaoIdInternal = new Integer(request.getParameter("objectCode"));

        String enrollmentBeginDay = (String) examEnrollmentForm.get("enrollmentBeginDayFormatted");
        String enrollmentBeginHour = (String) examEnrollmentForm.get("enrollmentBeginTimeFormatted");
        String enrollmentEndDay = (String) examEnrollmentForm.get("enrollmentEndDayFormatted");
        String enrollmentEndHour = (String) examEnrollmentForm.get("enrollmentEndTimeFormatted");
        if (enrollmentBeginDay.length() == 0 || enrollmentBeginHour.length() == 0
                || enrollmentEndDay.length() == 0 || enrollmentEndHour.length() == 0) {
            setErrorMessage(request, "error.form.incomplete");
            return mapping.getInputForward();
        }

        String[] enrollmentBeginDayArray = enrollmentBeginDay.split("/");
        String[] enrollmentBeginHourArray = enrollmentBeginHour.split(":");
        String[] enrollmentEndDayArray = enrollmentEndDay.split("/");
        String[] enrollmentEndHourArray = enrollmentEndHour.split(":");
        Calendar beginDate = Calendar.getInstance();
        beginDate.set(Calendar.YEAR, new Integer(enrollmentBeginDayArray[2]).intValue());
        beginDate.set(Calendar.MONTH, new Integer(enrollmentBeginDayArray[1]).intValue() - 1);
        beginDate.set(Calendar.DAY_OF_MONTH, new Integer(enrollmentBeginDayArray[0]).intValue());

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.HOUR_OF_DAY, new Integer(enrollmentBeginHourArray[0]).intValue());
        beginTime.set(Calendar.MINUTE, new Integer(enrollmentBeginHourArray[1]).intValue());

        Calendar endDate = Calendar.getInstance();

        endDate.set(Calendar.YEAR, new Integer(enrollmentEndDayArray[2]).intValue());
        endDate.set(Calendar.MONTH, new Integer(enrollmentEndDayArray[1]).intValue() - 1);
        endDate.set(Calendar.DAY_OF_MONTH, new Integer(enrollmentEndDayArray[0]).intValue());

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, new Integer(enrollmentEndHourArray[0]).intValue());
        endTime.set(Calendar.MINUTE, new Integer(enrollmentEndHourArray[1]).intValue());

        Object args[] = { disciplinaExecucaoIdInternal, examIdInternal, beginDate.getTime(), endDate.getTime(), beginTime.getTime(),
                endTime };

        try {
            ServiceUtils.executeService(userView, "EditWrittenEvaluationEnrolmentPeriod", args);
        } catch (DomainException e) {
            setErrorMessage(request, e.getKey());
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            setErrorMessage(request, e.getMessage());
            return mapping.getInputForward();
        }

        request.setAttribute("evaluationCode", examIdInternal);
        request.setAttribute("objectCode", disciplinaExecucaoIdInternal);

        return prepareEnrolmentManagement(mapping, form, request, response);
    }

    private void setErrorMessage(HttpServletRequest request, String message) {
        ActionErrors actionErrors = new ActionErrors();
        ActionError actionError = new ActionError(message);
        actionErrors.add(message, actionError);
        saveErrors(request, actionErrors);
    }
}