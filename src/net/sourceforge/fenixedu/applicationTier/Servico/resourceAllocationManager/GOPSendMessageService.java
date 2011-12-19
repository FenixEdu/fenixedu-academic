package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class GOPSendMessageService {
    private static Sender GOP_SENDER = null;

    private static Sender getGOPSender() {
	if (GOP_SENDER == null) {
	    GOP_SENDER = initGOPSender();
	    if (GOP_SENDER == null) {
		System.out.println("WARN: GOPSender couldn't be found, using SystemSender ...");
		GOP_SENDER = RootDomainObject.getInstance().getSystemSender();
	    }
	}
	return GOP_SENDER;
    }

    private static Sender initGOPSender() {
	for (Sender sender : Sender.getAvailableSenders()) {
	    final Group members = sender.getMembers();
	    if (members instanceof RoleGroup) {
		if (((RoleGroup) members).getRole().getRoleType().equals(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
		    return sender;
		}
	    }
	}
	return null;
    }

    @Service
    public static void sendMessage(Collection<Recipient> spaceManagers, String email, String subject, String body) {
	final Sender sender = getGOPSender();
	if (email != null || !spaceManagers.isEmpty()) {
	    new Message(sender, sender.getConcreteReplyTos(), spaceManagers, subject, body, email);
	}
    }

    @Service
    public static void requestRoom(WrittenTest test) {
	final String date = new SimpleDateFormat("dd/MM/yyyy").format(test.getDay().getTime());
	final String time = new SimpleDateFormat("HH:mm").format(test.getBeginning().getTime());
	final String endTime = new SimpleDateFormat("HH:mm").format(test.getEnd().getTime());

	// Foi efectuado um pedido de requisição de sala para {0} da
	// disciplina
	// {1} do(s) curso(s) {4} no dia {2} das {3} às {5}

	final Set<String> courseNames = new HashSet<String>();
	final Set<String> degreeNames = new HashSet<String>();
	final Set<ExecutionDegree> degrees = new HashSet<ExecutionDegree>();
	for (ExecutionCourse course : test.getAssociatedExecutionCourses()) {
	    courseNames.add(course.getName());
	    degreeNames.add(course.getDegreePresentationString());
	    degrees.addAll(course.getExecutionDegrees());
	}

	final String degreesString = StringUtils.join(degreeNames, ",");
	final String coursesString = StringUtils.join(courseNames, ",");
	final String subject = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
		"email.request.room.subject", coursesString, test.getDescription());

	final String body = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "email.request.room.body",
		test.getDescription(), coursesString, date, time, degreesString, endTime);
	for (String email : getGOPEmail(degrees)) {
	    new Message(getGOPSender(), email, subject, body);
	}
	test.setRequestRoomSentDate(new DateTime());
    }

    @Service
    public static void requestChangeRoom(WrittenTest test, Date oldDay, Date oldBeginning, Date oldEnd) {

	final String oldDate = new SimpleDateFormat("dd/MM/yyyy").format(oldDay);
	final String oldStartTime = new SimpleDateFormat("HH:mm").format(oldBeginning);
	final String oldEndTime = new SimpleDateFormat("HH:mm").format(oldEnd);

	final String date = new SimpleDateFormat("dd/MM/yyyy").format(test.getDay().getTime());
	final String startTime = new SimpleDateFormat("HH:mm").format(test.getBeginning().getTime());
	final String endTime = new SimpleDateFormat("HH:mm").format(test.getEnd().getTime());

	final Set<String> courseNames = new HashSet<String>();
	final Set<String> degreeNames = new HashSet<String>();
	final Set<ExecutionDegree> degrees = new HashSet<ExecutionDegree>();
	for (ExecutionCourse course : test.getAssociatedExecutionCourses()) {
	    courseNames.add(course.getName());
	    degreeNames.add(course.getDegreePresentationString());
	    degrees.addAll(course.getExecutionDegrees());
	}

	String coursesString = StringUtils.join(courseNames, ",");
	String degreesString = StringUtils.join(degreeNames, ",");

	final String subject = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
		"email.request.room.subject.edit", coursesString, test.getDescription());

	// O pedido de requisição de sala para {0} da disciplina {1} do(s)
	// cursos(s) {2} efecuado em {3} para o dia {4} das {5} às {6} foi
	// alterado para o dia {7} das {8} às {9}
	final String body = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
		"email.request.room.body.edit", test.getDescription(), coursesString, degreesString,
		test.getRequestRoomSentDateString(), oldDate, oldStartTime, oldEndTime, date, startTime, endTime);
	for (String email : getGOPEmail(degrees)) {
	    new Message(getGOPSender(), email, subject, body);
	}
	test.setRequestRoomSentDate(new DateTime());
    }

    private static Set<String> getGOPEmail(Collection<ExecutionDegree> degrees) {
	Set<String> emails = new HashSet<String>();
	for (ExecutionDegree executionDegree : degrees) {
	    if (executionDegree.getCampus().isCampusAlameda()) {
		emails.add(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "email.gop.alameda"));
	    }
	    if (executionDegree.getCampus().isCampusTaguspark()) {
		emails.add(BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", "email.gop.taguspark"));
	    }
	}
	return emails;
    }
}
