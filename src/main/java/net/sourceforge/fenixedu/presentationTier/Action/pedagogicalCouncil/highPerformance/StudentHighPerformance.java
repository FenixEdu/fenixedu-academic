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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.highPerformance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.StudentHighPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = TutorshipApp.class, path = "high-performance-students",
        titleKey = "link.tutorship.students.ListHighPerformance")
@Mapping(path = "/studentHighPerformance", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "listRequests", path = "/pedagogicalCouncil/highPerformance/listHighPerformanceRequests.jsp") })
public class StudentHighPerformance extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<QueueJob> jobs = QueueJob.getLastJobsForClassOrSubClass(StudentHighPerformanceQueueJob.class, 5);
        request.setAttribute("jobs", jobs);
        return mapping.findForward("listRequests");
    }

    public ActionForward resendJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        QueueJob job = getDomainObject(request, "id");
        job.resend();
        return listRequests(mapping, actionForm, request, response);
    }

    public ActionForward cancelJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        QueueJob job = getDomainObject(request, "id");
        job.cancel();
        return listRequests(mapping, actionForm, request, response);
    }
}
