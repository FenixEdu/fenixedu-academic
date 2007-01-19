package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;

public class DeleteAcademicCalendarEntry extends Service {

    public void run(AcademicCalendarEntry academicCalendarEntry) {
	academicCalendarEntry.delete();
    }
}
