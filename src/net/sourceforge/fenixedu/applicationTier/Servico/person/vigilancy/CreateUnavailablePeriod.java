package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CreateUnavailablePeriod extends FenixService {

    @Service
    public static void run(Person person, DateTime begin, DateTime end, String justification) {

	CreateUnavailable(person, begin, end, justification);
	sendEmail(person, begin, end, justification, person.getVigilantGroupsForExecutionYear(ExecutionYear
		.readFirstExecutionYear()));
    }

    private static void CreateUnavailable(Person person, DateTime begin, DateTime end, String justification) {
	new UnavailablePeriod(begin, end, justification, person);
    }

    private static void sendEmail(Person person, DateTime begin, DateTime end, String justification, List<VigilantGroup> groups) {
	for (VigilantGroup group : groups) {
	    ArrayList<String> replyTos = new ArrayList<String>();
	    replyTos.add(group.getContactEmail());
	    String[] contactArray = { group.getContactEmail() };

	    String beginDate = begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear() + " - "
		    + String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour()) + "h";
	    String endDate = end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - "
		    + String.format("%02d", end.getHourOfDay()) + ":" + String.format("%02d", end.getMinuteOfHour()) + "h";

	    String message = "A seguinte indisponilidade foi adicionada:\n\n" + person.getName() + " " + beginDate + " a "
		    + endDate + "\nJustificação: " + justification;

	    new Email(group.getName(), group.getContactEmail(), contactArray, replyTos, null, null, "Indisponibilidade", message);
	}
    }
}