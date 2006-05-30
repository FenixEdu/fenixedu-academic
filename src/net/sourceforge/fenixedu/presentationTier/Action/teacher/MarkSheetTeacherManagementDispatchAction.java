/*
 * Created on May 18, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherGradeSubmissionBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.gradeSubmission.MarkSheetTeacherMarkBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FinalMark;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class MarkSheetTeacherManagementDispatchAction extends ManageExecutionCourseDA {
    
    private ResourceBundle getResourceBundle(String bundleName) {
        return ResourceBundle.getBundle(bundleName);
    }
    
    private void addMessage(HttpServletRequest request, ActionMessages actionMessages, String keyMessage, String ... args) {
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(keyMessage, args));
        saveMessages(request, actionMessages);
    }
    
    public ActionForward evaluationIndex(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("evaluationIndex");
    }
    
    public ActionForward prepareSubmitMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        MarkSheetTeacherGradeSubmissionBean submissionBean = new MarkSheetTeacherGradeSubmissionBean();
        submissionBean.setExecutionCourse((ExecutionCourse) request.getAttribute("executionCourse"));
        
        request.setAttribute("submissionBean", submissionBean);
        return mapping.findForward("gradeSubmission.step.one");
    }
    
    public ActionForward gradeSubmissionStepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetTeacherGradeSubmissionBean submissionBean = (MarkSheetTeacherGradeSubmissionBean) RenderUtils.getViewState().getMetaObject().getObject();       
        submissionBean.setMarksToSubmit(getMarksToSubmit(submissionBean));
        
        request.setAttribute("submissionBean", submissionBean);
        return mapping.findForward("gradeSubmission.step.two");
    }

    public ActionForward gradeSubmissionStepTwo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        IUserView userView = getUserView(request);
        MarkSheetTeacherGradeSubmissionBean submissionBean = (MarkSheetTeacherGradeSubmissionBean) RenderUtils.getViewState("submissionBean-invisible").getMetaObject().getObject();
        submissionBean.setResponsibleTeacher(userView.getPerson().getTeacher());

        ActionMessages actionMessages = new ActionMessages();
        try {
            ServiceUtils.executeService(userView, "CreateMarkSheetByTeacher", new Object[] { submissionBean });
            return mapping.findForward("mainPage");
            
        } catch (NotAuthorizedException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (InvalidArgumentsServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        request.setAttribute("submissionBean", submissionBean);
        return mapping.findForward("gradeSubmission.step.two");
    }
    
    public ActionForward backToMainPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {
        
        return mapping.findForward("mainPage");
    }

    private Collection<MarkSheetTeacherMarkBean> getMarksToSubmit(MarkSheetTeacherGradeSubmissionBean submissionBean) {
        
        Collection<MarkSheetTeacherMarkBean> result = new HashSet<MarkSheetTeacherMarkBean>();
        for (Enrolment enrolment : getEnrolmentsNotInAnyMarkSheet(submissionBean)) {
            Attends attends = enrolment.getAttendsByExecutionCourse(submissionBean.getExecutionCourse());
            if (attends != null) {
                String mark = getMark(attends);
                String enrolmentEvaluationType = getEnrolmentEvaluationType(submissionBean, enrolment); 
                boolean toSubmitMark = mark.length() != 0;
                result.add(new MarkSheetTeacherMarkBean(attends, submissionBean.getEvaluationDate(), mark, enrolmentEvaluationType, toSubmitMark));
            }
        }

        return result;
    }

    private String getEnrolmentEvaluationType(MarkSheetTeacherGradeSubmissionBean submissionBean, Enrolment enrolment) {
        ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
        return enumerationBundle.getString(enrolment.isImprovementForExecutionCourse(submissionBean.getExecutionCourse()) ? 
                EnrolmentEvaluationType.IMPROVEMENT.name()
                : enrolment.getEnrolmentEvaluationType().name());
    }

    private String getMark(Attends attends) {
        FinalMark finalMark = attends.getFinalMark();
        return (finalMark != null) ? finalMark.getMark() : "";
    }

    private Collection<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetTeacherGradeSubmissionBean submissionBean) {
        Collection<Enrolment> enrolmentsNotInAnyMarkSheet = new HashSet<Enrolment>();
        for (CurricularCourse curricularCourse : submissionBean.getCurricularCourses()) {
            enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.NORMAL, submissionBean.getExecutionCourse().getExecutionPeriod()));
            enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.IMPROVEMENT, submissionBean.getExecutionCourse().getExecutionPeriod()));
            enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.SPECIAL_SEASON, submissionBean.getExecutionCourse().getExecutionPeriod()));
        }
        return enrolmentsNotInAnyMarkSheet;
    }
       
}
