package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentLowPerformance", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp",
        tileProperties = @Tile(title = "private.pedagogiccouncil.tutoring.studentswithlowperformance")) })
public class StudentLowPerformanceDA extends FenixDispatchAction {

    protected final String PRESCRIPTION_BEAN = "prescriptionBean";

    public ActionForward viewStudentsState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        PrescriptionBean prescriptionBean = getRenderedObject(PRESCRIPTION_BEAN);
        if (prescriptionBean == null || prescriptionBean.getSelectedPrescriptionEnum() == null
                || prescriptionBean.getExecutionYear() == null) {
            return viewJobs(mapping, actionForm, request, response, null);
        }
        TutorshipStudentLowPerformanceQueueJob job =
                TutorshipStudentLowPerformanceQueueJob.createTutorshipStudentLowPerformanceQueueJob(
                        prescriptionBean.getSelectedPrescriptionEnum(), prescriptionBean.getExecutionYear());
        RenderUtils.invalidateViewState(PRESCRIPTION_BEAN);
        return viewJobs(mapping, actionForm, request, response, job);

    }

    private PrescriptionBean getContextBean(HttpServletRequest request) {
        PrescriptionBean bean = getRenderedObject(PRESCRIPTION_BEAN);
        RenderUtils.invalidateViewState(PRESCRIPTION_BEAN);
        if (bean == null) {
            return new PrescriptionBean(null);
        }
        return bean;
    }

    public ActionForward cancelQueuedJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        cancelQueuedJob(request);
        return viewJobs(mapping, actionForm, request, response, null);
    }

    public ActionForward resendJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        resendJob(request);
        return viewJobs(mapping, actionForm, request, response, null);
    }

    private void cancelQueuedJob(HttpServletRequest request) {
        QueueJob job = getDomainObject(request, "id");
        job.cancel();
    }

    private void resendJob(HttpServletRequest request) {
        QueueJob job = getDomainObject(request, "id");
        job.resend();
    }

    public List<QueueJob> getLatestJobs() {
        return QueueJob.getLastJobsForClassOrSubClass(TutorshipStudentLowPerformanceQueueJob.class, 5);
    }

    private ActionForward viewJobs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, TutorshipStudentLowPerformanceQueueJob job) throws Exception {

        PrescriptionBean prescriptionBean = getContextBean(request);
        request.setAttribute("job", job);
        request.setAttribute("queueJobList", getLatestJobs());
        request.setAttribute(PRESCRIPTION_BEAN, prescriptionBean);
        return mapping.findForward("viewStudentsState");
    }

}