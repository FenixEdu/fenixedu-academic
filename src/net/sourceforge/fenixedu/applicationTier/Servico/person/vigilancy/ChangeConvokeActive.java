package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.joda.time.DateTime;

public class ChangeConvokeActive extends Service {

	public void run(Vigilancy convoke, Boolean bool, Person person)
			throws ExcepcaoPersistencia {

		convoke.setActive(bool);
		sendEmailNotification(bool, person, convoke);
	}

	private void sendEmailNotification(Boolean bool, Person person, Vigilancy convoke) {
		
		Vigilant vigilant = convoke.getVigilant();
		String emailTo = vigilant.getEmail();
		final ArrayList<String> tos = new ArrayList<String>();
		tos.add(emailTo);

		VigilantGroup group =  convoke.getAssociatedVigilantGroup();
		String groupEmail = group.getContactEmail();
		String[] replyTo;
		
		if (groupEmail != null) {
			tos.add(groupEmail);
			replyTo = new String[] { groupEmail };
		} else {
			replyTo = new String[] { person.getEmail() };
		}

		WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
		tos.addAll(Vigilancy.getEmailsThatShouldBeContactedFor(writtenEvaluation));

		String emailMessage = generateMessage(bool, convoke);
		DateTime date = writtenEvaluation.getBeginningDateTime();
		String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
		String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

		String subject = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject",
				new Object[] { writtenEvaluation.getName(), group.getName(), beginDateString, time });
		new Email(person.getName(), (groupEmail != null) ? groupEmail : person.getEmail(), replyTo,
				tos, null, null, subject, emailMessage);
	}

	private String generateMessage(Boolean bool, Vigilancy convoke) {

		String message = "";
		WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
		DateTime beginDate = writtenEvaluation.getBeginningDateTime();
		String date = beginDate.getDayOfMonth() + "-" + beginDate.getMonthOfYear() + "-"
				+ beginDate.getYear();

		message = "Caro(a) " + convoke.getVigilant().getPerson().getName() + ",\n\n";
		message += (bool) ? RenderUtils.getResourceString("VIGILANCY_RESOURCES",
				"email.convoke.convokedAgain") : RenderUtils.getResourceString("VIGILANCY_RESOURCES",
				"email.convoke.uncovoked");
		message += "\n\nProva de avaliacao: " + writtenEvaluation.getFullName();
		message += "\nData: " + date + " (" + writtenEvaluation.getBeginningDateHourMinuteSecond().toString() + ")";
		return message;
	}

}