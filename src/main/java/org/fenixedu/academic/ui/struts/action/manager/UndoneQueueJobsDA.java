/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerSystemManagementApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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
