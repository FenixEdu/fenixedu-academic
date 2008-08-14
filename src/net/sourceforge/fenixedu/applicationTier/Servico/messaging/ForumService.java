/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.messaging.ForumSubscription;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.util.HtmlToTextConverterUtil;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created on
 *         May 23, 2006, 3:48:23 PM
 * 
 */
public abstract class ForumService extends Service {

    protected static final Locale DEFAULT_LOCALE = new Locale("pt");

    protected static final ResourceBundle GLOBAL_RESOURCES = ResourceBundle
	    .getBundle("resources.GlobalResources", DEFAULT_LOCALE);

    protected void sendNotifications(ConversationMessage conversationMessage) {
	this.notifyEmailSubscribers(conversationMessage);
	this.notifyLastReplier(conversationMessage);
    }

    private void notifyEmailSubscribers(ConversationMessage conversationMessage) {
	final Set<Person> readers = conversationMessage.getConversationThread().getForum().getReadersGroup().getElements();
	final Set<String> teacherEmailAddresses = new HashSet<String>();
	final Set<String> studentEmailAddresses = new HashSet<String>();
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
			teacherEmailAddresses.add(subscriber.getEmail());
		    } else {
			studentEmailAddresses.add(subscriber.getEmail());
		    }
		}
	    }
	}

	for (final ForumSubscription subscriptionToRemove : subscriptionsToRemove) {
	    conversationMessage.getConversationThread().getForum().removeForumSubscriptions(subscriptionToRemove);
	    subscriptionToRemove.delete();
	}

	sendEmailWithConversationMessage(teacherEmailAddresses, studentEmailAddresses, conversationMessage);

    }

    private void notifyLastReplier(ConversationMessage conversationMessage) {
	ConversationMessage nextToLastConversationMessage = conversationMessage.getConversationThread()
		.getNextToLastConversationMessage();

	if (nextToLastConversationMessage != null) {
	    Person nextToLastMessageReplier = nextToLastConversationMessage.getCreator();
	    if (!conversationMessage.getConversationThread().getForum()
		    .isPersonReceivingMessagesByEmail(nextToLastMessageReplier)) {
		final Set<String> teacherEmailAddresses = new HashSet<String>();
		final Set<String> studentEmailAddresses = new HashSet<String>();
		if (nextToLastMessageReplier.hasRole(RoleType.TEACHER)) {
		    teacherEmailAddresses.add(nextToLastMessageReplier.getEmail());
		} else {
		    studentEmailAddresses.add(nextToLastMessageReplier.getEmail());
		}

		sendEmailWithConversationMessage(teacherEmailAddresses, studentEmailAddresses, conversationMessage);
	    }
	}

    }

    private void sendEmailWithConversationMessage(Set<String> teacherAddresses, Set<String> studentAddresses,
	    ConversationMessage conversationMessage) {
	String emailFrom = GLOBAL_RESOURCES.getString("forum.email.from");
	String emailFromAddress = GLOBAL_RESOURCES.getString("forum.email.fromAddress");
	String emailSubject = getEmailFormattedSubject(conversationMessage.getConversationThread());

	if (!teacherAddresses.isEmpty()) {
	    new Email(emailFrom, emailFromAddress, null, null, null, teacherAddresses, emailSubject, getEmailFormattedBody(
		    conversationMessage, true));
	}

	if (!studentAddresses.isEmpty()) {
	    new Email(emailFrom, emailFromAddress, null, null, null, studentAddresses, emailSubject, getEmailFormattedBody(
		    conversationMessage, false));
	}
    }

    private String getEmailFormattedSubject(ConversationThread conversationThread) {
	String emailSubject = MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.subject"), conversationThread
		.getTitle());

	return emailSubject;
    }

    private String getEmailFormattedBody(ConversationMessage conversationMessage, boolean isForTeacher) {
	String emailBodyAsText = HtmlToTextConverterUtil.convertToText(conversationMessage.getBody().getContent());
	String emailFormattedBody = MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.body"), conversationMessage
		.getCreator().getName(), conversationMessage.getConversationThread().getTitle(), conversationMessage
		.getConversationThread().getForum().getName(), emailBodyAsText, getConversationThreadUrl(conversationMessage,
		isForTeacher));

	return emailFormattedBody;
    }

    private String getConversationThreadUrl(ConversationMessage conversationMessage, boolean isForTeacher) {
	return (isForTeacher) ? getConversationThreadUrlForTeacher(conversationMessage)
		: getConversationThreadUrlForStudent(conversationMessage);
    }

    private String getConversationThreadUrlForTeacher(final ConversationMessage conversationMessage) {
	// TODO: this should be removed when foruns can be viewed on same portal
	final ExecutionCourseForum executionCourseForum = (ExecutionCourseForum) conversationMessage.getConversationThread()
		.getForum();
	return MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.conversationThread.teacher.url"), GLOBAL_RESOURCES
		.getString("fenix.url"), executionCourseForum.getExecutionCourse().getIdInternal().toString(),
		conversationMessage.getConversationThread().getForum().getIdInternal().toString(), conversationMessage
			.getConversationThread().getIdInternal().toString());
    }

    private String getConversationThreadUrlForStudent(final ConversationMessage conversationMessage) {
	return MessageFormat.format(GLOBAL_RESOURCES.getString("forum.email.conversationThread.student.url"), GLOBAL_RESOURCES
		.getString("fenix.url"), conversationMessage.getConversationThread().getForum().getIdInternal().toString(),
		conversationMessage.getConversationThread().getIdInternal().toString());
    }
}