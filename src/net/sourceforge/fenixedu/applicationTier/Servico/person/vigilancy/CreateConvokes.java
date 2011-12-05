package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CreateConvokes extends FenixService {

    @Service
    public static void run(List<VigilantWrapper> vigilants, WrittenEvaluation writtenEvaluation, VigilantGroup group,
	    ExamCoordinator coordinator, String emailMessage) {
	group.convokeVigilants(vigilants, writtenEvaluation);

	Set<Person> recievers = new HashSet<Person>();
	Set<String> bccs = new HashSet<String>();

	if (emailMessage.length() != 0) {
	    Person person = coordinator.getPerson();
	    for (VigilantWrapper vigilant : vigilants) {
		recievers.add(vigilant.getPerson());
	    }

	    String groupEmail = group.getContactEmail();
	    String replyTo;

	    recievers.addAll(writtenEvaluation.getTeachers());

	    if (groupEmail != null) {
		bccs.add(groupEmail);
		replyTo = groupEmail;
	    } else {
		replyTo = person.getEmail();
	    }

	    DateTime date = writtenEvaluation.getBeginningDateTime();
	    String beginDateString = date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();

	    String subject = BundleUtil.getStringFromResourceBundle("resources.VigilancyResources", "email.convoke.subject", new String[] {
		    group.getEmailSubjectPrefix(), writtenEvaluation.getName(), group.getName(), beginDateString });

	    new Message(PersonSender.newInstance(person), new ConcreteReplyTo(replyTo).asCollection(), new Recipient(
		    new FixedSetGroup(recievers)).asCollection(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject,
		    emailMessage, bccs);
	}
    }
}