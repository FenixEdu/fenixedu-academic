package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.GradesToSubmitExecutionCourseSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSendMailBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetToConfirmSendMailBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithGradesToSubmit;
import net.sourceforge.fenixedu.domain.accessControl.TeachersWithMarkSheetsToConfirm;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.UnitBasedSender;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/markSheetSendMail", module = "academicAdministration", formBean = "markSheetSendMailForm",
        input = "/academicAdminOffice/gradeSubmission/searchSendMail.jsp")
@Forwards({ @Forward(name = "searchSendMail", path = "/academicAdminOffice/gradeSubmission/searchSendMail.jsp") })
public class SendMailMarkSheetDispatchAction extends MarkSheetDispatchAction {

    public ActionForward prepareSearchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = new MarkSheetSendMailBean();
        request.setAttribute("bean", bean);
        return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareSearchSendMailPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final MarkSheetSendMailBean bean = getRenderedObject();
        bean.setMarkSheetToConfirmSendMailBean(null);
        bean.setGradesToSubmitExecutionCourseSendMailBean(null);

        RenderUtils.invalidateViewState();
        request.setAttribute("bean", bean);

        return mapping.getInputForward();
    }

    public ActionForward prepareSearchSendMailInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("bean", RenderUtils.getViewState().getMetaObject().getObject());
        return mapping.getInputForward();
    }

    public ActionForward searchSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState().getMetaObject().getObject();
        Collection<MarkSheet> markSheets = bean.getExecutionPeriod().getMarkSheetsToConfirm(bean.getDegreeCurricularPlan());
        Collection<ExecutionCourse> executionCourses =
                bean.getExecutionPeriod().getExecutionCoursesWithDegreeGradesToSubmit(bean.getDegreeCurricularPlan());
        if (!markSheets.isEmpty()) {
            Map<CurricularCourse, MarkSheetToConfirmSendMailBean> map =
                    new HashMap<CurricularCourse, MarkSheetToConfirmSendMailBean>();
            for (MarkSheet markSheet : markSheets) {
                if (map.get(markSheet.getCurricularCourse()) == null) {
                    map.put(markSheet.getCurricularCourse(), new MarkSheetToConfirmSendMailBean(markSheet, true));
                }
            }
            bean.setMarkSheetToConfirmSendMailBean(new ArrayList<MarkSheetToConfirmSendMailBean>(map.values()));
        }
        if (!executionCourses.isEmpty()) {
            Collection<GradesToSubmitExecutionCourseSendMailBean> executionCoursesBean =
                    new ArrayList<GradesToSubmitExecutionCourseSendMailBean>();
            for (ExecutionCourse course : executionCourses) {
                executionCoursesBean.add(new GradesToSubmitExecutionCourseSendMailBean(bean.getDegreeCurricularPlan(), course,
                        true));
            }
            bean.setGradesToSubmitExecutionCourseSendMailBean(executionCoursesBean);
        }
        request.setAttribute("bean", bean);
        return mapping.findForward("searchSendMail");
    }

    public ActionForward prepareMarkSheetsToConfirmSendMail(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
        Group teachersGroup =
                new TeachersWithMarkSheetsToConfirm(bean.getExecutionPeriod(), bean.getDegree(), bean.getDegreeCurricularPlan());
        String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.markSheets.to.confirm.send.mail");
        Recipient recipient = Recipient.newInstance(message, teachersGroup);
        UnitBasedSender sender = bean.getDegree().getAdministrativeOffice().getUnit().getUnitBasedSenderSet().iterator().next();
        return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward prepareGradesToSubmitSendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetSendMailBean bean = (MarkSheetSendMailBean) RenderUtils.getViewState("sendMailBean").getMetaObject().getObject();
        Group teachersGroup =
                new TeachersWithGradesToSubmit(bean.getExecutionPeriod(), bean.getDegree(), bean.getDegreeCurricularPlan());
        String message = getResources(request, "ACADEMIC_OFFICE_RESOURCES").getMessage("label.grades.to.submit.send.mail");
        Recipient recipient = Recipient.newInstance(message, teachersGroup);
        UnitBasedSender sender =
                AdministrativeOffice.readDegreeAdministrativeOffice().getUnit().getUnitBasedSenderSet().iterator().next();
        return EmailsDA.sendEmail(request, sender, recipient);
    }
}