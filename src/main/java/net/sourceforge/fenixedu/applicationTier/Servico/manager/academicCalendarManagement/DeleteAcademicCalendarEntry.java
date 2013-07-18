package net.sourceforge.fenixedu.applicationTier.Servico.manager.academicCalendarManagement;


import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteAcademicCalendarEntry {

    @Service
    public static void run(AcademicCalendarEntry entry, AcademicCalendarRootEntry rootEntry) {
        if (entry != null) {
            entry.delete(rootEntry);
        }
    }
}