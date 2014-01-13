/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import pt.ist.fenixframework.Atomic;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 *         Created on Jun 5, 2006, 10:57:10 AM
 * 
 */
public class RemoveAnnouncementBoardBookmark {

    @Atomic
    public static void run(AnnouncementBoard board, Person person) {
        board.removeBookmarkOwner(person);
    }
}