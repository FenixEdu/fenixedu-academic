package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.UUID;

import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import pt.ist.fenixWebFramework.services.Service;

public class DegreeOfficePublicCandidacyHashCode extends DegreeOfficePublicCandidacyHashCode_Base {

    public DegreeOfficePublicCandidacyHashCode() {
	super();
    }

    public boolean isAssociatedWithEmailAndCandidacyProcess(String email, Class<? extends IndividualCandidacyProcess> type,
	    CandidacyProcess process) {
	return email.equals(this.getEmail()) && this.hasIndividualCandidacyProcess()
		&& this.getIndividualCandidacyProcess().getClass() == type
		&& this.getIndividualCandidacyProcess().getCandidacyProcess() == process;
    }

    @Override
    public boolean hasCandidacyProcess() {
	return hasIndividualCandidacyProcess();
    }

    @Override
    public boolean isFromDegreeOffice() {
	return true;
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. If the hash
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    @Service
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCode(
	    Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
	    String email) throws HashCodeForEmailAndProcessAlreadyBounded {

	DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
		email, individualCandidadyProcessClass, parentProcess);

	if (publicCandidacyHashCode == null) {
	    return createNewHashCode(email);
	} else if (!publicCandidacyHashCode.hasCandidacyProcess()) {
	    return publicCandidacyHashCode;
	} else {
	    throw new HashCodeForEmailAndProcessAlreadyBounded();
	}
    }

    private static DegreeOfficePublicCandidacyHashCode createNewHashCode(String email) {
	DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode = new DegreeOfficePublicCandidacyHashCode();
	publicCandidacyHashCode.setEmail(email);
	publicCandidacyHashCode.setValue(UUID.randomUUID().toString());
	return publicCandidacyHashCode;
    }

    public static DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(
	    final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
	    if (!hashCode.isFromDegreeOffice()) {
		continue;
	    }

	    final DegreeOfficePublicCandidacyHashCode hash = (DegreeOfficePublicCandidacyHashCode) hashCode;
	    if (hash.isAssociatedWithEmailAndCandidacyProcess(email, processType, process)) {
		return hash;
	    }
	}

	return null;
    }

    static public DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
	    final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	DegreeOfficePublicCandidacyHashCode associatedHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email,
		processType, process);

	if (associatedHashCode != null) {
	    return associatedHashCode;
	}

	for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
	    if (hashCode.isFromDegreeOffice() && !hashCode.hasCandidacyProcess())
		return (DegreeOfficePublicCandidacyHashCode) hashCode;
	}

	return null;
    }

}
