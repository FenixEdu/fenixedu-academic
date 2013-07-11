/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 28, 2006,5:23:53 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;


import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import pt.ist.fenixframework.Atomic;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jun 28, 2006,5:23:53 PM
 * 
 */
public class DeleteAnnouncementBoard {

    @Atomic
    public static void run(AnnouncementBoard board) {
        board.delete();
    }
}