package net.sourceforge.fenixedu.domain.student.importation;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Campus;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class DgesStudentImportationFile extends DgesStudentImportationFile_Base {

    private static final String ROOT_DIR_DESCRIPTION = "New students database from DGES";
    private static final String ROOT_DIR = "DgesStudentImportationFile";

    private DgesStudentImportationFile() {
        super();
    }

    protected DgesStudentImportationFile(byte[] contents, String filename, VirtualPath path) {
        this();
        init(path, filename, filename, null, contents, null);
    }

    protected static VirtualPath obtainVirtualPath(ExecutionYear executionYear, Campus campus, EntryPhase entryPhase) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

        filePath.addNode(new VirtualPathNode("executionYear", executionYear.getName()));
        filePath.addNode(new VirtualPathNode("campus", campus.getName()));
        filePath.addNode(new VirtualPathNode("entryPhase", entryPhase.name()));

        return filePath;
    }

    @Service
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

        return new DgesStudentImportationFile(contents, filename, obtainVirtualPath(executionYear, campus, entryPhase));
    }

}
