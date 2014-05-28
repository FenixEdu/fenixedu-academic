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
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.ForunsManagement;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentParticipateApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author naat
 * @author pcma
 * 
 */
@StrutsFunctionality(app = StudentParticipateApp.class, path = "forums", titleKey = "link.viewExecutionCourseForuns")
@Mapping(module = "student", path = "/viewExecutionCourseForuns")
@Forwards({ @Forward(name = "viewForum", path = "/commons/forums/viewForum.jsp"),
        @Forward(name = "viewThread", path = "/commons/forums/viewThread.jsp"),
        @Forward(name = "createThreadAndMessage", path = "/commons/forums/createThreadAndMessage.jsp"),
        @Forward(name = "viewForuns", path = "/student/forums/viewExecutionCourseForuns.jsp") })
public class ViewExecutionCourseForunsDispatchAction extends ForunsManagement {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("contextPrefix", "/viewExecutionCourseForuns.do");
        request.setAttribute("module", "/student");
        return super.execute(mapping, actionForm, request, response);
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        SortedSet<Attends> attendsForCurrentExecutionPeriod =
                new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR_BY_EXECUTION_COURSE_NAME);
        attendsForCurrentExecutionPeriod.addAll(getLoggedPerson(request).getCurrentAttends());

        request.setAttribute("attendsForExecutionPeriod", attendsForCurrentExecutionPeriod);

        return mapping.findForward("viewForuns");

    }

}