package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.exams;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

public class DeleteWrittenEvaluation extends FenixService {

    /**
     * @param Integer
     *            executionCourseOID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    public void run(Integer executionCourseOID, Integer writtenEvaluationOID) throws FenixServiceException {
	final WrittenEvaluation writtenEvaluationToDelete = (WrittenEvaluation) rootDomainObject
		.readEvaluationByOID(writtenEvaluationOID);
	if (writtenEvaluationToDelete == null) {
	    throw new FenixServiceException("error.noWrittenEvaluation");
	}
	if (writtenEvaluationToDelete.hasAnyVigilancies()) {
	    notifyVigilants(writtenEvaluationToDelete);
	}
	writtenEvaluationToDelete.delete();
    }

    private void notifyVigilants(WrittenEvaluation writtenEvaluation) {

	final Set<Person> tos = new HashSet<Person>();

	for (VigilantGroup group : writtenEvaluation.getAssociatedVigilantGroups()) {
	    tos.clear();
	    DateTime date = writtenEvaluation.getBeginningDateTime();
	    String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
	    String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

	    String subject = BundleUtil.getStringFromResourceBundle("resources.VigilancyResources", "email.convoke.subject",
		    new String[] { writtenEvaluation.getName(), group.getName(), beginDateString, time });
	    String body = BundleUtil.getStringFromResourceBundle("resources.VigilancyResources",
		    "label.writtenEvaluationDeletedMessage", new String[] { writtenEvaluation.getName(), beginDateString, time });
	    for (Vigilancy vigilancy : writtenEvaluation.getVigilancies()) {
		Person person = vigilancy.getVigilantWrapper().getPerson();
		tos.add(person);
	    }
	    Sender sender = RootDomainObject.getInstance().getSystemSender();
	    new Message(sender, new ConcreteReplyTo(group.getContactEmail()).asCollection(),
		    new Recipient(new FixedSetGroup(tos)).asCollection(), subject, body, "");

	}
    }

}
