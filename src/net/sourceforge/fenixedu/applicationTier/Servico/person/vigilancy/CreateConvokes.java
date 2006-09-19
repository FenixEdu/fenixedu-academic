package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

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
        	for (Vigilant vigilant : vigilants) {
                String emailTo = vigilant.getEmail();
                final ArrayList<String> tos = new ArrayList<String>();
                tos.add(emailTo);
                EmailSender.send(person.getName(), person.getEmail(), tos, null, null, "Convocatória",
                        emailMessage);
            }
        }
    }

}