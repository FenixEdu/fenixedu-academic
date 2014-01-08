package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixframework.Atomic;

public class DgesStudentImportationFile extends DgesStudentImportationFile_Base {
    private DgesStudentImportationFile() {
        super();
    }

    protected DgesStudentImportationFile(byte[] contents, String filename) {
        this();
        init(filename, filename, contents, null);
    }

    @Atomic
    public static DgesStudentImportationFile create(byte[] contents, String filename, ExecutionYear executionYear, Campus campus,
            EntryPhase entryPhase) {
        if (executionYear == null) {
            throw new DomainException("error.DgesStudentImportationFile.execution.year.is.null");
        }

        if (campus == null) {
            throw new DomainException("error.error.DgesStudentImportationFile.campus.is.null");
        }

        if (entryPhase == null) {
            throw new DomainException("error.error.DgesStudentImportationFile.entry.phase.is.null");
        }

        return new DgesStudentImportationFile(contents, filename);
    }

    @Deprecated
    public boolean hasDgesStudentImportationProcess() {
        return getDgesStudentImportationProcess() != null;
    }

}
