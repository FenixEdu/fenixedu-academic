/*
 * Author : Goncalo Luiz
 * Creation Date: Jun 28, 2006,5:23:53 PM
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br><br>
 * Created on Jun 28, 2006,5:23:53 PM
 *
 */
public class DeleteAnnouncementBoard extends Service{

    public void run(AnnouncementBoard board)
    {
        board.delete();
    }
}
