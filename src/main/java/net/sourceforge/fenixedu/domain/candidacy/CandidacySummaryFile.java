package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

public class CandidacySummaryFile extends CandidacySummaryFile_Base {

    public CandidacySummaryFile(String fileName, byte[] fileByteArray, StudentCandidacy studentCandidacy) {
        super();
        init(fileName, fileName, fileByteArray, createPermittedGroup(studentCandidacy.getPerson()));
        setStudentCandidacy(studentCandidacy);
    }

    private Group createPermittedGroup(Person candidacyStudent) {
        return UserGroup.of(candidacyStudent.getUser()).or(RoleGroup.get(RoleType.ADMINISTRATOR));
    }

    @Deprecated
    public boolean hasStudentCandidacy() {
        return getStudentCandidacy() != null;
    }

}
