package net.sourceforge.fenixedu.domain.candidacyProcess;

import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import pt.ist.fenixframework.Atomic;

public class DegreeOfficePublicCandidacyHashCodeOperations {

    @Atomic
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        return DegreeOfficePublicCandidacyHashCode.getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
                individualCandidadyProcessClass, parentProcess, email);
    }

    @Atomic
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCode(
            Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
            String email) throws HashCodeForEmailAndProcessAlreadyBounded {

        return DegreeOfficePublicCandidacyHashCode.getUnusedOrCreateNewHashCode(individualCandidadyProcessClass, parentProcess,
                email);
    }

}
