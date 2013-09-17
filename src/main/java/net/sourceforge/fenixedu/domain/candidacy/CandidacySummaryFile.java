package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CandidacySummaryFile extends CandidacySummaryFile_Base {

    public CandidacySummaryFile(String fileName, byte[] fileByteArray, StudentCandidacy studentCandidacy) {
        super();
        init(getFilePath(), fileName, fileName, null, fileByteArray, createPermittedGroup(studentCandidacy.getPerson()));
        setStudentCandidacy(studentCandidacy);
    }

    private VirtualPath getFilePath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("FisrtYearFirstSemesterEnrollments", "FisrtYearFirstSemester Enrollments"));
        filePath.addNode(new VirtualPathNode("CandidacySummaries", "Candidacy Summary files"));
        return filePath;
    }

    private Group createPermittedGroup(Person candidacyStudent) {
        FixedSetGroup student = new FixedSetGroup(candidacyStudent);
        RoleGroup admins = new RoleGroup(RoleType.ADMINISTRATOR);
        return new GroupUnion(student, admins);
    }
    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

}
