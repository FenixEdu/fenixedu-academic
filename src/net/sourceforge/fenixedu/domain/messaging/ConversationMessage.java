package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.beanutils.BeanComparator;

    
public class ConversationMessage extends ConversationMessage_Base {
    
    public static final Comparator CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE = new BeanComparator("creationDate");
    
    public  ConversationMessage() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreationDate(Calendar.getInstance().getTime());
    }
    
    public  ConversationMessage(Person creator,String body) {
        this();
        
        setCreationDate(Calendar.getInstance().getTime());
        setCreator(creator);
        setBody(body);
    }
    
}