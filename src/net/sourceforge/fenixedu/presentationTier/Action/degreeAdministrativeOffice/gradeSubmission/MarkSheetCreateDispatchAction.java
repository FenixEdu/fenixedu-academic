/*
 * Created on May 5, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class MarkSheetCreateDispatchAction extends MarkSheetDispatchAction {
    
    public ActionForward prepareCreateMarkSheet(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
        markSheetManagementCreateBean.setExecutionPeriod(ExecutionPeriod.readActualExecutionPeriod());
        request.setAttribute("edit", markSheetManagementCreateBean);
        
        return mapping.findForward("createMarkSheetStep1");
    }
    
    public ActionForward prepareCreateMarkSheetFilled(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementCreateBean markSheetManagementCreateBean = new MarkSheetManagementCreateBean();
        fillMarkSheetBean(actionForm, request, markSheetManagementCreateBean);
        request.setAttribute("edit", markSheetManagementCreateBean);
        
        return mapping.findForward("createMarkSheetStep1");
    }

    public ActionForward createMarkSheetStepOne(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementCreateBean createBean = (MarkSheetManagementCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("edit", createBean);
        
        ActionMessages actionMessages = createActionMessages();
        
        Teacher teacher = Teacher.readByNumber(createBean.getTeacherNumber());
        createBean.setTeacher(teacher);
        
        checkIfTeacherIsResponsibleOrCoordinator(createBean, teacher, request, actionMessages);
        checkIfEvaluationDateIsInExamsPeriod(createBean, request, actionMessages);        
        prepareCreateEnrolmentEvaluationsForMarkSheet(createBean, request, actionMessages);
        
        if (!actionMessages.isEmpty()) {
            return mapping.findForward("createMarkSheetStep1");
        } else {
            return mapping.findForward("createMarkSheetStep2");
        }
    }

    private void prepareCreateEnrolmentEvaluationsForMarkSheet(MarkSheetManagementCreateBean createBean, HttpServletRequest request, ActionMessages actionMessages) {
        
        Collection<Enrolment> enrolments = createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(createBean.getMarkSheetType(), createBean.getExecutionPeriod());

        if (enrolments.isEmpty()) {
            addMessage(request, actionMessages, "error.allStudentsAreInMarkSheets");
        } else {
            Set<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans = new HashSet<MarkSheetEnrolmentEvaluationBean>();
            for (Enrolment enrolment : enrolments) {
                MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
                markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
                markSheetEnrolmentEvaluationBean.setEvaluationDate(createBean.getEvaluationDate());
                enrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
            }
            createBean.setEnrolmentEvaluationBeans(enrolmentEvaluationBeans);    
        }
    }

    private void checkIfEvaluationDateIsInExamsPeriod(MarkSheetManagementCreateBean createBean, HttpServletRequest request, ActionMessages actionMessages) {
        
        ExecutionDegree executionDegree = createBean.getDegreeCurricularPlan().getExecutionDegreeByYear(createBean.getExecutionPeriod().getExecutionYear());
        
        if(executionDegree == null || !executionDegree.isEvaluationDateInExamPeriod(createBean.getEvaluationDate(), createBean.getExecutionPeriod(), createBean.getMarkSheetType())) {
        
            OccupationPeriod occupationPeriod = executionDegree.getOccupationPeriodFor(createBean.getExecutionPeriod(), createBean.getMarkSheetType());
           
            if (occupationPeriod == null) {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriod");
            } else {
                addMessage(request, actionMessages, "error.evaluationDateNotInExamsPeriodWithDates",
                        DateFormatUtil.format("dd/MM/yyyy", occupationPeriod.getStart()),
                        DateFormatUtil.format("dd/MM/yyyy", occupationPeriod.getEnd()));
            }
        }
    }

    private void checkIfTeacherIsResponsibleOrCoordinator(MarkSheetManagementCreateBean createBean, Teacher teacher, HttpServletRequest request, ActionMessages actionMessages) {
        if(teacher == null) {
            addMessage(request, actionMessages, "error.noTeacher", String.valueOf(createBean.getTeacherNumber()));
        } else if (!teacher.isResponsibleOrCoordinatorFor(createBean.getDegreeCurricularPlan(), createBean.getCurricularCourse(), createBean.getExecutionPeriod())) {
            createBean.setTeacherNumber(null);
            addMessage(request, actionMessages, "error.teacherNotResponsibleOrNorCoordinator");
        }
    }
    
    public ActionForward createMarkSheetStepTwo(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        MarkSheetManagementCreateBean markSheetManagementCreateBean = (MarkSheetManagementCreateBean) RenderUtils.getViewState("edit-invisible").getMetaObject().getObject();
        markSheetManagementCreateBean.setEnrolmentEvaluationBeans((Collection<MarkSheetEnrolmentEvaluationBean>) RenderUtils.getViewState("edit-enrolments").getMetaObject().getObject());
        
        ActionMessages actionMessages = createActionMessages();
        try {
            
            ServiceUtils.executeService(getUserView(request), "CreateMarkSheet", new Object[] {markSheetManagementCreateBean});
            return mapping.findForward("searchMarkSheet");
            
        } catch (FenixFilterException e) {
            addMessage(request, actionMessages, "error.notAuthorized");
        } catch (FenixServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage());
        }
        return mapping.findForward("createMarkSheetStep2");
    }

}
