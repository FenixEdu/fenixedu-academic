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
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CreateUnavailablePeriod extends FenixService {

	@Service
	public static void run(Person person, DateTime begin, DateTime end, String justification) {

		CreateUnavailable(person, begin, end, justification);
		sendEmail(person, begin, end, justification,
				person.getVigilantGroupsForExecutionYear(ExecutionYear.readCurrentExecutionYear()));
	}

	private static void CreateUnavailable(Person person, DateTime begin, DateTime end, String justification) {
		new UnavailablePeriod(begin, end, justification, person);
	}

	private static void sendEmail(Person person, DateTime begin, DateTime end, String justification, List<VigilantGroup> groups) {
		for (VigilantGroup group : groups) {
			String bccs = group.getContactEmail();

			String beginDate =
					begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear() + " - "
							+ String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour())
							+ "h";
			String endDate =
					end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - "
							+ String.format("%02d", end.getHourOfDay()) + ":" + String.format("%02d", end.getMinuteOfHour())
							+ "h";;
			String message =
					BundleUtil.getStringFromResourceBundle("resources.VigilancyResources", "email.convoke.unavailablePeriod",
							new String[] { person.getName(), beginDate, endDate, justification });

			String subject =
					BundleUtil.getStringFromResourceBundle("resources.VigilancyResources",
							"email.convoke.unavailablePeriod.subject", new String[] { group.getName() });

			Sender sender = rootDomainObject.getSystemSender();
			new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(), Collections.EMPTY_LIST, subject,
					message, bccs);
		}
	}
}