package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar;

public class DeleteAcademicCalendar extends Service {

    public void run(AcademicCalendar academicCalendar) {
	academicCalendar.delete();
    }
}
