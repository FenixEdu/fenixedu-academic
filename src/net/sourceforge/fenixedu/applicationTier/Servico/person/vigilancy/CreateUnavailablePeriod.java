package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;

public class CreateUnavailablePeriod extends FenixService {

    @Service
    public static void run(Person person, DateTime begin, DateTime end, String justification) {

	CreateUnavailable(person, begin, end, justification);
	sendEmail(person, begin, end, justification, person.getVigilantGroupsForExecutionYear(ExecutionYear
		.readCurrentExecutionYear()));
    }

    private static void CreateUnavailable(Person person, DateTime begin, DateTime end, String justification) {
	new UnavailablePeriod(begin, end, justification, person);
    }

    private static void sendEmail(Person person, DateTime begin, DateTime end, String justification, List<VigilantGroup> groups) {
	for (VigilantGroup group : groups) {
	    String bccs = group.getContactEmail();

	    String beginDate = begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear() + " - "
		    + String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour()) + "h";
	    String endDate = end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - "
		    + String.format("%02d", end.getHourOfDay()) + ":" + String.format("%02d", end.getMinuteOfHour()) + "h";
	    ;
	    String message = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.unavailablePeriod",
		    new Object[] { person.getName(), beginDate, endDate, justification });

	    String subject = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.unavailablePeriod.subject",
		    new Object[] { group.getName() });

	    Sender sender = rootDomainObject.getSystemSender();
	    List<ConcreteReplyTo> replyTos = Collections.singletonList(new ConcreteReplyTo(group.getContactEmail()));

	    new Message(sender, replyTos, Collections.EMPTY_LIST, subject, message, bccs);
	}
    }
}