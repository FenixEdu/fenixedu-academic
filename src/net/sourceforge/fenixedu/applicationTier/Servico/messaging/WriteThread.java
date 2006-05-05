/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.WriteMessage.WriteMessageParameters;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Forum;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 * Created on May 5, 2006, 11:48:06 AM
 *
 */
public class WriteThread extends Service{

    static public class WriteThreadParameters
    {
	public Forum forum;
	public Person creator;
	public String subject;
	public String messageText;
    }
    
    public void run(WriteThreadParameters p)
    {
	Forum forum = p.forum;
	forum.createConversationThread(p.creator, p.subject);
	
	WriteMessageParameters parameters = new WriteMessageParameters();
	parameters.creator=p.creator;
	parameters.text=p.messageText;
	WriteMessage writeMessage = new WriteMessage();
	writeMessage.run(parameters);
    }
}
