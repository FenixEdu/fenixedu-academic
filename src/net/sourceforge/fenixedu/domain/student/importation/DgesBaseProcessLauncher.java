package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixWebFramework.services.Service;

public class DgesBaseProcessLauncher {

    @Service
    public static DgesBaseProcess launchImportation(final ExecutionYear executionYear, final Campus campus,
            final EntryPhase phase, DgesStudentImportationFile file) {
        return new DgesStudentImportationProcess(executionYear, campus, phase, file);
    }

    @Service
    public static ExportDegreeCandidaciesByDegreeForPasswordGeneration launchExportationCandidaciesForPasswordGeneration(
            final ExecutionYear executionYear, final EntryPhase entryPhase) {
        return new ExportDegreeCandidaciesByDegreeForPasswordGeneration(executionYear, entryPhase);
    }
}
