package net.sourceforge.fenixedu.domain.messaging;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Person;

    
public class ConversationMessage extends ConversationMessage_Base {
    public  ConversationMessage() {
        super();
    }
    
    public  ConversationMessage(Person creator,String body) {
        super();
        setCreationDate(Calendar.getInstance().getTime());
        setCreator(creator);
        setBody(body);
    }
    
}
