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
/**
 * @author Sérgio Silva ist152416
 */

package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupingGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupGroup;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "teacher", path = "/sendMailToWorkGroupStudents", functionality = ManageExecutionCourseDA.class)
public class SendMailToWorkGroupStudents extends FenixDispatchAction {

    public ActionForward sendEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionCourseCode = request.getParameter("executionCourseID");
        String studentGroupCode = request.getParameter("studentGroupCode");
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        Group groupToSend = StudentGroupGroup.get(studentGroup);
        Sender sender = ExecutionCourseSender.newInstance(executionCourse);
        final String label =
                getResources(request, "APPLICATION_RESOURCES").getMessage("label.students.group.send.email",
                        studentGroup.getGroupNumber(), studentGroup.getGrouping().getName());
        Recipient recipient = Recipient.newInstance(label, groupToSend);
        return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward sendGroupingEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionCourseCode = request.getParameter("executionCourseID");
        String groupingCode = request.getParameter("groupingCode");
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        Grouping grouping = FenixFramework.getDomainObject(groupingCode);
        Group groupToSend = GroupingGroup.get(grouping);
        Sender sender = ExecutionCourseSender.newInstance(executionCourse);
        final String label =
                getResources(request, "APPLICATION_RESOURCES").getMessage("label.students.grouping.send.email",
                        grouping.getName());
        Recipient recipient = Recipient.newInstance(label, groupToSend);
        return EmailsDA.sendEmail(request, sender, recipient);
    }
}
