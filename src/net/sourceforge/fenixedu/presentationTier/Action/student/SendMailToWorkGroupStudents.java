/**
 * @author Sérgio Silva ist152416
 */

package net.sourceforge.fenixedu.presentationTier.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupingGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.domain.util.email.ExecutionCourseSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.EmailsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "teacher", path = "/sendMailToWorkGroupStudents")
public class SendMailToWorkGroupStudents extends FenixDispatchAction {

    public ActionForward sendEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer executionCourseCode = Integer.parseInt(request.getParameter("objectCode"));
	Integer studentGroupCode = Integer.parseInt(request.getParameter("studentGroupCode"));
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
	Group groupToSend = new StudentGroupStudentsGroup(studentGroup);
	Sender sender = ExecutionCourseSender.newInstance(executionCourse);
	final String label = getResources(request, "APPLICATION_RESOURCES").getMessage("label.students.group.send.email",
		studentGroup.getGroupNumber(), studentGroup.getGrouping().getName());
	Recipient recipient = Recipient.newInstance(label, groupToSend);
	return EmailsDA.sendEmail(request, sender, recipient);
    }

    public ActionForward sendGroupingEmail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer executionCourseCode = Integer.parseInt(request.getParameter("objectCode"));
	Integer groupingCode = Integer.parseInt(request.getParameter("groupingCode"));
	ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
	Grouping grouping = rootDomainObject.readGroupingByOID(groupingCode);
	Group groupToSend = new GroupingGroup(grouping);
	Sender sender = ExecutionCourseSender.newInstance(executionCourse);
	final String label = getResources(request, "APPLICATION_RESOURCES").getMessage("label.students.grouping.send.email",
		grouping.getName());
	Recipient recipient = Recipient.newInstance(label, groupToSend);
	return EmailsDA.sendEmail(request, sender, recipient);
    }
}
