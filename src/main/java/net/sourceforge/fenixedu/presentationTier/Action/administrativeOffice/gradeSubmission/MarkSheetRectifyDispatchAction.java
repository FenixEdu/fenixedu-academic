package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.CreateRectificationMarkSheet;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/rectifyMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/academicAdminOffice/gradeSubmission/createMarkSheetStep1.jsp")
@Forwards({ @Forward(name = "searchMarkSheetFilled", path = "/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "rectifyMarkSheetStep1", path = "/academicAdminOffice/gradeSubmission/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2", path = "/academicAdminOffice/gradeSubmission/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdminOffice/gradeSubmission/viewMarkSheet.jsp") })
public class MarkSheetRectifyDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareRectifyMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = rootDomainObject.readMarkSheetByOID((Integer) form.get("msID"));

        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        fillMarkSheetBean(actionForm, request, rectifyBean);
        rectifyBean.setUrl(buildUrl(form));

        rectifyBean.setMarkSheet(markSheet);
        request.setAttribute("rectifyBean", rectifyBean);
        request.setAttribute("msID", form.get("msID"));

        List<EnrolmentEvaluation> enrolmentEvaluations = new ArrayList<EnrolmentEvaluation>(markSheet.getEnrolmentEvaluations());
        Collections.sort(enrolmentEvaluations, EnrolmentEvaluation.SORT_BY_STUDENT_NUMBER);
        request.setAttribute("enrolmentEvaluations", enrolmentEvaluations);
        return mapping.findForward("rectifyMarkSheetStep1");
    }

    public ActionForward rectifyMarkSheetStepOneByEvaluation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer evaluationID = (Integer) form.get("evaluationID");
        EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(evaluationID);
        MarkSheet markSheet = enrolmentEvaluation.getMarkSheet();
        MarkSheetRectifyBean rectifyBean = new MarkSheetRectifyBean();
        rectifyBean.setMarkSheet(markSheet);
        rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
        return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    public ActionForward rectifyMarkSheetStepOneByStudentNumber(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

        Integer studentNumber = rectifyBean.getStudentNumber();
        Student student = Student.readStudentByNumber(studentNumber);

        if (student == null) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.no.student", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        MarkSheet markSheet = rectifyBean.getMarkSheet();
        EnrolmentEvaluation enrolmentEvaluation = markSheet.getEnrolmentEvaluationByStudent(student);

        if (enrolmentEvaluation == null) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.no.student.in.markSheet", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        if (!enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
                && !enrolmentEvaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)) {
            ActionMessages actionMessages = new ActionMessages();
            addMessage(request, actionMessages, "error.markSheet.student.alreadyRectified", studentNumber.toString());
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        }
        return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean, enrolmentEvaluation);
    }

    private ActionForward rectifyMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, MarkSheetRectifyBean rectifyBean, EnrolmentEvaluation enrolmentEvaluation) {
        rectifyBean.setEnrolmentEvaluation(enrolmentEvaluation);
        request.setAttribute("rectifyBean", rectifyBean);

        return mapping.findForward("rectifyMarkSheetStep2");
    }

    public ActionForward rectifyMarkSheetStepTwo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        MarkSheetRectifyBean rectifyBean = (MarkSheetRectifyBean) RenderUtils.getViewState().getMetaObject().getObject();

        ActionMessages actionMessages = new ActionMessages();
        IUserView userView = getUserView(request);
        try {
            CreateRectificationMarkSheet.run(rectifyBean.getMarkSheet(), rectifyBean.getEnrolmentEvaluation(),
                    rectifyBean.getRectifiedGrade(), rectifyBean.getEvaluationDate(), rectifyBean.getReason(),
                    userView.getPerson());
            return mapping.findForward("searchMarkSheetFilled");
        } catch (DomainException e) {
            addMessage(request, actionMessages, e.getMessage(), e.getArgs());
            return rectifyMarkSheetStepOne(mapping, actionForm, request, response, rectifyBean,
                    rectifyBean.getEnrolmentEvaluation());
        }
    }

    public ActionForward validationError(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        String[] pages = request.getParameterValues("page");
        String page = pages[1];
        if (page.equals("1")) {
            return prepareRectifyMarkSheet(mapping, actionForm, request, response);
        } else {
            return rectifyMarkSheetStepOneByEvaluation(mapping, actionForm, request, response);
        }
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("searchMarkSheetFilled");
    }

    public ActionForward showRectificationHistoric(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        Integer evaluationID = (Integer) form.get("evaluationID");

        EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(evaluationID);
        Enrolment enrolment = enrolmentEvaluation.getEnrolment();

        List<EnrolmentEvaluation> rectifiedAndRectificationEvaluations =
                enrolment.getConfirmedEvaluations(enrolmentEvaluation.getMarkSheet().getMarkSheetType());
        if (!rectifiedAndRectificationEvaluations.isEmpty()) {
            request.setAttribute("enrolmentEvaluation", rectifiedAndRectificationEvaluations.remove(0));
            request.setAttribute("rectificationEvaluations", rectifiedAndRectificationEvaluations);
        }

        return mapping.findForward("showRectificationHistoric");
    }
}
