/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 23, 2006, 3:48:23 PM
 * 
 */
public abstract class ForumService extends Service {

    private static final Locale DEFAULT_LOCALE = new Locale("pt");

    private static final ResourceBundle GLOBAL_RESOURCES = ResourceBundle.getBundle(
            "resources.GlobalResources", DEFAULT_LOCALE);

    protected void sendNotifications(ConversationMessage conversationMessage) {
        this.notifyEmailSubscribers(conversationMessage);
        this.notifyLastReplier(conversationMessage);
    }

    private void notifyEmailSubscribers(ConversationMessage conversationMessage) {
        Set<Person> readers = conversationMessage.getConversationThread().getForum().getReadersGroup()
                .getElements();
        Set<String> emailAddressesToSendMessage = new HashSet<String>();
        Set<ForumSubscription> subscriptionsToRemove = new HashSet<ForumSubscription>();

        for (ForumSubscription subscription : conversationMessage.getConversationThread().getForum()
                .getForumSubscriptions()) {
            Person subscriber = subscription.getPerson();
            if (!readers.contains(subscriber)) {
                subscriptionsToRemove.add(subscription);
            }

            if (subscription.getReceivePostsByEmail()) {
                if (subscriber.getEmail() == null) {
                    subscription.setReceivePostsByEmail(false);
                } else {

                    emailAddressesToSendMessage.add(subscriber.getEmail());
                }
            }
        }

        for (ForumSubscription subscriptionToRemove : subscriptionsToRemove) {
            conversationMessage.getConversationThread().getForum().removeForumSubscriptions(
                    subscriptionToRemove);
            subscriptionToRemove.delete();
        }

        if (!emailAddressesToSendMessage.isEmpty()) {
            sendEmailWithConversationMessage(emailAddressesToSendMessage, conversationMessage);
        }

    }

    private void notifyLastReplier(ConversationMessage conversationMessage) {
        ConversationMessage nextToLastConversationMessage = conversationMessage.getConversationThread()
                .getNextToLastConversationMessage();

        if (nextToLastConversationMessage != null) {
            Person nextToLastMessageReplier = nextToLastConversationMessage.getCreator();
            if (!conversationMessage.getConversationThread().getForum()
                    .isPersonReceivingMessagesByEmail(nextToLastMessageReplier)) {
                Set<String> addressesToSend = new HashSet<String>();
                addressesToSend.add(nextToLastMessageReplier.getEmail());
                sendEmailWithConversationMessage(addressesToSend, conversationMessage);
            }
        }

    }

    private void sendEmailWithConversationMessage(Set<String> addressesToSend,
            ConversationMessage conversationMessage) {
        String emailFrom = GLOBAL_RESOURCES.getString("forum.email.from");
        String emailFromAddress = GLOBAL_RESOURCES.getString("forum.email.fromAddress");
        String emailSubject = getEmailFormattedSubject(conversationMessage.getConversationThread());
        String emailBody = getEmailFormattedBody(conversationMessage);
        EmailSender.send(emailFrom, emailFromAddress, new ArrayList<String>(), new ArrayList<String>(),
                addressesToSend, emailSubject, emailBody);
    }

    private String getEmailFormattedSubject(ConversationThread conversationThread) {
        String emailSubject = MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.subject"),
                conversationThread.getSubject());

        return emailSubject;
    }

    private String getEmailFormattedBody(ConversationMessage conversationMessage) {
        ConversationThread conversationThread = conversationMessage.getConversationThread();
        String emailBody = MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.body"),
                conversationMessage.getCreator().getName(), conversationThread.getSubject(),
                conversationMessage.getConversationThread().getForum().getName(), conversationMessage
                        .getBody());

        return emailBody;
    }

}