package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
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
        	
        	String groupEmail = group.getContactEmail();
        	String[] replyTo;
        	
        	tos.addAll(getEmailsFromTeachers(writtenEvaluation));
        	
        	if(groupEmail!=null) {
        		tos.add(groupEmail);
        		replyTo = new String[] { groupEmail };
        	}
        	else {
        		replyTo = new String[] { person.getEmail() };
        	}
        	
        	DateTime date = writtenEvaluation.getBeginningDateTime();
        	String convokeTitle = RenderUtils.getResourceString("VIGILANCY_RESOURCES", "email.convoke.subject");
        	
        	String subject = convokeTitle + writtenEvaluation.getName() + " " + group.getName() + " " + date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();  
        	EmailSender.send(person.getName(), (groupEmail == null) ? person.getEmail() : groupEmail, replyTo, tos, null, null, subject,
                    emailMessage);
        }
    }

    private List<String> getEmailsFromTeachers(WrittenEvaluation writtenEvaluation) {
    	Set<String> emails = new HashSet<String>();
    	for(ExecutionCourse course : writtenEvaluation.getAssociatedExecutionCourses()) {
    		emails.add(course.getSite().getMail());
    	}
    	return new ArrayList<String>(emails);
    }
}