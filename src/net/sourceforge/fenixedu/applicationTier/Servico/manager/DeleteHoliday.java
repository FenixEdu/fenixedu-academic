package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Holiday;

public class DeleteHoliday extends FenixService {

    public void run(final Holiday holiday) {
	holiday.delete();
    }

}
