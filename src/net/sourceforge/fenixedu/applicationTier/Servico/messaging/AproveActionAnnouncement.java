package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import pt.ist.fenixWebFramework.services.Service;

public class AproveActionAnnouncement extends FenixService {

    @Service
    public static void run(Announcement announcement, Boolean action) {
        announcement.setApproved(action);
    }

}