package net.sourceforge.fenixedu.domain.messaging;

import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ConversationThread extends ConversationThread_Base {

    public static final Comparator CONVERSATION_THREAD_COMPARATOR_BY_CREATION_DATE = new BeanComparator("creationDate", Collator.getInstance());
    
    public ConversationThread() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public ConversationThread(Person creator, String subject) {
        this();
        
        setCreationDate(Calendar.getInstance().getTime());
        setCreator(creator);
        setSubject(subject);
    }

    public void createConversationMessage(Person creator, String body) {
        ConversationMessage conversationMessage = new ConversationMessage(creator, body);
        this.addConversationMessages(conversationMessage);
    }
   
}