package net.sourceforge.fenixedu.domain.messaging;

import java.text.Collator;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

    
public class ConversationMessage extends ConversationMessage_Base {
    
    public static final Comparator CONVERSATION_MESSAGE_COMPARATOR_BY_CREATION_DATE = new BeanComparator("creationDate", Collator.getInstance());
    
    public  ConversationMessage() {
        super();
        
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    public  ConversationMessage(Person creator,String body) {
        this();
        
        setCreationDate(Calendar.getInstance().getTime());
        setCreator(creator);
        setBody(body);
    }
    
}
