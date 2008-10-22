package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.vigilancy.AttendingStatus;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeConvokeStatus extends FenixService {

    @Service
    public static void run(Vigilancy vigilancy, AttendingStatus status) {
	vigilancy.setStatus(status);
    }
}