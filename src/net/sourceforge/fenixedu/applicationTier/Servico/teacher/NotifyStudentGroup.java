package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.util.Email;

public class NotifyStudentGroup extends Service {

    public void run(ProjectSubmission submission, ExecutionCourse course, Person person) {

	ArrayList<String> emails = new ArrayList<String>();

	for (Attends attend : submission.getStudentGroup().getAttends()) {
	    emails.add(attend.getRegistration().getStudent().getPerson().getEmail());
	}

	String from = course.getSite().getMail();
	if(from==null || from.length()==0) {
	    from = person.getEmail();
	}
	String[] replyTo = { from };

	new Email(course.getNome(), from, replyTo, emails, null, null, submission
		.getProject().getName(), submission.getTeacherObservation());
    }
}
