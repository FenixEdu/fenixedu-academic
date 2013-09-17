package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixframework.Atomic;

public class DgesBaseProcessLauncher {

    @Atomic
    public static DgesBaseProcess launchImportation(final ExecutionYear executionYear, final Campus campus,
            final EntryPhase phase, DgesStudentImportationFile file) {
        return new DgesStudentImportationProcess(executionYear, campus, phase, file);
    }

    @Atomic
    public static ExportDegreeCandidaciesByDegreeForPasswordGeneration launchExportationCandidaciesForPasswordGeneration(
            final ExecutionYear executionYear, final EntryPhase entryPhase) {
        return new ExportDegreeCandidaciesByDegreeForPasswordGeneration(executionYear, entryPhase);
    }
}
