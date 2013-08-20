package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.EditMarkSheet;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementEditBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/markSheetManagement.do?method=prepareSearchMarkSheet")
@Forwards({ @Forward(name = "editMarkSheet", path = "/academicAdminOffice/gradeSubmission/editMarkSheet.jsp"),
        @Forward(name = "searchMarkSheetFilled", path = "/markSheetManagement.do?method=prepareSearchMarkSheetFilled") })
// @Forward(name = "editArchiveInformation", path =
// "/academicAdminOffice/gradeSubmission/editMarkSheetArchiveInformation.jsp")
// })
public class MarkSheetEditDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareEditMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementEditBean editBean = new MarkSheetManagementEditBean();
        fillMarkSheetBean(actionForm, request, editBean);

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");

        editBean.setTeacherId(markSheet.getResponsibleTeacher().getPerson().getIstUsername());
        editBean.setEvaluationDate(markSheet.getEvaluationDateDateTime().toDate());
        editBean.setMarkSheet(markSheet);
        editBean.setEnrolmentEvaluationBeansToEdit(getEnrolmentEvaluationBeansToEdit(markSheet));
        if (markSheet.getMarkSheetState() == MarkSheetState.NOT_CONFIRMED) {
            editBean.setEnrolmentEvaluationBeansToAppend(getEnrolmentEvaluationBeansToAppend(markSheet));
        }
        return mapping.findForward("editMarkSheet");
    }

    public ActionForward updateMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementEditBean editBean = getMarkSheetManagementEditBean();
        request.setAttribute("edit", editBean);

        ActionMessages actionMessages = createActionMessages();

        checkIfTeacherIsResponsibleOrCoordinator(editBean.getCurricularCourse(), editBean.getExecutionPeriod(),
                editBean.getTeacherId(), editBean.getTeacher(), request, editBean.getMarkSheet().getMarkSheetType(),
                actionMessages);

        checkIfEvaluationDateIsInExamsPeriod(editBean.getDegreeCurricularPlan(), editBean.getExecutionPeriod(),
                editBean.getEvaluationDate(), editBean.getMarkSheet().getMarkSheetType(), request, actionMessages);

        if (actionMessages.isEmpty()) {
            try {
                EditMarkSheet.run(editBean.getMarkSheet(), editBean.getTeacher(), editBean.getEvaluationDate());

                editBean.setEnrolmentEvaluationBeansToEdit(getEnrolmentEvaluationBeansToEdit(editBean.getMarkSheet()));
                RenderUtils.invalidateViewState("edit-marksheet-enrolments");

                editBean.setEnrolmentEvaluationBeansToAppend(getEnrolmentEvaluationBeansToAppend(editBean.getMarkSheet()));
                RenderUtils.invalidateViewState("append-enrolments");

            } catch (InvalidArgumentsServiceException e) {
                addMessage(request, actionMessages, e.getMessage());
            } catch (DomainException e) {
                addMessage(request, actionMessages, e.getMessage(), e.getArgs());
            }
        }
        return mapping.findForward("editMarkSheet");
    }

    public ActionForward editMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementEditBean editBean = getMarkSheetManagementEditBean();

        ActionMessages actionMessages = createActionMessages();
        try {
            EditMarkSheet.run(editBean);
            return mapping.findForward("searchMarkSheetFilled");

        } catch (InvalidArgumentsServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
        }

        request.setAttribute("edit", editBean);
        return mapping.findForward("editMarkSheet");
    }

    private MarkSheetManagementEditBean getMarkSheetManagementEditBean() {
        return (MarkSheetManagementEditBean) RenderUtils.getViewState("edit-markSheet").getMetaObject().getObject();
    }

    private Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeansToEdit(MarkSheet markSheet) {
        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToEdit =
                new HashSet<MarkSheetEnrolmentEvaluationBean>();
        for (EnrolmentEvaluation enrolmentEvaluation : markSheet.getEnrolmentEvaluationsSet()) {
            MarkSheetEnrolmentEvaluationBean enrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
            enrolmentEvaluationBean.setGradeValue(enrolmentEvaluation.getGradeValue());
            enrolmentEvaluationBean.setEvaluationDate(enrolmentEvaluation.getExamDate());
            enrolmentEvaluationBean.setEnrolment(enrolmentEvaluation.getEnrolment());
            enrolmentEvaluationBean.setEnrolmentEvaluation(enrolmentEvaluation);
            enrolmentEvaluationBeansToEdit.add(enrolmentEvaluationBean);
        }
        return enrolmentEvaluationBeansToEdit;
    }

    private Collection<MarkSheetEnrolmentEvaluationBean> getEnrolmentEvaluationBeansToAppend(MarkSheet markSheet) {
        Collection<Enrolment> enrolments =
                markSheet.getCurricularCourse().getEnrolmentsNotInAnyMarkSheet(markSheet.getMarkSheetType(),
                        markSheet.getExecutionPeriod());
        Collection<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeansToAppend =
                new HashSet<MarkSheetEnrolmentEvaluationBean>();
        for (Enrolment enrolment : enrolments) {
            MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
            markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
            markSheetEnrolmentEvaluationBean.setEvaluationDate(markSheet.getEvaluationDate());
            enrolmentEvaluationBeansToAppend.add(markSheetEnrolmentEvaluationBean);
        }
        return enrolmentEvaluationBeansToAppend;
    }
}
