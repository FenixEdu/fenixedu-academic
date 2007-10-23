package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;

public class DeleteAcademicCalendarEntry extends Service {

    public void run(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntry) {
	if(entry != null) {
	    entry.delete(rootEntry);
	}
    }
}
