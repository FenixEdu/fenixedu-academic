/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on Jun 5, 2006, 10:55:27 AM
 * 
 */
public class AddAnnouncementBoardBookmark extends FenixService {

	@Service
	public static void run(AnnouncementBoard board, Person person) {
		board.addBookmarkOwner(person);
	}

}