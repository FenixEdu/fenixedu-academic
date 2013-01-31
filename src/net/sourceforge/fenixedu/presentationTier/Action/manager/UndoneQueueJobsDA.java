package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/undoneQueueJobs", module = "manager")
@Forwards({ @Forward(name = "undoneQueueJobs", path = "/manager/undoneQueueJobs.jsp") })
public class UndoneQueueJobsDA extends FenixDispatchAction {

	public ActionForward prepareUndoneQueueJobList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		List<QueueJob> allJobs = rootDomainObject.getQueueJob();
		List<QueueJob> queueJobs = new ArrayList<QueueJob>();

		for (QueueJob job : allJobs) {
			if (job.getIsNotDoneAndCancelled() || job.getIsNotDoneAndNotCancelled()) {
				if (job instanceof TutorshipStudentLowPerformanceQueueJob) {

				}
				queueJobs.add(job);
			}
		}

		request.setAttribute("queueJobList", queueJobs);

		return mapping.findForward("undoneQueueJobs");
	}

	public ActionForward resendQueuedJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		QueueJob job = getDomainObject(request, "id");
		job.resend();

		return prepareUndoneQueueJobList(mapping, actionForm, request, response);
	}

	public ActionForward cancelQueuedJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		QueueJob job = getDomainObject(request, "id");
		job.cancel();

		return prepareUndoneQueueJobList(mapping, actionForm, request, response);
	}

}
