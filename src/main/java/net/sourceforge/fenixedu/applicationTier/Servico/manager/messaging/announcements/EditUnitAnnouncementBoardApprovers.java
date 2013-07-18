package net.sourceforge.fenixedu.applicationTier.Servico.manager.messaging.announcements;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.messaging.AnnouncementBoardApproversBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import pt.ist.fenixWebFramework.services.Service;

public class EditUnitAnnouncementBoardApprovers {

    @Service
    public static void run(final AnnouncementBoard announcementBoard, final Collection<AnnouncementBoardApproversBean> approvers) {
        Collection<Person> persons = new HashSet<Person>();
        for (AnnouncementBoardApproversBean announcementBoardApproversBean : approvers) {
            if (announcementBoardApproversBean.isApprover()) {
                persons.add(announcementBoardApproversBean.getPerson());
            }
        }
        announcementBoard.setApprovers(new FixedSetGroup(persons));
    }
}