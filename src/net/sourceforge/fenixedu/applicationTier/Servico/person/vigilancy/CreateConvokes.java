package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class CreateConvokes extends Service {

    public void run(List<Vigilant> vigilants, WrittenEvaluation writtenEvaluation, VigilantGroup group,
            ExamCoordinator coordinator, String emailMessage) throws ExcepcaoPersistencia {
        group.convokeVigilants(vigilants, writtenEvaluation);
        if (emailMessage.length() != 0) {
        	Person person = coordinator.getPerson();
            final ArrayList<String> tos = new ArrayList<String>();
        	for (Vigilant vigilant : vigilants) {
                String emailTo = vigilant.getEmail();
                tos.add(emailTo);
            }
        	DateTime date = writtenEvaluation.getBeginningDateTime();
        	String subject = writtenEvaluation.getName() + " " + group.getName() + " " + date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();  
        	EmailSender.send(person.getName(), person.getEmail(), tos, null, null, subject,
                    emailMessage);
        }
    }

}