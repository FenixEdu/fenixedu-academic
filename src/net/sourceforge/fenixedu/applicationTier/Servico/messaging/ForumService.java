/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on May 23, 2006, 3:48:23 PM
 * 
 */
public abstract class ForumService extends FenixService {

    protected static final Locale DEFAULT_LOCALE = new Locale("pt");

    protected static final ResourceBundle GLOBAL_RESOURCES = ResourceBundle
            .getBundle("resources.GlobalResources", DEFAULT_LOCALE);

    protected void sendNotifications(ConversationMessage conversationMessage) {
        this.notifyEmailSubscribers(conversationMessage);
        this.notifyLastReplier(conversationMessage);
    }

    private void notifyEmailSubscribers(ConversationMessage conversationMessage) {
        final Set<Person> readers = conversationMessage.getConversationThread().getForum().getReadersGroup().getElements();
        final Set<Person> teachers = new HashSet<Person>();
        final Set<Person> students = new HashSet<Person>();
        final Set<ForumSubscription> subscriptionsToRemove = new HashSet<ForumSubscription>();

        for (final ForumSubscription subscription : conversationMessage.getConversationThread().getForum()
                .getForumSubscriptions()) {
            Person subscriber = subscription.getPerson();
            if (!readers.contains(subscriber)) {
                subscriptionsToRemove.add(subscription);
            }

            if (subscription.getReceivePostsByEmail()) {
                if (subscriber.getEmail() == null) {
                    subscription.setReceivePostsByEmail(false);
                } else {
                    if (subscriber.hasRole(RoleType.TEACHER)) {
                        teachers.add(subscriber);
                    } else {
                        students.add(subscriber);
                    }
                }
            }
        }

        for (final ForumSubscription subscriptionToRemove : subscriptionsToRemove) {
            conversationMessage.getConversationThread().getForum().removeForumSubscriptions(subscriptionToRemove);
            subscriptionToRemove.delete();
        }

        sendEmailWithConversationMessage(teachers, students, conversationMessage);

    }

    private void notifyLastReplier(ConversationMessage conversationMessage) {
        ConversationMessage nextToLastConversationMessage =
                conversationMessage.getConversationThread().getNextToLastConversationMessage();

        if (nextToLastConversationMessage != null) {
            Person nextToLastMessageReplier = nextToLastConversationMessage.getCreator();
            if (!conversationMessage.getConversationThread().getForum()
                    .isPersonReceivingMessagesByEmail(nextToLastMessageReplier)) {
                final Set<Person> teachers = new HashSet<Person>();
                final Set<Person> students = new HashSet<Person>();
                if (nextToLastMessageReplier.hasRole(RoleType.TEACHER)) {
                    teachers.add(nextToLastMessageReplier);
                } else {
                    students.add(nextToLastMessageReplier);
                }

                sendEmailWithConversationMessage(teachers, students, conversationMessage);
            }
        }

    }

    private void sendEmailToPersons(Set<Person> persons, String personsName, String subject, String body) {
        if (!persons.isEmpty()) {
            final Recipient recipient = new Recipient(GLOBAL_RESOURCES.getString("label.teachers"), new FixedSetGroup(persons));
            SystemSender systemSender = rootDomainObject.getSystemSender();
            new Message(systemSender, systemSender.getConcreteReplyTos(), recipient.asCollection(), subject, body, "");
        }
    }

    private void sendEmailWithConversationMessage(Set<Person> teachers, Set<Person> students,
            ConversationMessage conversationMessage) {
        final String emailSubject = getEmailFormattedSubject(conversationMessage.getConversationThread());

        sendEmailToPersons(teachers, GLOBAL_RESOURCES.getString("label.teachers"), emailSubject,
                getEmailFormattedBody(conversationMessage, true));
        sendEmailToPersons(students, GLOBAL_RESOURCES.getString("label.students"), emailSubject,
                getEmailFormattedBody(conversationMessage, false));
    }

    private String getEmailFormattedSubject(ConversationThread conversationThread) {
        String emailSubject =
                MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.subject"), conversationThread.getTitle());

        return emailSubject;
    }

    private String getEmailFormattedBody(ConversationMessage conversationMessage, boolean isForTeacher) {
        String emailBodyAsText = HtmlToTextConverterUtil.convertToText(conversationMessage.getBody().getContent());

        String emailFormattedBody =
                MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.body"), conversationMessage.getCreator().getName(),
                        conversationMessage.getConversationThread().getTitle(), conversationMessage.getConversationThread()
                                .getForum().getName(), emailBodyAsText);

        return emailFormattedBody;
    }
}