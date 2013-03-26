package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.EvaluationManagementLog;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.accessControl.VigilancyGroup;
import net.sourceforge.fenixedu.domain.util.email.ConcreteReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.PersonSender;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class ChangeConvokeActive extends FenixService {

    @Service
    public static void run(Vigilancy convoke, Boolean bool, Person person) {

        convoke.setActive(bool);
        sendEmailNotification(bool, person, convoke);
        for (ExecutionCourse ec : convoke.getAssociatedExecutionCourses()) {
            EvaluationManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.evaluation.generic.edited.vigilancy", convoke.getWrittenEvaluation()
                            .getPresentationName(), ec.getName(), ec.getDegreePresentationString());
        }
    }

    private static void sendEmailNotification(Boolean bool, Person person, Vigilancy convoke) {

        String replyTo = person.getEmail();

        VigilantGroup group = convoke.getAssociatedVigilantGroup();
        String groupEmail = group.getContactEmail();

        if (groupEmail != null) {
            if (person.isExamCoordinatorForVigilantGroup(group)) {
                replyTo = groupEmail;
            }
        }

        WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();

        String emailMessage = generateMessage(bool, convoke);
        DateTime date = writtenEvaluation.getBeginningDateTime();
        String time = writtenEvaluation.getBeginningDateHourMinuteSecond().toString();
        String beginDateString = date.getDayOfMonth() + "-" + date.getMonthOfYear() + "-" + date.getYear();

        String subject =
                BundleUtil.getStringFromResourceBundle("resources.VigilancyResources", "email.convoke.subject", new String[] {
                        writtenEvaluation.getName(), group.getName(), beginDateString, time });

        new Message(PersonSender.newInstance(person), new ConcreteReplyTo(replyTo).asCollection(), new Recipient(
                new VigilancyGroup(convoke)).asCollection(), Collections.EMPTY_LIST, Collections.EMPTY_LIST, subject,
                emailMessage, convoke.getSitesAndGroupEmails());

    }

    private static String generateMessage(Boolean bool, Vigilancy convoke) {

        WrittenEvaluation writtenEvaluation = convoke.getWrittenEvaluation();
        DateTime beginDate = writtenEvaluation.getBeginningDateTime();
        String date = beginDate.getDayOfMonth() + "-" + beginDate.getMonthOfYear() + "-" + beginDate.getYear();

        return BundleUtil.getStringFromResourceBundle(
                "resources.VigilancyResources",
                "email.convoke.active.body",
                new String[] {
                        convoke.getVigilantWrapper().getPerson().getName(),
                        (bool) ? BundleUtil.getStringFromResourceBundle("resources.VigilancyResources",
                                "email.convoke.convokedAgain") : BundleUtil.getStringFromResourceBundle(
                                "resources.VigilancyResources", "email.convoke.uncovoked"), writtenEvaluation.getFullName(),
                        date, writtenEvaluation.getBeginningDateHourMinuteSecond().toString() });
    }
}