package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.Convoke;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class ChangeConvokeActive extends Service {

    private static final String YOU_HAVE_BEEN_CONVOKED = "Possui uma nova convocatória.";

    private static final String YOU_HAVE_BEEN_UNCOVOKED = "A sua convocatória foi desactivada";

    public void run(Integer convokeOID, Boolean bool, ExamCoordinator coordinator)
            throws ExcepcaoPersistencia {

        Convoke convoke = (Convoke) RootDomainObject.readDomainObjectByOID(Convoke.class, convokeOID);
        convoke.setActive(bool);

        Vigilant vigilant = convoke.getVigilant();
        String emailTo = vigilant.getEmail();
        final ArrayList<String> tos = new ArrayList<String>();
        tos.add(emailTo);

        Person person = coordinator.getPerson();

        String emailMessage = generateMessage(bool, convoke);
        EmailSender.send(person.getName(), person.getEmail(), tos, null, null, "Convocatória",
                emailMessage);
    }

    private String generateMessage(Boolean bool, Convoke convoke) {
        String message = "";
        message = (bool) ? YOU_HAVE_BEEN_CONVOKED : YOU_HAVE_BEEN_UNCOVOKED;
        message += "Prova de avaliacao: " + convoke.getWrittenEvaluation().getName();

        return message;
    }

}