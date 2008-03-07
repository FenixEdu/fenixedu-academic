package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class AnnouncementPredicates {

    public static final AccessControlPredicate<Announcement> approvePredicate = new AccessControlPredicate<Announcement>() {

	public boolean evaluate(final Announcement announcement) {
	    return announcement.getAnnouncementBoard().isCurrentUserApprover();
	}

    };

}
