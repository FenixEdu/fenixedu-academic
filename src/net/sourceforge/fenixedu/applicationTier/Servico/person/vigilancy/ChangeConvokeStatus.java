package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;

public class ChangeConvokeStatus extends Service {

    public void run(Vigilancy vigilancy, AttendingStatus status) {
	vigilancy.setStatus(status);
    }
}
