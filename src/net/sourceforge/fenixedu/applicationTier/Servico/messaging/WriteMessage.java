/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ConversationMessage;
import net.sourceforge.fenixedu.domain.messaging.ConversationThread;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 * Created on May 5, 2006, 1:42:24 PM
 *
 */
public class WriteMessage extends Service{

    static public class WriteMessageParameters
    {
	public Person creator;
	public ConversationThread thread;
	public String text;
    }
    
    public void run(WriteMessageParameters p)
    {
	p.thread.addConversationMessages(new ConversationMessage(p.creator,p.text));
    }
}
