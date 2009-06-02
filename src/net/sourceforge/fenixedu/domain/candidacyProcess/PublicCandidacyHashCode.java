package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.services.Service;

public class PublicCandidacyHashCode extends PublicCandidacyHashCode_Base {

    public PublicCandidacyHashCode() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean isAssociatedWithEmailAndCandidacyProcess(String email, Class<? extends IndividualCandidacyProcess> type,
	    CandidacyProcess process) {
	return email.equals(this.getEmail()) && this.hasIndividualCandidacyProcess()
		&& this.getIndividualCandidacyProcess().getClass() == type
		&& this.getIndividualCandidacyProcess().getCandidacyProcess() == process;
    }

    @Service
    public void sendEmail(String fromSubject, String body) {
	SystemSender systemSender = getRootDomainObject().getSystemSender();
	new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, fromSubject, body, this.getEmail());
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. If the hash
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    @Service
    public static PublicCandidacyHashCode getUnusedOrCreateNewHashCode(
	    Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
	    String email) throws HashCodeForEmailAndProcessAlreadyBounded {
	PublicCandidacyHashCode publicCandidacyHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
		email, individualCandidadyProcessClass, parentProcess);

	if (publicCandidacyHashCode == null) {
	    return createNewHashCode(email);
	} else if (!publicCandidacyHashCode.hasIndividualCandidacyProcess()) {
	    return publicCandidacyHashCode;
	} else {
	    throw new HashCodeForEmailAndProcessAlreadyBounded();
	}
    }

    private static PublicCandidacyHashCode createNewHashCode(String email) {
	PublicCandidacyHashCode publicCandidacyHashCode = new PublicCandidacyHashCode();
	publicCandidacyHashCode.setEmail(email);
	publicCandidacyHashCode.setValue(UUID.randomUUID().toString());
	return publicCandidacyHashCode;
    }

    public static PublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(final String email,
	    Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	List<PublicCandidacyHashCode> associatedHashes = getHashCodesAssociatedWithEmail(email);

	for (PublicCandidacyHashCode hashCode : associatedHashes) {
	    if (hashCode.isAssociatedWithEmailAndCandidacyProcess(email, processType, process))
		return hashCode;
	}

	return null;
    }

    public static PublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
	    final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	PublicCandidacyHashCode associatedHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email, processType,
		process);

	if (associatedHashCode != null) {
	    return associatedHashCode;
	}

	List<PublicCandidacyHashCode> associatedHashes = getHashCodesAssociatedWithEmail(email);

	for (PublicCandidacyHashCode hashCode : associatedHashes) {
	    if (!hashCode.hasIndividualCandidacyProcess())
		return hashCode;
	}

	return null;
    }

    private static List<PublicCandidacyHashCode> getHashCodesAssociatedWithEmail(final String email) {
	java.util.Set<PublicCandidacyHashCode> publicCandidacyHashCodes = RootDomainObject
		.readAllDomainObjects(PublicCandidacyHashCode.class);
	return new ArrayList<PublicCandidacyHashCode>(CollectionUtils.select(publicCandidacyHashCodes, new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return ((PublicCandidacyHashCode) arg0).getEmail().equals(email);
	    }

	}));
    }

    public static PublicCandidacyHashCode getPublicCandidacyCodeByHash(String hash) {
	if (StringUtils.isEmpty(hash)) {
	    return null;
	}

	java.util.Set<PublicCandidacyHashCode> publicCandidacyHashCodes = RootDomainObject
		.readAllDomainObjects(PublicCandidacyHashCode.class);
	for (PublicCandidacyHashCode hashCode : publicCandidacyHashCodes) {
	    if (hash.equals(hashCode.getValue()))
		return hashCode;
	}

	return null;
    }

}
