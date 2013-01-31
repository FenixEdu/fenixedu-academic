package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.UUID;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.util.StringUtils;
import net.sourceforge.fenixedu.util.phd.PhdProperties;
import pt.ist.fenixWebFramework.services.Service;

public class PhdProgramPublicCandidacyHashCode extends PhdProgramPublicCandidacyHashCode_Base {

	private PhdProgramPublicCandidacyHashCode() {
		super();
	}

	@Override
	public boolean hasCandidacyProcess() {
		return hasPhdProgramCandidacyProcess();
	}

	@Override
	final public boolean isFromPhdProgram() {
		return true;
	}

	static public PhdProgramPublicCandidacyHashCode getOrCreatePhdProgramCandidacyHashCode(final String email) {
		final PhdProgramPublicCandidacyHashCode hashCode = getPhdProgramCandidacyHashCode(email);
		if (hashCode != null) {
			return hashCode;
		}
		return create(email);
	}

	@Service
	static private PhdProgramPublicCandidacyHashCode create(final String email) {
		final PhdProgramPublicCandidacyHashCode hash = new PhdProgramPublicCandidacyHashCode();
		hash.setEmail(email);
		hash.setValue(UUID.randomUUID().toString());

		/* Disable alerts */
		// new PublicPhdMissingCandidacyAlert(hash);
		return hash;
	}

	static public PhdProgramPublicCandidacyHashCode getPhdProgramCandidacyHashCode(final String email) {
		if (StringUtils.isEmpty(email)) {
			throw new IllegalArgumentException();
		}

		for (final PublicCandidacyHashCode hashCode : RootDomainObject.getInstance().getCandidacyHashCodesSet()) {
			if (hashCode.isFromPhdProgram() && hashCode.getEmail().equals(email)) {
				return (PhdProgramPublicCandidacyHashCode) hashCode;
			}
		}

		return null;
	}

	public PhdIndividualProgramProcess getIndividualProgramProcess() {
		return getPhdProgramCandidacyProcess().getIndividualProgramProcess();
	}

	public Person getPerson() {
		return getPhdProgramCandidacyProcess().getPerson();
	}

	public String getAccessLink() {
		return PhdProperties.getPublicCandidacyAccessLink() + getValue();
	}

}
