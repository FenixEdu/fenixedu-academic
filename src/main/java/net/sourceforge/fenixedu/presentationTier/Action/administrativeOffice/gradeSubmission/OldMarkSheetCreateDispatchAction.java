package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementCreateBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetRectifyBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/oldCreateMarkSheet", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/academicAdminOffice/gradeSubmission/oldMarkSheets/createMarkSheetStep1.jsp")
@Forwards({
        @Forward(name = "createMarkSheetStep1",
                path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/createMarkSheetStep1.jsp"),
        @Forward(name = "createMarkSheetStep2",
                path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/createMarkSheetStep2.jsp"),
        @Forward(name = "searchMarkSheetFilled", path = "/oldMarkSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "rectifyMarkSheetStep1", path = "/academicAdminOffice/gradeSubmission/rectifyMarkSheetStep1.jsp"),
        @Forward(name = "rectifyMarkSheetStep2", path = "/academicAdminOffice/gradeSubmission/rectifyMarkSheetStep2.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdminOffice/gradeSubmission/oldMarkSheets/viewMarkSheet.jsp") })
public class OldMarkSheetCreateDispatchAction extends MarkSheetCreateDispatchAction {

    @Override
    public ActionForward createMarkSheetStepOne(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementCreateBean createBean =
                (MarkSheetManagementCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        request.setAttribute("edit", createBean);

        Teacher teacher = Teacher.readByIstId(createBean.getTeacherId());
        createBean.setTeacher(teacher);

        ActionMessages actionMessages = createActionMessages();

        prepareCreateEnrolmentEvaluationsForMarkSheet(createBean, request, actionMessages);

        if (!actionMessages.isEmpty()) {
            return mapping.findForward("createMarkSheetStep1");
        } else {
            return mapping.findForward("createMarkSheetStep2");
        }
    }

    @Override
    protected MarkSheet createMarkSheet(MarkSheetManagementCreateBean createBean, IUserView userView) {
        return createBean.createOldMarkSheet(userView.getPerson());
    }

    @Override
    protected void prepareCreateEnrolmentEvaluationsForMarkSheet(MarkSheetManagementCreateBean createBean,
            HttpServletRequest request, ActionMessages actionMessages) {

        final Collection<Enrolment> enrolments =
                createBean.getCurricularCourse().getEnrolmentsNotInAnyMarkSheetForOldMarkSheets(createBean.getMarkSheetType(),
                        createBean.getExecutionPeriod());

        if (enrolments.isEmpty()) {
            addMessage(request, actionMessages, "error.allStudentsAreInMarkSheets");
        } else {
            final Set<MarkSheetEnrolmentEvaluationBean> enrolmentEvaluationBeans =
                    new HashSet<MarkSheetEnrolmentEvaluationBean>();

            for (final Enrolment enrolment : enrolments) {
                final MarkSheetEnrolmentEvaluationBean markSheetEnrolmentEvaluationBean = new MarkSheetEnrolmentEvaluationBean();
                markSheetEnrolmentEvaluationBean.setEnrolment(enrolment);
                markSheetEnrolmentEvaluationBean.setEvaluationDate(createBean.getEvaluationDate());
                enrolmentEvaluationBeans.add(markSheetEnrolmentEvaluationBean);
            }
            createBean.setEnrolmentEvaluationBeans(enrolmentEvaluationBeans);
        }
    }

    public ActionForward prepareRectifyMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = FenixFramework.getDomainObject((String) form.get("msID"));

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

}
