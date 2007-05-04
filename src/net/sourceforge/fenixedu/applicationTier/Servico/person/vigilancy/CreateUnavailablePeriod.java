package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class CreateUnavailablePeriod extends Service {

	public void run(Vigilant vigilant, DateTime begin, DateTime end, String justification)
			throws ExcepcaoPersistencia {

		CreateUnavailable(vigilant, begin, end,justification);
		for(VigilantGroup group : vigilant.getVigilantGroups()) {
			sendEmail(vigilant, begin, end, justification, group);
		}
	}

	public void run(Vigilant vigilant, DateTime begin, DateTime end, String justification,
			VigilantGroup group) throws ExcepcaoPersistencia {
		CreateUnavailable(vigilant, begin, end,justification);
		sendEmail(vigilant, begin, end, justification, group);
	}

	private void CreateUnavailable(Vigilant vigilant, DateTime begin, DateTime end, String justification) {
		new UnavailablePeriod(begin, end, justification, vigilant);
	}
	
	private void sendEmail(Vigilant vigilant, DateTime begin, DateTime end, String justification,
			VigilantGroup group) {
		ArrayList<String> replyTos = new ArrayList<String>();
		replyTos.add(group.getContactEmail());
		String[] contactArray = { group.getContactEmail() };

		String beginDate = begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear()
				+ " - " + String.format("%02d", begin.getHourOfDay()) + ":"
				+ String.format("%02d", begin.getMinuteOfHour()) + "h";
		String endDate = end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - "
				+ String.format("%02d", end.getHourOfDay()) + ":"
				+ String.format("%02d", end.getMinuteOfHour()) + "h";

		String message = "A seguinte indisponilidade foi adicionada:\n\n" + vigilant.getPerson().getName()
				+ " " + beginDate + " a " + endDate + "\nJustificação: " + justification;

		new Email(group.getName(), group.getContactEmail(), contactArray, replyTos, null, null,
				"Indisponibilidade", message);
	}
}
