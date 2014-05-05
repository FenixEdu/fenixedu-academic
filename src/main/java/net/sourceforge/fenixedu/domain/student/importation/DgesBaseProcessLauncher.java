package net.sourceforge.fenixedu.domain.student.importation;

import org.fenixedu.spaces.domain.Space;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixframework.Atomic;

public class DgesBaseProcessLauncher {

    @Atomic
    public static DgesBaseProcess launchImportation(final ExecutionYear executionYear, final Space campus,
            final EntryPhase phase, DgesStudentImportationFile file) {
        return new DgesStudentImportationProcess(executionYear, campus, phase, file);
    }

    @Atomic
    public static ExportDegreeCandidaciesByDegreeForPasswordGeneration launchExportationCandidaciesForPasswordGeneration(
            final ExecutionYear executionYear, final EntryPhase entryPhase) {
        return new ExportDegreeCandidaciesByDegreeForPasswordGeneration(executionYear, entryPhase);
    }
}
