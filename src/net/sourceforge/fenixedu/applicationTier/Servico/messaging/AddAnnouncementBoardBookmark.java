/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 * Created on Jun 5, 2006, 10:55:27 AM
 *
 */
public class AddAnnouncementBoardBookmark extends Service {
    
    public void run(AnnouncementBoard board, Person person)
    {
        board.addBookmarkOwner(person);
    }

}
