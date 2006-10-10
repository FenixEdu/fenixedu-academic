/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.messaging.Announcement;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created on Jun 1, 2006, 3:15:29
 *         PM
 * 
 */
public class DeleteAnnouncement extends Service {

    public void run(Announcement announcement) {

        announcement.delete();
    }

}
