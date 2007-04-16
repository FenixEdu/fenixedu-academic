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

        new UnavailablePeriod(begin, end, justification,vigilant);
    }
    
    public void run(Vigilant vigilant, DateTime begin, DateTime end, String justification, VigilantGroup group) throws ExcepcaoPersistencia {
	run(vigilant, begin, end, justification);
	
	ArrayList<String> replyTos = new ArrayList<String>();
	replyTos.add(group.getContactEmail());
	String[] contactArray = { group.getContactEmail() };
	
	String beginDate = begin.getDayOfMonth() + "/" + begin.getMonthOfYear() + "/" + begin.getYear() + " - " + String.format("%02d", begin.getHourOfDay()) + ":" + String.format("%02d", begin.getMinuteOfHour());
	String endDate = end.getDayOfMonth() + "/" + end.getMonthOfYear() + "/" + end.getYear() + " - " + end.getHourOfDay() + ":" + end.getMinuteOfHour();
	
	String message = "A seguinte disponilidade foi adicionada:\n\n" + vigilant.getPerson().getName() + " " + beginDate + " a " + endDate + "\nJustificação: " + justification;

	new Email(group.getName(),group.getContactEmail(),contactArray,replyTos,null,null,"Indisponibilidade",message);
    }

}
