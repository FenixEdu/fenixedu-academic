package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;

import org.fenixedu.bennu.core.groups.NobodyGroup;

import pt.ist.fenixframework.Atomic;

public class IndividualCandidacyDocumentFile extends IndividualCandidacyDocumentFile_Base {

    protected IndividualCandidacyDocumentFile() {
        super();
        this.setCandidacyFileActive(Boolean.TRUE);
    }

    protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, IndividualCandidacy candidacy,
            byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        addIndividualCandidacy(candidacy);
        setCandidacyFileType(type);
        init(filename, filename, contents, NobodyGroup.get());
    }

    protected IndividualCandidacyDocumentFile(IndividualCandidacyDocumentFileType type, byte[] contents, String filename) {
        this();
        this.setCandidacyFileActive(Boolean.TRUE);
        setCandidacyFileType(type);
        init(filename, filename, contents, NobodyGroup.get());
    }

    @Atomic
    public static IndividualCandidacyDocumentFile createCandidacyDocument(byte[] contents, String filename,
            IndividualCandidacyDocumentFileType type, String processName, String documentIdNumber) {
        return new IndividualCandidacyDocumentFile(type, contents, filename);
    }

    @Override
    public boolean isPersonAllowedToAccess(Person person) {
        if (person == null) {
            return false;
        }

        // Academic Administration Permissions
        for (AcademicProgram program : AcademicAuthorizationGroup.getProgramsForOperation(person, AcademicOperationType.MANAGE_CANDIDACY_PROCESSES)) {
            for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
                if (individualCandidacy.getAllDegrees().contains(program)) {
                    return true;
                }
            }
        }

        // Coordinators
        for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
            for (Degree degree : individualCandidacy.getAllDegrees()) {
                if (degree.isCurrentCoordinator(person)) {
                    return true;
                }
            }
        }

        // Candidates
        for (IndividualCandidacy individualCandidacy : getIndividualCandidacySet()) {
            IndividualCandidacyPersonalDetails personalDetails = individualCandidacy.getPersonalDetails();
            if (personalDetails != null) {
                if (person.equals(personalDetails.getPerson())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getIndividualCandidacy() {
        return getIndividualCandidacySet();
    }

    @Deprecated
    public boolean hasAnyIndividualCandidacy() {
        return !getIndividualCandidacySet().isEmpty();
    }

    @Deprecated
    public boolean hasCandidacyFileType() {
        return getCandidacyFileType() != null;
    }

    @Deprecated
    public boolean hasCandidacyFileActive() {
        return getCandidacyFileActive() != null;
    }

}
