/*
 * Created on May 18, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
        request.setAttribute("submissionBean", submissionBean);
        
        ActionMessages actionMessages = new ActionMessages();
        boolean canSubmitMarksAnyCurricularCourse = checkIfCanSubmitMarksToAnyCurricularCourse(submissionBean.getAllCurricularCourses(), submissionBean.getExecutionCourse().getExecutionPeriod(), request, actionMessages);
        Collection<MarkSheetTeacherMarkBean> marksToSubmit = getMarksToSubmit(submissionBean);
        
        if (marksToSubmit.isEmpty()) {
            addMessage(request, actionMessages, (! canSubmitMarksAnyCurricularCourse) ? "error.teacher.gradeSubmission.noStudentsToSubmitMarksInPeriods" : "error.teacher.gradeSubmission.noStudentsToSubmitMarks");
            return mapping.findForward("gradeSubmission.step.one");
        }
            
        submissionBean.setMarksToSubmit(marksToSubmit);
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
        	List<EnrolmentEvaluation> marksSubmited = (List<EnrolmentEvaluation>) ServiceUtils.executeService(userView, "CreateMarkSheetByTeacher", new Object[] { submissionBean });
        	request.setAttribute("marksSubmited", marksSubmited);
            return mapping.findForward("viewGradesSubmited");
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
                result.add(new MarkSheetTeacherMarkBean(attends, submissionBean.getEvaluationDate(),
                        getMark(attends), getEnrolmentEvaluationType(submissionBean, enrolment), getMark(attends).length() != 0));
            }
        }
        return result;
    }

    private EnrolmentEvaluationType getEnrolmentEvaluationType(MarkSheetTeacherGradeSubmissionBean submissionBean, Enrolment enrolment) {
        return enrolment.isImprovementForExecutionCourse(submissionBean.getExecutionCourse()) ? EnrolmentEvaluationType.IMPROVEMENT
                : enrolment.getEnrolmentEvaluationType();
    }

    private String getMark(Attends attends) {
        FinalMark finalMark = attends.getFinalMark();
        return (finalMark != null) ? finalMark.getMark() : "";
    }

    private Collection<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetTeacherGradeSubmissionBean submissionBean) {

        Collection<Enrolment> enrolmentsNotInAnyMarkSheet = new HashSet<Enrolment>();
        for (CurricularCourse curricularCourse : submissionBean.getAllCurricularCourses()) {
            
            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(), MarkSheetType.NORMAL)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.NORMAL, submissionBean.getExecutionCourse().getExecutionPeriod()));
            }
            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(), MarkSheetType.IMPROVEMENT)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.IMPROVEMENT, submissionBean.getExecutionCourse().getExecutionPeriod()));
            }
            if (curricularCourse.isGradeSubmissionAvailableFor(submissionBean.getExecutionCourse().getExecutionPeriod(), MarkSheetType.SPECIAL_SEASON)) {
                enrolmentsNotInAnyMarkSheet.addAll(curricularCourse.getEnrolmentsNotInAnyMarkSheet(MarkSheetType.SPECIAL_SEASON, submissionBean.getExecutionCourse().getExecutionPeriod()));
            }
        }
        return enrolmentsNotInAnyMarkSheet;
    }

    private boolean checkIfCanSubmitMarksToAnyCurricularCourse(List<CurricularCourse> curricularCourses, ExecutionPeriod executionPeriod, HttpServletRequest request, ActionMessages actionMessages) {
        boolean result = true;
        String dateFormat = "dd/MM/yyyy";
        for (CurricularCourse curricularCourse : curricularCourses) {
            if (! curricularCourse.isGradeSubmissionAvailableFor(executionPeriod)) {
                ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(executionPeriod.getExecutionYear());
                addMessage(request, actionMessages, "error.teacher.gradeSubmission.invalid.date.for.curricularCourse", curricularCourse.getDegreeCurricularPlan().getName() + " > " + curricularCourse.getName());
                addMessageGradeSubmissionNormalSeasonFirstSemester(request, actionMessages, dateFormat, executionDegree);
                addMessageGradeSubmissionNormalSeasonSecondSemester(request, actionMessages, dateFormat, executionDegree);
                addMessageGradeSubmissionSpecialSeason(request, actionMessages, dateFormat, executionDegree);
                result = false;
            }
        }
        return result;
    }

    private void addMessageGradeSubmissionSpecialSeason(HttpServletRequest request, ActionMessages actionMessages, String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionSpecialSeason() != null) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.specialSeason.dates", 
                    executionDegree.getPeriodGradeSubmissionSpecialSeason().getStartYearMonthDay().toString(dateFormat),
                    executionDegree.getPeriodGradeSubmissionSpecialSeason().getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.specialSeason.notDefined");
        }
    }

    private void addMessageGradeSubmissionNormalSeasonSecondSemester(HttpServletRequest request, ActionMessages actionMessages, String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester() != null) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.secondSemester.normalSeason.dates", 
                    executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester().getStartYearMonthDay().toString(dateFormat),
                    executionDegree.getPeriodGradeSubmissionNormalSeasonSecondSemester().getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.secondSemester.normalSeason.notDefined");
        }
    }

    private void addMessageGradeSubmissionNormalSeasonFirstSemester(HttpServletRequest request, ActionMessages actionMessages, String dateFormat, ExecutionDegree executionDegree) {
        if (executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester() != null) {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.firstSemester.normalSeason.dates", 
                    executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester().getStartYearMonthDay().toString(dateFormat),
                    executionDegree.getPeriodGradeSubmissionNormalSeasonFirstSemester().getEndYearMonthDay().toString(dateFormat));
        } else {
            addMessage(request, actionMessages, "error.teacher.gradeSubmission.firstSemester.normalSeason.notDefined");
        }
    }
    
}
