package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.messaging.Announcement;

public class AproveActionAnnouncement extends FenixService {

    public void run(Announcement announcement, Boolean action) {
	announcement.setApproved(action);
    }

}
