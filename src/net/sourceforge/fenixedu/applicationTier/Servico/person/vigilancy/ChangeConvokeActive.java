package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.VigilancyWithCredits;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

public class ChangeConvokeActive extends Service {

    public void run(Integer convokeOID, Boolean bool, ExamCoordinator coordinator)
            throws ExcepcaoPersistencia {

        VigilancyWithCredits convoke = (VigilancyWithCredits) RootDomainObject.readDomainObjectByOID(VigilancyWithCredits.class, convokeOID);
        convoke.setActive(bool);

        Vigilant vigilant = convoke.getVigilant();
        String emailTo = vigilant.getEmail();
        final ArrayList<String> tos = new ArrayList<String>();
        tos.add(emailTo);

        Person person = coordinator.getPerson();

        String emailMessage = generateMessage(bool, convoke);
        EmailSender.send(person.getName(), person.getEmail(), tos, null, null, RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject"),
                emailMessage);
    }

    private String generateMessage(Boolean bool, VigilancyWithCredits convoke) {
     
    	String message = "";
        message = (bool) ? RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.convokedAgain") : RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.uncovoked");
        message += "\n\nProva de avaliacao: " + convoke.getWrittenEvaluation().getName();

        return message;
    }

}