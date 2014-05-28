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
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerSystemManagementApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerSystemManagementApp.class, path = "queue-jobs", titleKey = "title.queuejobs")
@Mapping(path = "/undoneQueueJobs", module = "manager")
@Forwards({ @Forward(name = "undoneQueueJobs", path = "/manager/undoneQueueJobs.jsp") })
public class UndoneQueueJobsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareUndoneQueueJobList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Collection<QueueJob> allJobs = rootDomainObject.getQueueJobSet();
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
