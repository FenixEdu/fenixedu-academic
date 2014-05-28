/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = TutorshipApp.class, path = "low-performance-students",
        titleKey = "link.tutorship.students.ListLowPerformance", bundle = "ApplicationResources")
@Mapping(path = "/studentLowPerformance", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp") })
public class StudentLowPerformanceDA extends FenixDispatchAction {

    protected final String PRESCRIPTION_BEAN = "prescriptionBean";

    @EntryPoint
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