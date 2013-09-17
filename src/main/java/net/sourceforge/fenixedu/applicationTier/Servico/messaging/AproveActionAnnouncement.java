package net.sourceforge.fenixedu.applicationTier.Servico.messaging;


import net.sourceforge.fenixedu.domain.messaging.Announcement;
import pt.ist.fenixframework.Atomic;

public class AproveActionAnnouncement {

    @Atomic
    public static void run(Announcement announcement, Boolean action) {
        announcement.setApproved(action);
    }

}